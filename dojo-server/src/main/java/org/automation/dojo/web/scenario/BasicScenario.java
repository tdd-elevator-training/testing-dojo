package org.automation.dojo.web.scenario;

import org.automation.dojo.BugsQueue;
import org.automation.dojo.web.bugs.Bug;

import java.io.Serializable;
import java.util.List;

/**
 * @author serhiy.zelenin
 */
public abstract class BasicScenario<T> implements Scenario<T>, Serializable {

    private static final long serialVersionUID = -7932822537221318057L;

    private int id;
    private transient BugsQueue bugsQueue;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BasicScenario that = (BasicScenario) o;

        if (id != that.id) {
            return false;
        }

        return true;
    }

}
