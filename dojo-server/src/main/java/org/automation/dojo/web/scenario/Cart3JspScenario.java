package org.automation.dojo.web.scenario;

import org.automation.dojo.BugsQueue;
import org.automation.dojo.web.servlet.RequestWorker;

public class Cart3JspScenario extends TerminatorScenario {

    public Cart3JspScenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    public boolean activate(RequestWorker request) {
        return request.isCartAction();
    }

    @Override
    protected String getUrl() {
        return "cart_level3.jsp";
    }

}
