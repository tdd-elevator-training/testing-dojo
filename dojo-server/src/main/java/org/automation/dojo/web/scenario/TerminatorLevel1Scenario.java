package org.automation.dojo.web.scenario;

import org.automation.dojo.BugsQueue;
import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.Arrays;
import java.util.List;

public class TerminatorLevel1Scenario extends BasicScenario<RequestWorker> {

    public TerminatorLevel1Scenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    public String process(RequestWorker request) {
        return "search_level1.jsp";
    }

    public List<? extends Bug> getPossibleBugs() {
        return Arrays.asList();
    }

}
