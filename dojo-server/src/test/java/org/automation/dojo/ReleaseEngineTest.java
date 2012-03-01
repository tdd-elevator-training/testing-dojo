package org.automation.dojo;

import org.fest.assertions.ListAssert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author serhiy.zelenin
 */
public class ReleaseEngineTest {

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

    private ListAssert assertCurrentScenarios(Integer... scenarioId) {
        return assertThat(engine.getCurrentScenarios()).onProperty("id").contains(scenarioId);
    }
}
