package org.example.pluto.servlets;

import com.google.gson.Gson;
import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "Logout", value = "/Logout")
public class Logout extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject obj = new JSONObject();
        HttpSession session = request.getSession(false);
        if (session == null) {
            //noinspection unchecked
            obj.put("status", "session invalid");
        } else {
            request.getSession(false).invalidate();
            //noinspection unchecked
            obj.put("success", "Successfully logged out");
        }
        response.getWriter().println((new Gson()).toJson(obj));
    }
}
