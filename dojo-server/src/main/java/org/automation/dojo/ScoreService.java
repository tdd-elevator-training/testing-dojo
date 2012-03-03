package org.automation.dojo;

public interface ScoreService {
    boolean testResult(String clientName, String clientAddress, int scenarioNumber, boolean testPassed);

    void nextRelease(Release previousRelease);
}
