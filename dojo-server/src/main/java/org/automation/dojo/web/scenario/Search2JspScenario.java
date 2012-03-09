package org.automation.dojo.web.scenario;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.BugsQueue;
import org.automation.dojo.web.bugs.AddExistingItemWithPriceLessThanEntered;
import org.automation.dojo.web.bugs.AddExistingItemWithPriceMoreThanEntered;
import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.Arrays;
import java.util.List;

public class Search2JspScenario extends TerminatorScenario {

    public Search2JspScenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    public boolean activate(RequestWorker request) {
        return request.isSearchAction();
    }

    @Override
    protected String getUrl() {
        return "search_level2.jsp";
    }
}
