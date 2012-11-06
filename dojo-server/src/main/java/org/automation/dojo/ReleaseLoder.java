package org.automation.dojo;

import org.apache.commons.io.IOUtils;
import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.fest.reflect.core.Reflection.constructor;

public class ReleaseLoder {

    private Resource scenarioResource;
    private BugsQueue bugsQueue;

    public ReleaseLoder(Resource scenarioResource) {
        this.scenarioResource = scenarioResource;
    }

    public List<? extends Release> getReleases(BugsQueue bugsQueue) {
        this.bugsQueue = bugsQueue;

        List<Release> result = new LinkedList<Release>();
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
                    result.add(currentRelease);
                    currentReleaseNumber = releaseNumber;
                    currentRelease = new Release();
                }

                currentRelease.addScenario(scenario(scenarioClass, scenarioId, scenarioDescription));
                i++;
            }
            result.add(currentRelease);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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
}
