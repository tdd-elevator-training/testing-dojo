package org.automation.dojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author serhiy.zelenin
 */
public class Release {
    private List<Scenario> scenarios = new ArrayList<Scenario>();

    public Release(Scenario ... scenario) {
        scenarios.addAll(Arrays.asList(scenario));
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }
}
