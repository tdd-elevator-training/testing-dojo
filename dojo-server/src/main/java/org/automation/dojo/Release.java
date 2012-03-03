package org.automation.dojo;

import org.automation.dojo.web.scenario.Scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author serhiy.zelenin
 */
public class Release<T> {
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

    public String process(T request) {
        String result = null;
        for (Scenario scenario : scenarios) {
            result = scenario.process(request);
        }
        return result;
    }
}
