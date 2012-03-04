package org.automation.dojo;

import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;
import org.fest.assertions.ListAssert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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
    @Mock
    ScoreService scoreService;
    @Mock LogService logService;
    
    @Captor ArgumentCaptor<Release> logServiceReleaseCaptor;
    @Captor ArgumentCaptor<Release> scoreServiceReleaseCaptor;

    
    private ReleaseEngine engine;
    private Release capturedScoreServiceRelease;
    private Release capturedLogServiceRelease;

    @Before
    public void setUp() throws Exception {
        engine = new ReleaseEngine(scoreService, logService);
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
        
        BasicScenario scenario = engine.getCurrentScenarios().get(0);
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
    @Ignore //TODO: kill or leave after serialization
    public void shouldNotifyServicesWhenInitThenMinorReleaseThenMajorRelease(){
        setScenarioDefinitions("" +
                "1,scenario1,1,org.automation.dojo.MockScenario\n" +
                "2,scenario1,1,org.automation.dojo.MockScenario\n" +
                "1,scenario1,2,org.automation.dojo.MockScenario\n");
        engine.init();

        Release release = engine.getCurrentRelease();
        captureReleaseEvents(1);
        assertEquals(release, capturedLogServiceRelease);
        assertEquals(release, capturedLogServiceRelease);

        engine.nextMinorRelease();

        captureReleaseEvents(2);
        assertEquals(Bug.NULL_BUG, capturedScoreServiceRelease.getScenario(1).getBug());
        assertEquals(1, capturedLogServiceRelease.getScenario(1).getBug().getId());

/*

        Release release1 = engine.getCurrentRelease();
        int wantedNumberOfInvocations = 2;
        assertSame(release1, scoreServiceReleaseCaptor.getValue());
        assertSame(release1, logServiceReleaseCaptor.getValue());

        Release release = engine.getCurrentRelease();
        engine.nextMajorRelease();
        assertServicesNotified(release, 3);
*/
    }

    private void captureReleaseEvents(int wantedNumberOfInvocations) {
        verify(scoreService, times(wantedNumberOfInvocations)).nextRelease(scoreServiceReleaseCaptor.capture());
        verify(logService, times(wantedNumberOfInvocations)).createGameLog(logServiceReleaseCaptor.capture());
        capturedLogServiceRelease = logServiceReleaseCaptor.getValue();
        capturedScoreServiceRelease = scoreServiceReleaseCaptor.getValue();
    }

    private void assertServicesNotified(Release release, int wantedNumberOfInvocations) {
        captureReleaseEvents(wantedNumberOfInvocations);
        assertSame(release, scoreServiceReleaseCaptor.getValue());
        assertSame(release, logServiceReleaseCaptor.getValue());
    }

    private void setScenarioDefinitions(String scenarioDefinition) {
        engine.setScenarioResource(new ByteArrayResource(scenarioDefinition.getBytes()));
    }

    private void putNextBugForScenario(BasicScenario expectedScenario, Integer bugId) {
        ((MockScenario) expectedScenario).setNextBug(bugId == null ? Bug.NULL_BUG : new Bug(bugId));
    }

    private ListAssert assertCurrentScenarios(Integer... scenarioId) {
        return assertThat(engine.getCurrentScenarios()).onProperty("id").contains(scenarioId);
    }

    private ListAssert assertCurrentScenarios(String... descriptions) {
        return assertThat(engine.getCurrentScenarios()).onProperty("description").contains(descriptions);
    }
}
