package org.automation.dojo;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author serhiy.zelenin
 */
public class GameLogService implements LogService {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ReleaseLogs currentRelease;
    private ArrayList<ReleaseLogs> releases = new ArrayList<ReleaseLogs>();
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
            for (ReleaseLogs release : releases) {
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
            currentRelease = new ReleaseLogs(release);
            releases.add(currentRelease);
        }finally{
            lock.writeLock().unlock();
        }
    }
}
