package org.automation.dojo;

/**
 * @author serhiy.zelenin
 */
public interface BugsQueue {
    Bug nextBugFor(Scenario scenario);
}
