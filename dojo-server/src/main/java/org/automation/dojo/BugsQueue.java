package org.automation.dojo;

import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.scenario.Scenario;

/**
 * @author serhiy.zelenin
 */
public interface BugsQueue {
    Bug nextBugFor(Scenario scenario);
}
