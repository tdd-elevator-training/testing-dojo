package org.automation.dojo.web.scenario;

import org.automation.dojo.web.bugs.Bug;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author serhiy.zelenin
 */
public abstract class BasicScenario<T> implements Scenario<T> {

    private int id;
    protected Bug bug;
    private String description;

    public BasicScenario(int id) {
        this(id, "");
    }

    public BasicScenario(int id, String description) {
        this.id = id;
        this.description = description;
//        this.bugsQueue = bugsQueue;
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
        // TODO test this
        ArrayList<Bug> possibleBugs = new ArrayList<Bug>(getPossibleBugs());
        possibleBugs.add(Bug.NULL_BUG);
        int bugIndex = new Random().nextInt(possibleBugs.size());
        this.bug = possibleBugs.get(bugIndex);
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
