package org.automation.dojo.web.servlet;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.ReleaseEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WorkflowController  extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        try {
            ReleaseEngine engine = ApplicationContextLocator.getInstance().getBean("releaseEngine");
            String url = engine.getCurrentRelease().process(new RequestWorkerImpl(request));

            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception exception) {
            exception.printStackTrace();
            request.setAttribute("error_message", exception.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}

