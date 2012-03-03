package org.automation.dojo;

import org.automation.dojo.web.scenario.BasicScenario;

/**
 * @author serhiy.zelenin
 */
public class PlayerRecord {
    private int score;
    private String description;
    private BasicScenario scenario;
    private boolean passed;
    private String clientName;
    private String clientAddress;

    public PlayerRecord(String clientName, String clientAddress, BasicScenario scenario, boolean passed, int score,
            String description) {
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.scenario = scenario;
        this.passed = passed;
        this.score = score;
        this.description = description;
    }

    public int getScore() {
        return score;
    }

    public BasicScenario getScenario() {
        return scenario;
    }

    public boolean isPassed() {
        return passed;
    }
}
