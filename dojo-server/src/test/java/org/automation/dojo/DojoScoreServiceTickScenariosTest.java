package org.automation.dojo;

import org.automation.dojo.web.scenario.Release;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
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


    private void verifyNoPlayerLogs() {
        verify(logService, never()).playerLog(Matchers.<PlayerRecord>anyObject());
    }

    private void assertCapturedTimeoutRecord(PlayerRecord record, int expectedScore, String expectedPlayer,
            MockScenario expectedScenario) {
        assertEquals(expectedScore, record.getScore());
        assertEquals(expectedPlayer, record.getPlayerName());
        assertEquals(expectedScenario, record.getScenario());
        assertEquals(PlayerRecord.Type.TIMEOUT, record.getType());
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
