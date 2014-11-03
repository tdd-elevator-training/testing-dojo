package org.automation.dojo;

import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.scenario.BasicScenario;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author serhiy.zelenin
 */
public class ShopBugsQueue implements BugsQueue {

    @Autowired
    private Dice dice;

    public Bug nextBugFor(BasicScenario scenario) {
        ArrayList<Bug> possibleBugs = new ArrayList<Bug>(scenario.getPossibleBugs());
        possibleBugs.add(Bug.NULL_BUG);
        int bugIndex = dice.next(possibleBugs.size());
        Bug bug = possibleBugs.get(bugIndex);
        if (!scenario.getBug().isNull() && bug.equals(scenario.getBug())) {
            return nextBugFor(scenario);
        }
        return bug;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }
}
