package org.automation.dojo.web.servlet;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.ReleaseEngine;
import org.automation.dojo.web.scenario.Release;

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
            clearCartIfNeeded(request);

            ReleaseEngine engine = ApplicationContextLocator.getBean("releaseEngine");
            String url = getCurrentRelease(engine).process(new RequestWorkerImpl(request));

            request.setAttribute("context", getContext());
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception exception) {
            exception.printStackTrace();
            request.setAttribute("error_message", exception.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    protected Release getCurrentRelease(ReleaseEngine engine) {
        return engine.getCurrentRelease();
    }

    protected String getContext() {
        return "search";
    }

    /**
     * Этот метод используется в тестовых целях, иногда просто надо почистить карт между вызовами двух тестов SearchPageLevel3
     */
    protected void clearCartIfNeeded(HttpServletRequest request) {
        if (request.getParameter("clear") != null) {
            request.getSession().setAttribute("cart", null);
        }
    }
}

