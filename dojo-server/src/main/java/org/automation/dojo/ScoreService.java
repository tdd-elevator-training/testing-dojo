package org.automation.dojo;

import org.automation.dojo.web.scenario.Release;

public interface ScoreService {
    boolean testResult(String clientName, String clientAddress, int scenarioNumber, boolean testPassed);

    void nextRelease(Release previousRelease);
}
