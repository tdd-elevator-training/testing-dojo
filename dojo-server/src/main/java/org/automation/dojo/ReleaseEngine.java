package org.automation.dojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author serhiy.zelenin
 */
public class ReleaseEngine {
    private List<Release> releases = new ArrayList<Release>();
    private int currentReleaseIndex;

    public ReleaseEngine() {
        releases.add(new Release(new Scenario(1)));
        releases.add(new Release(new Scenario(1), new Scenario(2), new Scenario(3)));
        releases.add(new Release(new Scenario(4)));
    }

    public List<Scenario> getCurrentScenarios() {
        return releases.get(currentReleaseIndex).getScenarios();
    }

    public void nextMajorRelease() {
        currentReleaseIndex++;
    }

    public void nextMinorRelease() {

    }
}
