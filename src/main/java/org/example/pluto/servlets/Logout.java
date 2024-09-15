package org.example.pluto.servlets;

import com.google.gson.Gson;
import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Logout", value = "/Logout")
public class Logout extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        boolean logged_out = false;
        JSONObject obj = new JSONObject();
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("auth_user_id")) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                obj.put("success", "Successfully logged out");
                logged_out = true;
            }
        }
        if (!logged_out) {
            obj.put("error", "Nothing to logout from");
        }
        response.getWriter().println((new Gson()).toJson(obj));
    }
}
