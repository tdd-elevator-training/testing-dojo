package org.automation.dojo;

import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.scenario.BasicScenario;

import java.util.Arrays;
import java.util.List;

public class MockScenario extends BasicScenario {
    private int currentBugNumber = -1;
    private Bug nextBug;

    public MockScenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    public String process(Object request) {
        return null;
    }

    @Override
    public List<? extends Bug> getPossibleBugs() {
        return Arrays.asList(new MockBug(1), new MockBug(2));
    }

    public void setNextBug(Bug nextBug) {
        this.nextBug = nextBug;
    }

    @Override
    public void takeNextBug() {
        bug = nextBug;
    }
}
