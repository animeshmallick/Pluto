package org.example.pluto.servlets;

import com.google.gson.Gson;
import org.example.pluto.common.LoginLambdaFunction;
import org.example.pluto.model.User;
import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
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
                //noinspection unchecked
                obj.put("error", "No Users Found with given username and password pair");
                response.getWriter().write(obj.toJSONString());
            }else {
                request.getSession().setAttribute("auth_user_id", String.format("%s@%s", user.getId(), user.getHashCode()));
                JSONObject obj = new JSONObject();
                //noinspection unchecked
                obj.put("success", "Successfully logged in");
                //noinspection unchecked
                obj.put("user", user);
                response.getWriter().println((new Gson()).toJson(obj));
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }else{
            JSONObject obj = new JSONObject();
            //noinspection unchecked
            obj.put("error", "username and password field not found");
            response.getWriter().println((new Gson()).toJson(obj));
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        JSONObject obj = new JSONObject();

        if (session == null) {
            //noinspection unchecked
            obj.put("message", "Invalid Session. Login and try again");
        }else {
            String auth_user_id = (String) session.getAttribute("auth_user_id");
            if (auth_user_id == null) {
                //noinspection unchecked
                obj.put("message", "No users logged in. Login and try again");
            }
            else {
                //noinspection unchecked
                obj.put("message", "User logged in as " + auth_user_id);
            }
        }
        response.getWriter().println((new Gson()).toJson(obj));
    }
}
