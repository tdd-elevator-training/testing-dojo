package org.automation.dojo.web.servlet;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.ReleaseEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public abstract class Controller extends HttpServlet {

    protected HttpServletResponse response;
    protected HttpServletRequest request;
    protected HttpSession session;

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        this.response = response;
        this.request = request;
        this.session = request.getSession();

        try {
            goTo(doAction());
        } catch (Exception exception) {
            exception.printStackTrace();
            request.setAttribute("error_message", exception.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void goTo(final String url) throws ServletException, IOException {
        request.getRequestDispatcher(url).forward(request, response);
    }

    public abstract String doAction() throws ServletException, IOException;
}
