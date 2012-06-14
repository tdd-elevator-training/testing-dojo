package org.automation.dojo;

import org.automation.dojo.web.scenario.BasicScenario;

import java.util.Date;

/**
 * @author serhiy.zelenin
 */
public class PlayerRecord {


    public enum Type{DUPLICATE, VALID_BUG, MISSED, LIAR, TIMEOUT, PASSED, EXCEPTION}

    private int score;
    private String description;
    private Type type;
    protected BasicScenario scenario;
    private boolean passed;
    private String playerName;
    private Date logTime;

    public PlayerRecord(String playerName, BasicScenario scenario, boolean passed,
                        int score, String description, Type type) {
        this(playerName, scenario, passed, score, description, type, new Date());
    }

    public PlayerRecord(String playerName, BasicScenario scenario, boolean passed, 
            int score, String description, Type type, Date logTime) {
        this.playerName = playerName;
        this.scenario = scenario;
        this.passed = passed;
        this.score = score;
        this.description = description;
        this.type = type;
        this.logTime = logTime;
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

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getPlayerName() {
        return playerName;
    }


    public long getLogTime() {
        return logTime.getTime();
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String toString() {
        return String.format("Scenario #%s - %s: %s",
                scenario.getId(),
                (scenario.bugsFree() ? "passed" : "failed"),
                description) +
            ((score == 0) ? "" : String.format(". You get %s points", ((score >= 0)?"+":"") + String.valueOf(score)));
    }
}
