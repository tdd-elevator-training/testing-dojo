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
        return findReportedBugs() != null;
    }

    public PlayerRecord findReportedBugs() {
        List<PlayerRecord> playerRecords = getPlayerRecords();
        for (PlayerRecord record : playerRecords) {
            if (!record.isPassed() && record.getType() == PlayerRecord.Type.VALID_BUG) {
                return record;
            }
        }
        return null;
    }

    public boolean liarReported() {
        List<PlayerRecord> records = getPlayerRecords();
        for (PlayerRecord record : records) {
            if (record.getType() == PlayerRecord.Type.LIAR) {
                return true;
            }
        }
        return false;
    }
}
