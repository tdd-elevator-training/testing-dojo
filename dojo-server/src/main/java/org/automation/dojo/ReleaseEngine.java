package org.automation.dojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author serhiy.zelenin
 */
public class ReleaseEngine {
    private List<Release> releases = new ArrayList<Release>();
    private int currentReleaseIndex;

    public ReleaseEngine(BugsQueue bugsQueue) {
        this(new Release(new Scenario(1, bugsQueue)),
                new Release(new Scenario(1, bugsQueue), new Scenario(2, bugsQueue), new Scenario(3, bugsQueue)),
                new Release(new Scenario(4, bugsQueue)));
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
}
