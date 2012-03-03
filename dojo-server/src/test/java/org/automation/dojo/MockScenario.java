package org.automation.dojo;

import org.automation.dojo.BugsQueue;
import org.automation.dojo.Scenario;

import java.util.List;

public class MockScenario extends Scenario {
    public MockScenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    public String process(Object request) {
        return null;
    }

    @Override
    public List<? extends Bug> getPossibleBugs() {
        return null;
    }

}
