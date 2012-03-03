package org.automation.dojo;

/**
 * @author serhiy.zelenin
 */
public class PlayerRecord {
    private int score;
    private int scenario;
    private boolean passed;
    private String clientName;
    private String clientAddress;

    public PlayerRecord(String clientName, String clientAddress, int scenario, boolean passed, int score) {
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.scenario = scenario;
        this.passed = passed;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getScenario() {
        return scenario;
    }

    public boolean isPassed() {
        return passed;
    }
}
