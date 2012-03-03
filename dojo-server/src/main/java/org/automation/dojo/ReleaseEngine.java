package org.automation.dojo;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public ReleaseEngine() {
    }

    public ReleaseEngine(BugsQueue bugsQueue) {
        this.bugsQueue = bugsQueue;
    }

    public ReleaseEngine(Release ... releasesArray) {
        Collections.addAll(releases, releasesArray);
    }

    public List<Scenario> getCurrentScenarios() {
        return releases.get(currentReleaseIndex).getScenarios();
    }

    public void nextMajorRelease() {
        currentReleaseIndex++;
    }

    public void nextMinorRelease() {
        List<Scenario> currentScenarios = getCurrentScenarios();
        for (Scenario currentScenario : currentScenarios) {
            currentScenario.takeNextBug();
        }
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
                Class<Scenario> scenarioClass = getScenarioClassByName(scenarioParts[3]);

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
    }

    private Class<Scenario> getScenarioClassByName(String className) {
        Class<?> aClass = null;
        try {
            aClass = this.getClass().getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("Class %s not found in the classpath", className));
        }
        if (aClass != null && Scenario.class.isAssignableFrom(aClass)) {
            return (Class<Scenario>)aClass;
        } else {
            throw new IllegalArgumentException("This is not scenario class: " + aClass.getName());
        }
    }

    private Scenario scenario(Class<Scenario> scenarioClass, int id, String description) {
        return constructor().withParameterTypes(int.class, String.class, BugsQueue.class)
                             .in(scenarioClass)
                             .newInstance(id, description, bugsQueue);
    }

    public void setScenarioResource(Resource scenarioResource) {
        this.scenarioResource = scenarioResource;
    }

    public void setBugsQueue(BugsQueue bugsQueue) {
        this.bugsQueue = bugsQueue;
    }

    public Scenario getScenario(int scenarioId) {
        List<Scenario> scenarios = getCurrentScenarios();
        for (Scenario scenario : scenarios) {
            if (scenario.getId() == scenarioId) {
                return scenario;
            }
        }
        throw new IllegalArgumentException("No current scenario found with id : " + scenarioId);
    }

    public String getMinorInfo() {
        return getCurrentScenarios().toString();
    }

    public String getMajorInfo() {
        return String.valueOf(currentReleaseIndex);
    }

    public void setMajorRelease(int index) {
        currentReleaseIndex = index;
    }
}
