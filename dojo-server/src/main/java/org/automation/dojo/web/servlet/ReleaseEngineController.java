package org.automation.dojo.web.servlet;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.ReleaseEngine;

public class ReleaseEngineController extends Controller {

    @Override
    public String doAction() {
        ReleaseEngine engine = ApplicationContextLocator.getInstance().getBean(ReleaseEngine.class);
        if (request.getParameter("next_minor") == "true") {
            engine.nextMinorRelease();
        } else if (request.getParameter("next_major") == "true") {
            engine.nextMajorRelease();
        }

        return "releases.jsp";
    }

}
