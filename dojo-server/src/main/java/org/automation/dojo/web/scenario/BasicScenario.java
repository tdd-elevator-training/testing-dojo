package org.automation.dojo.web.scenario;

import org.automation.dojo.BugsQueue;
import org.automation.dojo.web.bugs.Bug;

import java.util.List;

/**
 * @author serhiy.zelenin
 */
public abstract class BasicScenario<T> implements Scenario<T> {

    private int id;
    private BugsQueue bugsQueue;
    protected Bug bug;
    private String description;

    public BasicScenario(int id, BugsQueue bugsQueue) {
        this(id, "", bugsQueue);
    }

    public BasicScenario(int id, String description, BugsQueue bugsQueue) {
        this.id = id;
        this.description = description;
        this.bugsQueue = bugsQueue;
        setNoBug();
    }

    public int getId() {
        return id;
    }

    public Bug getBug() {
        return bug;
    }

    public void setBug(Bug bug) {
        this.bug = bug;
    }

    @Override
    public void takeNextBug() {
        this.bug = bugsQueue.nextBugFor(this);
    }

    public String getDescription() {
        return description;
    }

    public abstract List<? extends Bug> getPossibleBugs();

    public boolean bugsFree() {
        return bug == null || bug == Bug.NULL_BUG;
    }

    @Override
    public String toString() {
        return String.format("Scenario %s with bug %s",
                getClass().getSimpleName(),
                bug.toString());
    }

    @Override
    public void setNoBug() {
        this.bug = Bug.NULL_BUG;
    }
}
