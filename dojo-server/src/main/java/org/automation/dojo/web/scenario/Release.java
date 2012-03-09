package org.automation.dojo.web.scenario;

import org.automation.dojo.web.scenario.Scenario;
import org.automation.dojo.web.servlet.RequestWorker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author serhiy.zelenin
 */
public class Release<T> implements Scenario<T>, Serializable {

    private static final long serialVersionUID = -2300292798842489992L;

    private List<BasicScenario> scenarios = new ArrayList<BasicScenario>();

    @Override
    public boolean activate(T request) {
        return true;
    }

    public Release(BasicScenario ... scenario) {
        scenarios.addAll(Arrays.asList(scenario));
    }

    public List<BasicScenario> getScenarios() {
        return scenarios;
    }

    public void addScenario(BasicScenario scenario) {
        scenarios.add(scenario);
    }

    @Override
    public void takeNextBug() {
        for (Scenario scenario : scenarios) {
            scenario.takeNextBug();
        }
    }

    public String process(T request) {
        String result = null;
        for (Scenario scenario : scenarios) {
            if (scenario.activate(request)) {
                result = scenario.process(request);
            }
        }
        return result;
    }

    @Override
    public void setNoBug() {
        for (Scenario scenario : scenarios) {
            scenario.setNoBug();
        }
    }

    @Override
    public String toString() {
        List<BasicScenario> result = new LinkedList<BasicScenario>();
        for (BasicScenario scenario : getScenarios()) {
            if (!scenario.isTerminator()) {
                result.add(scenario);
            }
        }
        return result.toString();
    }

    public BasicScenario getScenario(int scenarioId) {
        List<BasicScenario> scenarios = getScenarios();
        for (BasicScenario scenario : scenarios) {
            if (scenario.getId() == scenarioId) {
                return scenario;
            }
        }
        throw new IllegalArgumentException("No current scenario found with id : " + scenarioId);
    }
}
