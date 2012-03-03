package org.automation.dojo.web.scenario;

import org.automation.dojo.Bug;
import org.automation.dojo.BugsQueue;
import org.automation.dojo.Scenario;
import org.automation.dojo.web.bugs.NoResultWhenExpected;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.Arrays;
import java.util.List;

public class SearchByTextLevel2Scenario extends Scenario<RequestWorker> {

    public SearchByTextLevel2Scenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    public String process(RequestWorker request) {
        new SearchByTextLevel1Scenario().process(request);

        new SearchByPriceLevel2Scenario().process(request);
        return "search_level2.jsp";
    }

    public List<? extends Bug> getPossibleBugs() {
        return Arrays.asList(new NoResultWhenExpected());
    }

}
