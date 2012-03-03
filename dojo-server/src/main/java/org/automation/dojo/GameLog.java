package org.automation.dojo;

import org.automation.dojo.web.scenario.BasicScenario;

import java.util.Arrays;
import java.util.List;

/**
 * @author serhiy.zelenin
 */
public class GameLog {
    private BasicScenario scenario;
    private List<PlayerRecord> playerRecords;

    public GameLog(BasicScenario scenario, PlayerRecord ... records) {
        this.scenario = scenario;
        this.playerRecords = Arrays.asList(records);
    }

    public BasicScenario getScenario() {
        return scenario;
    }

    public List<PlayerRecord> getPlayerRecords() {
        return playerRecords;
    }

    public boolean hasNoRecords() {
        return playerRecords.isEmpty();
    }

    public boolean bugReported() {
        return findReportedRecord() != null;
    }

    public PlayerRecord findReportedRecord() {
        List<PlayerRecord> playerRecords = getPlayerRecords();
        for (PlayerRecord record : playerRecords) {
            if (!record.isPassed()) {
                return record;
            }
        }
        return null;
    }
}
