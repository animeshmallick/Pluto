package org.example.pluto.servlets;

import com.google.gson.Gson;
import org.json.simple.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;


@WebFilter("/*")
public class ServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //Filter.super.init(filterConfig);
        //log.info("ServletFilter init");
    }

    @Override
    public void destroy() {
        //Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("ServletFilter doFilter");
        if (isAuthenticated(servletRequest))
            filterChain.doFilter(servletRequest, servletResponse);
        else {
            System.out.println("ServletFilter doFilter: not authenticated");
            JSONObject obj = new JSONObject();
            obj.put("errorMessage", "You are not authenticated to view this content");
            obj.put("resolution", "Login and try again");
            servletResponse.getWriter().println((new Gson()).toJson(obj));
        }
    }

    private boolean isAuthenticated(ServletRequest request) {
        String servletName = ((HttpServletRequest)request).getServletPath();
        if(servletName.contains("/GetProducts") || servletName.contains("/Logout")) {
            Cookie[] cookies = ((HttpServletRequest) request).getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("auth_user_id")) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}
