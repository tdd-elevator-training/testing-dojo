package org.automation.dojo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author serhiy.zelenin
 */
@RunWith(MockitoJUnitRunner.class)
public class DojoReportServiceTest {
    private static final String CLIENT_ADDRESS = "10.10.1.1";
    @Captor ArgumentCaptor<PlayerRecord> recordCaptor;
    @Mock LogService logService;
    @Mock ReleaseEngine releaseEngine;

    private DojoReportService reportService;


    @Before
    public void setUp() throws Exception {
        reportService = new DojoReportService(logService, releaseEngine);
    }

    @Test
    public void shouldReturnFailureWhenReportFailure() {
        Scenario scenario = setupScenario(1, true);
        setupGameLogs(scenario, gameLog(scenario));

        assertFalse(reportScenario(1, false));
    }

    @Test
    public void shouldReturnPassedWhenReportPassed() {
        Scenario scenario = setupScenario(1, false);
        setupGameLogs(scenario, gameLog(scenario));

        assertTrue(reportScenario(1, true));
    }

    @Test
    public void shouldAddScoreWhenBugFound() {
        Scenario scenario = setupScenario(1, 88);
        setupGameLogs(scenario, gameLog(scenario));

        reportScenario(1, false);

        PlayerRecord record = captureLogRecord();

        assertEquals(88, record.getScore());
        assertEquals(1, record.getScenario().getId());
        assertFalse(record.isPassed());
    }

    @Test
    public void shouldAddHalfScoreWhenFoundAgainAfterPassed(){
        Scenario scenario = setupScenario(1, 100);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario, false, 100)),
                gameLog(scenario, record(scenario, true, 0)),
                gameLog(scenario)); //new minor release record, no reports yet

        reportScenario(1, false);

        PlayerRecord capturedRecord = captureLogRecord();
        
        assertEquals(100 / 2, capturedRecord.getScore());
    }

    @Test
    public void shouldNotAddScoreWhenReportedBugAgainForSameMinorRelease(){
        Scenario scenario = setupScenario(1, 100);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario, false, 100)));

        reportScenario(1, false);

        PlayerRecord capturedRecord = captureLogRecord();

        assertEquals(0, capturedRecord.getScore());
    }

    private void setupGameLogs(Scenario scenario, GameLog... gameLogs) {
        when(logService.getGameLogs(CLIENT_ADDRESS, scenario))
                .thenReturn(Arrays.asList(gameLogs));
    }

    private PlayerRecord record(Scenario scenario, boolean passed, int score) {
        return new PlayerRecord("", CLIENT_ADDRESS, scenario, passed, score);
    }

    private GameLog gameLog(Scenario scenario,PlayerRecord ... records) {
        return new GameLog(scenario, records);
    }

    private PlayerRecord captureLogRecord() {
        verify(logService, atLeastOnce()).playerLog(recordCaptor.capture());
        return recordCaptor.getValue();
    }

    private Scenario setupScenario(int scenarioId, int bugWeight) {
        Scenario scenario = createScneario(1, bugWeight);
        when(releaseEngine.getScenario(scenarioId)).thenReturn(scenario);
        return scenario;
    }

    private Scenario setupScenario(int scenarioId, boolean hasBug) {
        return setupScenario(scenarioId, hasBug ? 100 : 0);
    }

    private boolean reportScenario(int scenarioId, boolean testPassed) {
        return reportService.testResult("vasya", CLIENT_ADDRESS, scenarioId, testPassed);
    }


    private Scenario createScneario(int scenarioId, int bugWeight) {
        Scenario scenario = new MockScenario(scenarioId, "", null);
        if (bugWeight > 0) {
            Bug bug = new Bug(1);
            bug.setWeight(bugWeight);
            scenario.setBug(bug);
        }else{
            scenario.setBug(Bug.NULL_BUG);
        }
        return scenario;
    }
}
