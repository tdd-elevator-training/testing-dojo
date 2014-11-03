package org.automation.dojo.web.servlet;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.ReleaseEngine;
import org.automation.dojo.web.scenario.Release;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BugsFreeWorkflowController extends WorkflowController {

    @Override
    protected Release getCurrentRelease(ReleaseEngine engine) {
        return engine.getCurrentBugsFreeRelease();
    }

    @Override
    protected String getContext() {
        return "production";
    }

}

