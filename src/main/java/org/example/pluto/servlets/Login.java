package org.example.pluto.servlets;

import com.google.gson.Gson;
import org.example.pluto.common.LoginLambdaFunction;
import org.example.pluto.model.User;
import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Login", value = "/Login")
public class Login extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (request.getParameterMap().containsKey("username") && request.getParameterMap().containsKey("password")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            User user = (new LoginLambdaFunction()).login(username, password);
            if (user == null) {
                JSONObject obj = new JSONObject();
                obj.put("error", "No Users Found with given username and password pair");
                response.getWriter().write(obj.toJSONString());
            }else {
                //Add auth cookie
                Cookie auth_cookie = new Cookie("auth_user_id", String.format("%s@%s", user.getId(), user.getHashCode()));
                auth_cookie.setMaxAge(3600*12);
                auth_cookie.setSecure(true);
                auth_cookie.setDomain(request.getServerName());
                auth_cookie.setPath(request.getContextPath());
                response.addCookie(auth_cookie);
                JSONObject obj = new JSONObject();
                obj.put("success", "Successfully logged in");
                obj.put("user", user);
                response.getWriter().println((new Gson()).toJson(obj));
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }else{
            JSONObject obj = new JSONObject();
            obj.put("error", "username and password field not found");
            response.getWriter().println((new Gson()).toJson(obj));
        }
    }
}
