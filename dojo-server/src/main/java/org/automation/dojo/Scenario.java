package org.automation.dojo;

/**
 * @author serhiy.zelenin
 */
public class Scenario {
    private int id;
    private BugsQueue bugsQueue;
    private Bug bug;
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

    public boolean hasBug() {
        return bug == null || bug == Bug.NULL_BUG;
    }
}
