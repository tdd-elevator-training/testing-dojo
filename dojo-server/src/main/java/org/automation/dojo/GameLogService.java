package org.automation.dojo;

import com.google.common.collect.Iterables;
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
    private Set<String> addresses = new HashSet<String>();

    public void playerLog(PlayerRecord record) {
        lock.writeLock().lock();
        try {
            addresses.add(record.getClientAddress());
            currentRelease.putRecord(record);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<GameLog> getGameLogs(final String clientAddress, final BasicScenario scenario) {
        lock.readLock().lock();
        try {
            ArrayList<GameLog> result = new ArrayList<GameLog>();
            for (ReleaseLog release : releases) {
                GameLog gameLog = new GameLog(scenario);
                gameLog.addAll(release.getRecordsFor(clientAddress, scenario));
                result.add(gameLog);
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Collection<String> getUniqueClientAddresses() {
        return addresses;
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

    public List<ReleaseLog> getReleaseLogsForHost(String clientAddress) {
        lock.readLock().lock();
        try {
            return new ArrayList<ReleaseLog>(releases);
        } finally {
            lock.readLock().unlock();
        }
    }
}
