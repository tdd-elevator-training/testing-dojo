package org.automation.dojo;

import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author serhiy.zelenin
 */
public class GameLogService implements LogService {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ReleaseLog currentRelease;
    private ArrayList<ReleaseLog> releases = new ArrayList<ReleaseLog>();
    private final Set<String> registeredPlayers = new HashSet<String>();

    public void playerLog(PlayerRecord record) {
        lock.writeLock().lock();
        try {
            if (!registeredPlayers.contains(record.getPlayerName())) {
                throw new IllegalArgumentException("Player " + record.getPlayerName() + " does not exist!");
            }
            currentRelease.putRecord(record);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<GameLog> getGameLogs(final String player, final BasicScenario scenario) {
        lock.readLock().lock();
        try {
            ArrayList<GameLog> result = new ArrayList<GameLog>();
            for (ReleaseLog release : releases) {
                GameLog gameLog = new GameLog(scenario);
                gameLog.addAll(release.getRecordsFor(player, scenario));
                result.add(gameLog);
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Collection<String> getRegisteredPlayers() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableCollection(registeredPlayers);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void createGameLog(Release release) {
        lock.writeLock().lock();
        try {
            currentRelease = new ReleaseLog(release);
            releases.add(currentRelease);
        }finally{
            lock.writeLock().unlock();
        }
    }


    public List<ReleaseLog> getReleaseLogs() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableList(releases);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean registerPlayer(String name) {
        lock.writeLock().lock();
        try {
            if (registeredPlayers.contains(name)) {
                return false;
            }
            registeredPlayers.add(name);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<BoardRecord> getBoardRecords() {
        lock.readLock().lock();
        try {
            Map<String, Integer> gameScores = new HashMap<String, Integer>();
            for (ReleaseLog release : releases) {
                addReleaseScoresToGameScores(release, gameScores);
            }
            ArrayList<BoardRecord> result = convertGameScores(gameScores);
            Collections.sort(result);
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    private ArrayList<BoardRecord> convertGameScores(Map<String, Integer> gameScores) {
        ArrayList<BoardRecord> result = new ArrayList<BoardRecord>();
        for (Map.Entry<String, Integer> entry : gameScores.entrySet()) {
            result.add(new BoardRecord(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    private void addReleaseScoresToGameScores(ReleaseLog release, Map<String, Integer> gameScores) {
        Map<String, Integer> releaseBoard = release.getBoardRecords();
        for (Map.Entry<String, Integer> releaseEntry : releaseBoard.entrySet()) {
            int total = totalForRelease(gameScores, releaseEntry);
            gameScores.put(releaseEntry.getKey(), total);
        }
    }

    private int totalForRelease(Map<String, Integer> board, Map.Entry<String, Integer> releaseEntry) {
        int total = releaseEntry.getValue();
        String playerName = releaseEntry.getKey();
        if (board.containsKey(playerName)) {
            total += board.get(playerName);
        }
        return total;
    }
}
