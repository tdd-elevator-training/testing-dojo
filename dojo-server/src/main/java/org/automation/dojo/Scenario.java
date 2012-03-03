package org.automation.dojo;

import org.automation.dojo.web.servlet.RequestWorker;

import java.util.List;

/**
 * @author serhiy.zelenin
 */
public abstract class Scenario<T> {
    private int id;
    private BugsQueue bugsQueue;
    protected Bug bug = Bug.NULL_BUG;
    private String description;

    public Scenario(int id, BugsQueue bugsQueue) {
        this(id, "", bugsQueue);
    }

    public Scenario(int id, String description, BugsQueue bugsQueue) {
        this.id = id;
        this.description = description;
        this.bugsQueue = bugsQueue;
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

    public void takeNextBug() {
        this.bug = bugsQueue.nextBugFor(this);
    }

    public String getDescription() {
        return description;
    }

    public abstract String process(T request);

    public abstract List<? extends Bug> getPossibleBugs();

    public boolean hasBug() {
        return bug == null || bug == Bug.NULL_BUG;
    }
}
