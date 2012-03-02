package org.automation.dojo;

/**
 * @author serhiy.zelenin
 */
public class Scenario {
    private int id;
    private BugsQueue bugsQueue;
    private Bug bug;

    public Scenario(int id, BugsQueue bugsQueue) {
        this.id = id;
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
}
