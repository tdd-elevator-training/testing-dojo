package org.automation.dojo;

/**
 * @author serhiy.zelenin
 */
public class PlayerRecord {
    private int score;
    private Scenario scenario;
    private boolean passed;
    private String clientName;
    private String clientAddress;

    public PlayerRecord(String clientName, String clientAddress, Scenario scenario, boolean passed, int score) {
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.scenario = scenario;
        this.passed = passed;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public boolean isPassed() {
        return passed;
    }
}