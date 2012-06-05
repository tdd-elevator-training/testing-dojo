package org.automation.dojo;

import java.util.ArrayList;
import java.util.List;

/**
 * User: serhiy.zelenin
 * Date: 6/1/12
 * Time: 3:03 PM
 */
public class TestSuiteResult {
    private String playerName;
    private long timestamp;

    private List<TestResult> testResults = new ArrayList<TestResult>();

    public TestSuiteResult(String playerName, long timestamp) {
        this.playerName = playerName;
        this.timestamp = timestamp;
    }

    public void addTestResult(int scenarioId, TestStatus testStatus) {
        testResults.add(new TestResult(scenarioId, testStatus));
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<TestResult> getTestResults() {
        return testResults;
    }
}
