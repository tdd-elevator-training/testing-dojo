package org.automation.dojo;

import org.automation.dojo.BugsQueue;
import org.automation.dojo.Scenario;

public class MockScenario extends Scenario {
    public MockScenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    protected String process() {
        return null;
    }
}
