package org.automation.dojo.web.scenario;

import org.automation.dojo.BugsQueue;
import org.automation.dojo.web.servlet.RequestWorker;

public class Search1JspScenario extends TerminatorScenario {

    public Search1JspScenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    public boolean activate(RequestWorker request) {
        return request.isSearchAction();
    }

    @Override
    protected String getUrl() {
        return "search_level1.jsp";
    }

}
