package org.example.pluto.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Logout", value = "/Logout")
public class Logout extends HttpServlet {
    public Logout() {
        System.out.println("init");
    }
    public void init(ServletConfig config) throws ServletException {
        System.out.println("init");
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        System.out.println("doGet1  ");
        destroy();
        System.out.println("doGet");
    }
}
