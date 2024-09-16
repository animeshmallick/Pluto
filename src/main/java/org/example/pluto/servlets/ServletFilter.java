package org.example.pluto.servlets;

import com.google.gson.Gson;
import org.example.pluto.common.LoginLambdaFunction;
import org.example.pluto.model.User;
import org.json.simple.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebFilter("/*")
public class ServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        //Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        //Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (isAuthenticated(servletRequest))
            filterChain.doFilter(servletRequest, servletResponse);
        else {
            JSONObject obj = new JSONObject();
            //noinspection unchecked
            obj.put("errorMessage", "You are not authenticated to view this content");
            //noinspection unchecked
            obj.put("resolution", "Login and try again");
            servletResponse.getWriter().println((new Gson()).toJson(obj));
        }
    }

    private boolean isAuthenticated(ServletRequest request) {
        String servletName = ((HttpServletRequest)request).getServletPath();
        if(servletName.contains("/GetProducts") || servletName.contains("/Logout")) {
            HttpSession session = ((HttpServletRequest)request).getSession(false);
            if (session == null)
                return false;
            String auth = (String) session.getAttribute("auth_user_id");
            if(auth == null)
                return false;

            String[] value = auth.split("@");
            if (value.length != 2)
                return false;
            String username = value[0];
            int hashedUsername = Integer.parseInt(value[1]);
            if (username.hashCode() != hashedUsername)
                return false;
            User user = (new LoginLambdaFunction()).validateUser(username);
            return user != null;
        }
        return true;
    }
}
