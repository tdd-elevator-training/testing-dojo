package org.automation.dojo;

import com.google.common.collect.Iterables;
import org.automation.dojo.web.scenario.BasicScenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author serhiy.zelenin
 */
public class GameLog {
    private BasicScenario scenario;
    private Date releaseDate;
    private List<PlayerRecord> playerRecords = new ArrayList<PlayerRecord>();

    public GameLog(BasicScenario scenario, Date releaseDate, PlayerRecord ... records) {
        this.scenario = scenario;
        this.releaseDate = releaseDate;
        this.playerRecords = new ArrayList<PlayerRecord>(Arrays.asList(records));
    }

    public GameLog(BasicScenario scenario, Iterable<PlayerRecord> records) {
        this.scenario = scenario;
        Iterables.addAll(playerRecords, records);
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

    public void addAll(List<PlayerRecord> playerRecords) {
        this.playerRecords.addAll(playerRecords);
    }

    public long getReleaseDateMilis() {
        return releaseDate.getTime();
    }
}
