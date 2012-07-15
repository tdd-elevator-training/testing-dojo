package org.automation.dojo;

import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author serhiy.zelenin
 */
@RunWith(MockitoJUnitRunner.class)
public class DojoScoreServiceTickScenariosTest extends DojoScoreBaseTest {

    private static final int PENALTY_TIMEOUT = 50;
    private static final int PENALTY_VALUE = 10;

    @Before
    public void setUp2() {
        setupPenalty(PENALTY_TIMEOUT, PENALTY_VALUE);
    }

    @Test
    public void shouldDecreaseScoreWhenNoReportsForRelease(){
        setupRegisteredPlayers("vasya");
        setupLastReleaseLog(100, scenario(1));

        scoreService.tick(100 + PENALTY_TIMEOUT);

        assertCapturedTimeoutRecord(captureLastLogRecord(), -PENALTY_VALUE, "vasya", scenario(1));
    }

    @Test
    public void shouldNotDecreaseScoreWhenNoTimeoutAfterRelease(){
        setupRegisteredPlayers("petya");
        MockScenario scenario = scenario(1);
        setupLastReleaseLog(100, scenario);

        scoreService.tick(100 + PENALTY_TIMEOUT - 1);

        verifyNoPlayerLogs();
    }

    @Test
    public void shouldNotDecreaseScoreWhenReportedOnTime(){
        setupRegisteredPlayers("petya");
        MockScenario scenario = scenario(1);
        setupLastReleaseLog(100, scenario).putRecord(record("petya", scenario, 100 + PENALTY_TIMEOUT - 1));

        scoreService.tick(100 + PENALTY_TIMEOUT);

        verifyNoPlayerLogs();
    }


    @Test
    public void shouldDecreaseWhenReportedForScenarioEarlier(){
        setupRegisteredPlayers("petya");
        MockScenario scenario = scenario(1);
        setupLastReleaseLog(100, scenario).putRecord(record("petya", scenario, 100 + 1));

        scoreService.tick(100 + 2 * PENALTY_TIMEOUT);

        verify(logService, times(1)).playerLog(Matchers.<PlayerRecord>anyObject());
        assertCapturedTimeoutRecord(captureLastLogRecord(), -PENALTY_VALUE, "petya", scenario);
    }

    @Test
    public void shouldDecreaseScoreProgressivelyWhenReportedWithMultiplierDelay(){
        setupRegisteredPlayers("petya");
        MockScenario scenario = scenario(1);
        setupLastReleaseLog(100, scenario)
                .putRecord(record("petya", scenario, 100 + PENALTY_TIMEOUT, PlayerRecord.Type.TIMEOUT));

        scoreService.tick(100 + 2 * PENALTY_TIMEOUT);

        assertCapturedTimeoutRecord(captureLastLogRecord(), -2 * PENALTY_VALUE, "petya", scenario);
    }

    @Test
    public void shouldReportBugForSuperman(){
        MockScenario scenario = setupScenario(1, true);
        Release release = setupReleaseWithGameLogs(ScoreService.SUPERMAN, scenario);

        scoreService.reportSuperUsers(release);

        assertCapturedRecord(captureLastLogRecord(), scenario.getBug().getWeight(), ScoreService.SUPERMAN, scenario, PlayerRecord.Type.VALID_BUG);
    }

    @Test
    public void shouldReportPassedScenarioForSupermanWhenNoBugs(){
        MockScenario scenario = setupScenario(1, false);
        Release release = setupReleaseWithGameLogs(ScoreService.SUPERMAN, scenario);

        scoreService.reportSuperUsers(release);

        assertCapturedRecord(captureLastLogRecord(), 0, ScoreService.SUPERMAN, scenario, PlayerRecord.Type.PASSED);
    }

    @Test
    public void shouldIterateScenariosForSupermanWhenReportSuperUsers(){
        MockScenario scenario1 = setupScenario(1, false);
        MockScenario scenario2 = setupScenario(2, true);
        Release release = setupReleaseWithGameLogs(ScoreService.SUPERMAN, scenario1, scenario2);

        scoreService.reportSuperUsers(release);

        verify(logService, atLeastOnce()).playerLog(recordCaptor.capture());
        List<PlayerRecord> capturedRecords = recordCaptor.getAllValues();
        assertCapturedRecord(capturedRecords.get(0), 0, ScoreService.SUPERMAN, scenario1, PlayerRecord.Type.PASSED);
        assertCapturedRecord(capturedRecords.get(1), 100, ScoreService.SUPERMAN, scenario2, PlayerRecord.Type.VALID_BUG);
    }

    @Test
    public void shouldIterateScenariosForSuperlooserWhenReportSuperUsers(){
        MockScenario scenario1 = setupScenario(1, false);
        MockScenario scenario2 = setupScenario(2, true);
        Release release = setupReleaseWithGameLogs(ScoreService.LOOSER, scenario1, scenario2);

        scoreService.reportSuperUsers(release);

        verify(logService, atLeastOnce()).playerLog(recordCaptor.capture());
        List<PlayerRecord> capturedRecords = recordCaptor.getAllValues();
        assertCapturedRecord(capturedRecords.get(0), -20, ScoreService.LOOSER, scenario1, PlayerRecord.Type.LIAR);
        assertCapturedRecord(capturedRecords.get(1), -20, ScoreService.LOOSER, scenario2, PlayerRecord.Type.LIAR);
    }

    @Test
    public void shouldNotDecreaseScoreWhenSuperman(){
        setupRegisteredPlayers(ScoreService.SUPERMAN);
        setupLastReleaseLog(100, scenario(1));

        scoreService.tick(100 + PENALTY_TIMEOUT);

        verifyNoPlayerLogs();
    }

    private Release setupReleaseWithGameLogs(String playerName, MockScenario... scenario) {
        setupRegisteredPlayers(playerName);
        setupGameLogs(playerName, scenario);
        return setupLastReleaseLog(123, scenario).getRelease();
    }

    private void verifyNoPlayerLogs() {
        verify(logService, never()).playerLog(Matchers.<PlayerRecord>anyObject());
    }

    private void assertCapturedTimeoutRecord(PlayerRecord record, int expectedScore, String expectedPlayer,
            BasicScenario expectedScenario) {
        assertCapturedRecord(record, expectedScore, expectedPlayer, expectedScenario, PlayerRecord.Type.TIMEOUT);
    }

    private void assertCapturedRecord(PlayerRecord record, int expectedScore, String expectedPlayer, BasicScenario expectedScenario, PlayerRecord.Type type) {
        assertEquals(expectedScore, record.getScore());
        assertEquals(expectedPlayer, record.getPlayerName());
        assertEquals(expectedScenario, record.getScenario());
        assertEquals(type, record.getType());
    }

    private ReleaseLog setupLastReleaseLog(int releaseTime, MockScenario... scenario) {
        ReleaseLog releaseLog = new ReleaseLog(new Release(scenario), new Date(releaseTime));
        when(logService.getCurrentReleaseLog()).thenReturn(releaseLog);
        return releaseLog;
    }

    private MockScenario scenario(int id) {
        return new MockScenario(id, "", null);
    }

    private void setupPenalty(int penaltyTimeOut, int penaltyValue) {
        configurationService.setPenaltyTimeOut(penaltyTimeOut);
        configurationService.setPenaltyValue(penaltyValue);
    }



}
