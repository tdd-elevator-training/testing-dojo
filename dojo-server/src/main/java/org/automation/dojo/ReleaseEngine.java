package org.automation.dojo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SerializationUtils;
import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.fest.reflect.core.Reflection.constructor;

/**
 * @author serhiy.zelenin
 */
public class ReleaseEngine {
    private List<Release> releases = new ArrayList<Release>();
    protected int currentReleaseIndex = 0;

    private Resource scenarioResource;

    @Autowired
    private BugsQueue bugsQueue;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private LogService logService;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private int minorNumber = 1;

    public ReleaseEngine() {
    }

    public ReleaseEngine(BugsQueue bugsQueue, ScoreService scoreService, LogService logService) {
        this.bugsQueue = bugsQueue;
        this.scoreService = scoreService;
        this.logService = logService;
    }

    public Release getCurrentRelease() {
        lock.readLock().lock();
        try {
            return (Release) SerializationUtils.clone(releases.get(currentReleaseIndex));
        } finally {
            lock.readLock().unlock();
        }
    }

    public void nextMajorRelease() {
        lock.writeLock().lock();
        try {
            if (currentReleaseIndex == releases.size() - 1) {
                return;
            }
            scoreService.nextRelease(getCurrentRelease());
            setMajorRelease(currentReleaseIndex + 1);
            logService.createGameLog(getCurrentRelease());
            minorNumber = 1;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void nextMinorRelease() {
        lock.writeLock().lock();
        try {
            scoreService.nextRelease(getCurrentRelease());
            currentRelease().takeNextBug();
            logService.createGameLog(getCurrentRelease());
            minorNumber++;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Release currentRelease() {
        return releases.get(currentReleaseIndex);
    }

    private void notifyServices() {
        logService.createGameLog(getCurrentRelease());
        scoreService.nextRelease(getCurrentRelease());
    }

    public void init() {
        releases.addAll(new ReleaseLoder(scenarioResource).getReleases(bugsQueue));
        notifyServices();
    }

    public void setScenarioResource(Resource scenarioResource) {
        this.scenarioResource = scenarioResource;
    }

    public BasicScenario getScenario(int scenarioId) {
        lock.readLock().lock();
        try {
            return getCurrentRelease().getScenario(scenarioId);
        } finally {
            lock.readLock().unlock();
        }
    }

    public String getMinorInfo() {
        return currentRelease().toString();
    }

    //WARN!! public for testing only!!!
    public void setMajorRelease(int index) {
        lock.writeLock().lock();
        try {
            currentReleaseIndex = index;
            currentRelease().setNoBug();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<BasicScenario> getCurrentScenarios() {
        lock.readLock().lock();
        try {
            return getCurrentRelease().getScenarios();
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getMinorNumber() {
        return minorNumber;
    }

    public int getMajorNumber() {
        return currentReleaseIndex;
    }

    public String getMajorInfo() {
        return String.valueOf(currentReleaseIndex);
    }
}
