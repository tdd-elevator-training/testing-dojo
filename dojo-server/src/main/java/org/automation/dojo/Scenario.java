package org.automation.dojo;

import org.automation.dojo.web.servlet.RequestWorker;

/**
 * @author serhiy.zelenin
 */
public abstract class Scenario {
    private int id;
    private BugsQueue bugsQueue;
    private Bug bug;
    private String description;

    protected RequestWorker request;

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

    public String process(RequestWorker request) {
        this.request = request;
        return process();
    }

    protected abstract String process();
}
