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
/*
            if (!registeredPlayers.contains(record.getPlayerName())) {
                throw new IllegalArgumentException("Player " + record.getPlayerName() + " does not exist!");
            }
*/
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
}
