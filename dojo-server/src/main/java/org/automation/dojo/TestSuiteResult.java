package org.automation.dojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: serhiy.zelenin
 * Date: 6/1/12
 * Time: 3:03 PM
 */
public class TestSuiteResult {
    private String playerName;
    private long timestamp;

    private Map<Integer, List<TestStatus>> testResults = new HashMap<Integer, List<TestStatus>>();

    public TestSuiteResult(String playerName, long timestamp) {
        this.playerName = playerName;
        this.timestamp = timestamp;
    }

    public void addTestResult(int scenarioId, TestStatus testStatus) {
        List<TestStatus> scenarioResults = testResults.get(scenarioId);
        if (scenarioResults == null) {
            scenarioResults = new ArrayList<TestStatus>();
        }
        scenarioResults.add(testStatus);
        testResults.put(scenarioId, scenarioResults);
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<Integer, List<TestStatus>> getScenarioResults() {
        return testResults;
    }

    public TestStatus getStatusForScenario(Integer scenarioId) {
        List<TestStatus> scenarioResults = testResults.get(scenarioId);
        for (TestStatus scenarioResult : scenarioResults) {
            if (scenarioResult == TestStatus.FAILED) {
                return TestStatus.FAILED;
            }
        }
        return TestStatus.PASSED;
    }
}
