package org.automation.dojo;

import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.scenario.BasicScenario;

/**
 * @author serhiy.zelenin
 */
public interface BugsQueue {
    Bug nextBugFor(BasicScenario scenario);
}
