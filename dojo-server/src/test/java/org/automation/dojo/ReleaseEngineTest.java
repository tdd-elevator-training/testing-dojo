package org.automation.dojo;

import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.scenario.Scenario;
import org.fest.assertions.ListAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.core.io.ByteArrayResource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author serhiy.zelenin
 */
@RunWith(MockitoJUnitRunner.class)
public class ReleaseEngineTest {
    @Mock BugsQueue bugsQueue;
    @Mock
    ScoreService scoreService;
    @Mock LogService logService;
    
    @Captor ArgumentCaptor<Release> logServiceReleaseCaptor;
    @Captor ArgumentCaptor<Release> scoreServiceReleaseCaptor;

    
    private ReleaseEngine engine;

    @Before
    public void setUp() throws Exception {
        engine = new ReleaseEngine(bugsQueue, scoreService, logService);
    }

    @Test
    public void shouldHaveScenarioWhenGameStarted() {
        setScenarioDefinitions("1,scenario1,1,org.automation.dojo.MockScenario");

        engine.init();

        assertCurrentScenarios(1);
        assertCurrentScenarios("scenario1");
    }

    @Test
    public void shouldActivateScenarioWhenMajorRelease() {
        setScenarioDefinitions("" +
                "1,scenario1,1,org.automation.dojo.MockScenario\n" +
                "1,scenario1,2,org.automation.dojo.MockScenario\n" +
                "2,scenario2,2,org.automation.dojo.MockScenario\n" +
                "3,scenario3,3,org.automation.dojo.MockScenario\n");

        engine.init();

        engine.nextMajorRelease();

        assertCurrentScenarios(1, 2);

        engine.nextMajorRelease();
        assertCurrentScenarios(3);
    }

    @Test
    public void shouldGenerateBugsWhenNewMinorRelease() {
        setScenarioDefinitions("1,scenario1,1,org.automation.dojo.MockScenario");
        engine.init();

        putNextBugForScenario(engine.getCurrentScenarios().get(0), 123);

        engine.nextMinorRelease();
        
        Scenario scenario = engine.getCurrentScenarios().get(0);
        assertEquals(123, scenario.getBug().getId());
    }

    @Test
    public void shouldGenerateBugsWhenNewMinorReleaseForSeveralScenarios() {
        setScenarioDefinitions(
                "1,scenario1,1,org.automation.dojo.MockScenario\n" +
                "2,scenario2,1,org.automation.dojo.MockScenario");
        engine.init();
        putNextBugForScenario(engine.getScenario(1), 123);
        putNextBugForScenario(engine.getScenario(2), 345);

        engine.nextMinorRelease();

        assertEquals(123, engine.getScenario(1).getBug().getId());
        assertEquals(345, engine.getScenario(2).getBug().getId());
    }

    @Test
    public void shouldBeNoBugsWhenNoBugs() {
        setScenarioDefinitions("1,scenario1,1,org.automation.dojo.MockScenario");
        engine.init();
        putNextBugForScenario(engine.getScenario(1), null);

        engine.nextMinorRelease();

        assertSame(Bug.NULL_BUG, engine.getScenario(1).getBug());
    }

    @Test
    public void shouldNotifyServicesWhenInitThenMinorReleaseThenMajorRelease(){
        setScenarioDefinitions("" +
                "1,scenario1,1,org.automation.dojo.MockScenario\n" +
                "2,scenario1,1,org.automation.dojo.MockScenario\n" +
                "1,scenario1,2,org.automation.dojo.MockScenario\n");
        engine.init();

        assertServicesNotified(engine.getCurrentRelease(), 1);

        engine.nextMinorRelease();

        assertServicesNotified(engine.getCurrentRelease(), 2);

        Release release = engine.getCurrentRelease();
        engine.nextMajorRelease();
        assertServicesNotified(release, 3);
    }

    private void assertServicesNotified(Release release, int wantedNumberOfInvocations) {
        verify(scoreService, times(wantedNumberOfInvocations)).nextRelease(scoreServiceReleaseCaptor.capture());
        verify(logService, times(wantedNumberOfInvocations)).createGameLog(logServiceReleaseCaptor.capture());
        assertSame(release, scoreServiceReleaseCaptor.getValue());
        assertSame(release, logServiceReleaseCaptor.getValue());
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
