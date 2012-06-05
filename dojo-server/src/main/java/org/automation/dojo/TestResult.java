package org.automation.dojo;

/**
 * User: serhiy.zelenin
 * Date: 6/1/12
 * Time: 3:07 PM
 */
public class TestResult {
    private int scenarioId;
    private TestStatus testStatus;

    public TestResult(int scenarioId, TestStatus testStatus) {
        this.scenarioId = scenarioId;
        this.testStatus = testStatus;
    }

    public int getScenarioId() {
        return scenarioId;
    }

    public TestStatus getTestStatus() {
        return testStatus;
    }
}
