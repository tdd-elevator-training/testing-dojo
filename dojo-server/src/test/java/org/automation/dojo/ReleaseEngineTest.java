package org.automation.dojo;

import org.fest.assertions.ListAssert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author serhiy.zelenin
 */
@RunWith(MockitoJUnitRunner.class)
public class ReleaseEngineTest {
    @Mock BugsQueue bugsQueue;

    private ReleaseEngine engine;

    @Before
    public void setUp() throws Exception {
        engine = new ReleaseEngine();
    }

    @Test
    public void shouldHaveScenarioWhenGameStarted() {
        engine = new ReleaseEngine();

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
    @Ignore //TODO: IMPLEMENT!
    public void shouldGenerateBugsWhenNewMinorRelease() {
        engine.nextMinorRelease();

        Scenario scenario = engine.getCurrentScenarios().get(0);
        assertNotNull(scenario.getBug());
    }

    private ListAssert assertCurrentScenarios(Integer... scenarioId) {
        return assertThat(engine.getCurrentScenarios()).onProperty("id").contains(scenarioId);
    }
}
