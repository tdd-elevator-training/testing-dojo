package org.automation.dojo.web.servlet;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.ReleaseEngine;

public class ReleaseEngineController extends Controller {

    @Override
    public String doAction() {
        ReleaseEngine engine = ApplicationContextLocator.getInstance().getBean("releaseEngine");
        if (isMinorClicked()) {
            engine.nextMinorRelease();
        } else if (isMajorClicked()) {
            engine.nextMajorRelease();
        }

        return "releases.jsp";
    }

    private boolean isMajorClicked() {
        return "true".equals(request.getParameter("next_major"));
    }

    private boolean isMinorClicked() {
        return "true".equals(request.getParameter("next_minor"));
    }

}
