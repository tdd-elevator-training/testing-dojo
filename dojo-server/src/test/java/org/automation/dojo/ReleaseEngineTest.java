package org.automation.dojo;

import org.fest.assertions.ListAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author serhiy.zelenin
 */
@RunWith(MockitoJUnitRunner.class)
public class ReleaseEngineTest {
    @Mock BugsQueue bugsQueue;

    private ReleaseEngine engine;

    @Before
    public void setUp() throws Exception {
        engine = new ReleaseEngine(bugsQueue);
    }

    @Test
    public void shouldHaveScenarioWhenGameStarted() {
        engine = new ReleaseEngine(bugsQueue);

        assertNotNull(engine.getCurrentScenarios());
        assertCurrentScenarios(1);
    }

    @Test
    public void shouldActivateScenarioWhenMajorRelease() {
        engine.nextMajorRelease();

        assertCurrentScenarios(1, 2, 3);

        engine.nextMajorRelease();
        assertCurrentScenarios(4);
    }

    @Test
    public void shouldGenerateBugsWhenNewMinorRelease() {
        putNextBugForScenario(engine.getCurrentScenarios().get(0), 123);

        engine.nextMinorRelease();
        
        Scenario scenario = engine.getCurrentScenarios().get(0);
        assertEquals(123, scenario.getBug().getId());
    }

    @Test
    public void shouldGenerateBugsWhenNewMinorReleaseForSeveralScenarios() {
        Scenario scenario1 = new Scenario(1, bugsQueue);
        Scenario scenario2 = new Scenario(2, bugsQueue);
        engine = new ReleaseEngine(new Release(scenario1, scenario2));
        putNextBugForScenario(scenario1, 123);
        putNextBugForScenario(scenario2, 345);

        engine.nextMinorRelease();

        assertEquals(123, scenario1.getBug().getId());
        assertEquals(345, scenario2.getBug().getId());
    }


    private OngoingStubbing<Bug> putNextBugForScenario(Scenario expectedScenario, int bugId) {
        return when(bugsQueue.nextBugFor(expectedScenario)).thenReturn(new Bug(bugId));
    }

    private ListAssert assertCurrentScenarios(Integer... scenarioId) {
        return assertThat(engine.getCurrentScenarios()).onProperty("id").contains(scenarioId);
    }
}
