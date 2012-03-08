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
        try {
            List<String> scenarioLines = IOUtils.readLines(scenarioResource.getInputStream());
            int i = 0;
            int currentReleaseNumber = 1;
            Release currentRelease = new Release();
            while (i<scenarioLines.size()) {
                String[] scenarioParts = scenarioLines.get(i).split(",");
                int scenarioId = Integer.parseInt(scenarioParts[0]);
                int releaseNumber = Integer.parseInt(scenarioParts[2]);
                String scenarioDescription = scenarioParts[1];
                Class<BasicScenario> scenarioClass = getScenarioClassByName(scenarioParts[3]);

                if (currentReleaseNumber != releaseNumber) {
                    releases.add(currentRelease);
                    currentReleaseNumber = releaseNumber;
                    currentRelease = new Release();
                }

                currentRelease.addScenario(scenario(scenarioClass, scenarioId, scenarioDescription));
                i++;
            }
            releases.add(currentRelease);
        } catch (IOException e) {
            e.printStackTrace();
        }
        notifyServices();
    }

    private Class<BasicScenario> getScenarioClassByName(String className) {
        Class<?> aClass = null;
        try {
            aClass = this.getClass().getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("Class %s not found in the classpath", className));
        }
        if (aClass != null && BasicScenario.class.isAssignableFrom(aClass)) {
            return (Class<BasicScenario>)aClass;
        } else {
            throw new IllegalArgumentException("This is not scenario class: " + aClass.getName());
        }
    }

    private BasicScenario scenario(Class<BasicScenario> scenarioClass, int id, String description) {
        return constructor().withParameterTypes(int.class, String.class, BugsQueue.class)
                             .in(scenarioClass)
                             .newInstance(id, description, bugsQueue);
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

    public String getMajorInfo() {
        return String.valueOf(currentReleaseIndex);
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
}
