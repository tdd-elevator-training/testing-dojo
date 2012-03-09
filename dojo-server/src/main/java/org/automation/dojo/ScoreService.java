package org.automation.dojo;

import org.automation.dojo.web.scenario.Release;

public interface ScoreService {
    boolean testResult(String clientName, int scenarioNumber, TestResult testResult);

    void nextRelease(Release previousRelease);

    void tick(long currentTime);
}
