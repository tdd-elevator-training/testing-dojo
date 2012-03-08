package org.automation.dojo.web.servlet;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.ReleaseEngine;

import javax.servlet.ServletException;
import java.io.IOException;

public class WorkflowController extends Controller {

    @Override
    public String doAction() throws ServletException, IOException {
        ReleaseEngine engine = ApplicationContextLocator.getInstance().getBean("releaseEngine");
        return engine.getCurrentRelease().process(new RequestWorkerImpl(request));
    }
}
