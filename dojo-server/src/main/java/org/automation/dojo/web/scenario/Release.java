package org.automation.dojo.web.scenario;

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

    /**
     * Тут внимательно! Сценарием может быть и так назфываемый терминатор, который ничего не делает
     * а только возвращает jsp. Есть метод onlyScenarios() который возвращает список сценариев без
     * терминаторов.
     */
    private List<BasicScenario> scenariosAndTerminators = new ArrayList<BasicScenario>();


    @Override
    public boolean activate(T request) {
        return true;
    }

    public Release(BasicScenario ... scenario) {
        scenariosAndTerminators.addAll(Arrays.asList(scenario));
    }

    public List<BasicScenario> getScenarios() {
        return onlyScenarios();
    }

    private List<BasicScenario> onlyScenarios() {
        List<BasicScenario> result = new LinkedList<BasicScenario>();
        for (BasicScenario scenario : scenariosAndTerminators) {
            if (!scenario.isTerminator()) {
                result.add(scenario);
            }
        }
        return result;
    }

    public void addScenario(BasicScenario scenario) {
        scenariosAndTerminators.add(scenario);
    }

    @Override
    public void takeNextBug() {
        for (Scenario scenario : onlyScenarios()) {
            scenario.takeNextBug();
        }
    }

    public String process(T request) {
        String result = null;
        for (Scenario scenario : scenariosAndTerminators) {
            if (scenario.activate(request)) {
                result = scenario.process(request);
            }
        }
        return result;
    }

    @Override
    public void setNoBug() {
        for (Scenario scenario : onlyScenarios()) {
            scenario.setNoBug();
        }
    }

    @Override
    public String toString() {
        return onlyScenarios().toString();
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
