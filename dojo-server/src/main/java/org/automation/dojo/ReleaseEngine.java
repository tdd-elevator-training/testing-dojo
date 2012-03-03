package org.automation.dojo;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author serhiy.zelenin
 */
public class ReleaseEngine {
    private List<Release> releases = new ArrayList<Release>();
    private int currentReleaseIndex;

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
                if (currentReleaseNumber != releaseNumber) {
                    releases.add(currentRelease);
                    currentReleaseNumber = releaseNumber;
                    currentRelease = new Release();
                }
                currentRelease.addScenario(scenario(scenarioId, scenarioParts[1]));
                i++;
            }
            releases.add(currentRelease);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Scenario scenario(int id, String description) {
        return new Scenario(id, description, bugsQueue);
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
}
