package org.automation.dojo;

import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;

import java.util.*;

/**
 * @author serhiy.zelenin
 */
public class ReleaseLog {
    private Release release;
    private Date releaseDate;

    private List<PlayerRecord> records = new ArrayList<PlayerRecord>();

    public ReleaseLog(Release release, Date releaseDate) {
        this.release = release;
        this.releaseDate = releaseDate;
    }

    public void putRecord(PlayerRecord record) {
        records.add(record);
    }

    public List<PlayerRecord> getRecordsFor(final String playerName, final BasicScenario scenario) {
        ArrayList<PlayerRecord> result = new ArrayList<PlayerRecord>();
        for (PlayerRecord playerRecord : records) {
            if (playerRecord.getPlayerName().equals(playerName) && playerRecord.getScenario().equals(scenario)) {
                result.add(playerRecord);
            }
        }
        return result;
    }

    public Release getRelease() {
        return release;
    }

    private List<PlayerRecord> getRecordsForPlayer(String player) {
        ArrayList<PlayerRecord> result = new ArrayList<PlayerRecord>();
        for (PlayerRecord playerRecord : records) {
            if (playerRecord.getPlayerName().equals(player)) {
                result.add(playerRecord);
            }
        }
        return result;
    }

    public Map<String, Integer> getBoardRecords() {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        for (PlayerRecord record : records) {
            int score = record.getScore();
            if (result.containsKey(record.getPlayerName())) {
                score += result.get(record.getPlayerName());
            }
            result.put(record.getPlayerName(), score);
        }
        return result;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public long getReleaseTime() {
        return releaseDate.getTime();
    }

    public List<PlayerRecord> getLastRecordsForPlayer(String playerName, int maxLogRecordsAmount) {
        List<PlayerRecord> playerRecords = getRecordsForPlayer(playerName);
        int fromIndex = playerRecords.size() - maxLogRecordsAmount > 0 ? playerRecords.size() - maxLogRecordsAmount : 0;
        return playerRecords.subList(fromIndex, playerRecords.size());
    }

    public void clearRecords() {
        records.clear();
    }
}
