package org.automation.dojo;

import java.util.Arrays;
import java.util.List;

/**
 * @author serhiy.zelenin
 */
public class GameLog {
    private Scenario scenario;
    private List<PlayerRecord> playerRecords;

    public GameLog(Scenario scenario, PlayerRecord ... records) {
        this.scenario = scenario;
        this.playerRecords = Arrays.asList(records);
    }

    public Scenario getScenario() {
        return scenario;
    }

    public List<PlayerRecord> getPlayerRecords() {
        return playerRecords;
    }

    public boolean hasNoRecords() {
        return playerRecords.isEmpty();
    }

    public boolean bugReported() {
        List<PlayerRecord> playerRecords = getPlayerRecords();
        for (PlayerRecord record : playerRecords) {
            if (!record.isPassed()) {
                return true;
            }
        }
        return false;
    }
}
