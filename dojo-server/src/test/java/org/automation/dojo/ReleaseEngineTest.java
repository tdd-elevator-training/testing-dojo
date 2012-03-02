package org.automation.dojo;

import org.fest.assertions.ListAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.core.io.ByteArrayResource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;
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
        setScenarioDefinitions("1,scenario1,1");

        engine.init();

        assertCurrentScenarios(1);
        assertCurrentScenarios("scenario1");
    }

    @Test
    public void shouldActivateScenarioWhenMajorRelease() {
        setScenarioDefinitions("" +
                "1,scenario1,1\n" +
                "1,scenario1,2\n" +
                "2,scenario2,2\n" +
                "3,scenario3,3\n");

        engine.init();

        engine.nextMajorRelease();

        assertCurrentScenarios(1, 2);

        engine.nextMajorRelease();
        assertCurrentScenarios(3);
    }

    @Test
    public void shouldGenerateBugsWhenNewMinorRelease() {
        setScenarioDefinitions("1,scenario1,1");
        engine.init();

        putNextBugForScenario(engine.getCurrentScenarios().get(0), 123);

        engine.nextMinorRelease();
        
        Scenario scenario = engine.getCurrentScenarios().get(0);
        assertEquals(123, scenario.getBug().getId());
    }

    @Test
    public void shouldGenerateBugsWhenNewMinorReleaseForSeveralScenarios() {
        setScenarioDefinitions("1,scenario1,1\n2,scenario2,1");
        engine.init();
        putNextBugForScenario(engine.getScenario(0), 123);
        putNextBugForScenario(engine.getScenario(1), 345);

        engine.nextMinorRelease();

        assertEquals(123, engine.getScenario(0).getBug().getId());
        assertEquals(345, engine.getScenario(1).getBug().getId());
    }

    @Test
    public void shouldBeNoBugsWhenNoBugs() {
        setScenarioDefinitions("1,scenario1,1");
        engine.init();
        putNextBugForScenario(engine.getScenario(0), null);

        engine.nextMinorRelease();

        assertSame(Bug.NULL_BUG, engine.getScenario(0).getBug());
    }

    private void setScenarioDefinitions(String scenarioDefinition) {
        engine.setScenarioResource(new ByteArrayResource(scenarioDefinition.getBytes()));
    }

    private OngoingStubbing<Bug> putNextBugForScenario(Scenario expectedScenario, Integer bugId) {
        return when(bugsQueue.nextBugFor(eq(expectedScenario))).thenReturn(bugId == null ? Bug.NULL_BUG : new Bug(bugId));
    }

    private ListAssert assertCurrentScenarios(Integer... scenarioId) {
        return assertThat(engine.getCurrentScenarios()).onProperty("id").contains(scenarioId);
    }

    private ListAssert assertCurrentScenarios(String... descriptions) {
        return assertThat(engine.getCurrentScenarios()).onProperty("description").contains(descriptions);
    }
}
