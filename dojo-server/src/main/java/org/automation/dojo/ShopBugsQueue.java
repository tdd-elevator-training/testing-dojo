package org.automation.dojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * @author serhiy.zelenin
 */
public class ShopBugsQueue implements BugsQueue{
    public Bug nextBugFor(Scenario scenario) {    // TODO test this
        ArrayList<Bug> possibleBugs = new ArrayList<Bug>(scenario.getPossibleBugs());
        possibleBugs.add(Bug.NULL_BUG);
        int bugIndex = 1; // new Random().nextInt(possibleBugs.size() - 1);
        return possibleBugs.get(bugIndex);
    }
}
