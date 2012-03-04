package org.automation.dojo;

import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author serhiy.zelenin
 */
@RunWith(MockitoJUnitRunner.class)
public class DojoScoreServiceTest {
    private static final String CLIENT_ADDRESS = "10.10.1.1";
    @Captor ArgumentCaptor<PlayerRecord> recordCaptor;
    @Mock LogService logService;
    @Mock ReleaseEngine releaseEngine;

    private DojoScoreService scoreService;


    @Before
    public void setUp() throws Exception {
        scoreService = new DojoScoreService(logService, releaseEngine);
    }

    @Test
    public void shouldReturnFailureWhenReportFailure() {
        BasicScenario scenario = setupScenario(1, true);
        setupGameLogs(scenario, gameLog(scenario));

        assertFalse(reportScenario(1, false));
    }

    @Test
    public void shouldReturnPassedWhenReportPassed() {
        BasicScenario scenario = setupScenario(1, false);
        setupGameLogs(scenario, gameLog(scenario));

        assertTrue(reportScenario(1, true));
    }

    @Test
    public void shouldAddScoreWhenBugFound() {
        BasicScenario scenario = setupScenario(1, 88);
        setupGameLogs(scenario, gameLog(scenario));

        reportScenario(1, false);

        PlayerRecord record = captureLogRecord();

        assertEquals(88, record.getScore());
        assertEquals(1, record.getScenario().getId());
        assertFalse(record.isPassed());
    }

    @Test
    public void shouldAddHalfScoreWhenFoundAgainAfterPassed(){
        BasicScenario scenario = setupScenario(1, 100);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario, false, 100)),
                gameLog(scenario, record(scenario, true, 0)),
                gameLog(scenario)); //new minor release record, no reports yet

        reportScenario(1, false);

        PlayerRecord capturedRecord = captureLogRecord();
        
        assertEquals(100 / 2, capturedRecord.getScore());
    }

    @Test
    public void shouldAddHalfScoreWhenFoundAgainAfterPassed_SkipZeros(){
        BasicScenario scenario = setupScenario(1, 100);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario, false, 100), record(scenario, false, 0)),
                gameLog(scenario, record(scenario, true, 0)),
                gameLog(scenario)); //new minor release record, no reports yet

        reportScenario(1, false);

        PlayerRecord capturedRecord = captureLogRecord();

        assertEquals(100 / 2, capturedRecord.getScore());
    }

    @Test
    public void shouldNotAddScoreWhenReportedBugAgainForSameMinorRelease(){
        BasicScenario scenario = setupScenario(1, 100);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario, false, 100)));

        reportScenario(1, false);

        assertEquals(0, captureLogRecord().getScore());
    }

    @Test
    public void shouldDecreaseScoreWhenBugNotReported() {
        BasicScenario scenario = setupScenario(1, 100);
        setupGameLogs(scenario, gameLog(scenario));

        setupClientAddressesDb();

        scoreService.nextRelease(new Release(scenario));

        PlayerRecord record = captureLogRecord();
        assertEquals(-100, record.getScore());
        assertTrue(record.isPassed());
    }

    private void setupClientAddressesDb() {
        when(logService.getUniqueClientAddresses()).thenReturn(Arrays.asList(CLIENT_ADDRESS));
    }

    @Test
    public void shouldDecreaseScoreWhenBugNotFound() {
        BasicScenario scenario = setupScenario(1, 50);
        setupGameLogs(scenario, gameLog(scenario, record(scenario, true, 0)));
        setupClientAddressesDb();

        scoreService.nextRelease(new Release(scenario));

        assertEquals(-50, captureLogRecord().getScore());
    }

    @Test
    public void shouldNotDecreaseScoreWhenNoBugsInPreviousRelease() {
        BasicScenario scenario = createScneario(1, 0);
        setupGameLogs(scenario, gameLog(scenario));
        setupClientAddressesDb();

        scoreService.nextRelease(new Release(scenario));

        verify(logService, never()).playerLog(Matchers.<PlayerRecord>anyObject());
    }

    @Test
    public void shouldNotDecreaseScoreWhenBugReportedOnPreviousMinorRelease() {
        BasicScenario scenario = createScneario(1, 100);
        setupGameLogs(scenario, gameLog(scenario, record(scenario, false, 100)));
        setupClientAddressesDb();

        scoreService.nextRelease(new Release(scenario));

        verify(logService, never()).playerLog(Matchers.<PlayerRecord>anyObject());
    }

    @Test
    public void shouldDecreaseScoreWhenLiar() {
        BasicScenario scenario = setupScenario(1, 0);
        setupGameLogs(scenario,
                gameLog(scenario, record(createScneario(1, 120), false, 120)),
                gameLog(scenario));

        reportScenario(1, false);

        PlayerRecord record = captureLogRecord();
        assertEquals(-120*2, record.getScore());
        assertFalse(record.isPassed());
    }

    @Test
    public void shouldDecreaseScoreWhenInvertedLiar() {
        BasicScenario scenario = setupScenario(1, 100);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario, false, 100)));

        reportScenario(1, true);

        PlayerRecord record = captureLogRecord();
        assertEquals(-100*2, record.getScore());
        assertTrue(record.isPassed());
        assertEquals(PlayerRecord.Type.LIAR, record.getType());
    }

    @Test
    public void shouldNotDecreaseScoreWhenLiarReportedForSameRelease() {
        BasicScenario scenario = setupScenario(1, 100);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario, false, 100)),
                gameLog(scenario, record(scenario, true, -200, PlayerRecord.Type.LIAR)));

        reportScenario(1, true);

        PlayerRecord record = captureLogRecord();
        assertEquals(0, record.getScore());
        assertTrue(record.isPassed());
        assertEquals(PlayerRecord.Type.LIAR, record.getType());
    }

    @Test
    public void shouldNotDecreaseScoreWhenInvertedLiarReportedForSameRelease() {
        BasicScenario scenario = setupScenario(1, 0);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario, false, 100)),
                gameLog(scenario, record(scenario, false, -200, PlayerRecord.Type.LIAR)));

        reportScenario(1, false);

        PlayerRecord record = captureLogRecord();
        assertEquals(0, record.getScore());
        assertEquals(PlayerRecord.Type.LIAR, record.getType());
    }

    @Test
    public void shouldNotDecreaseScoreWhenResultMismatch() {
        BasicScenario scenario = setupScenario(1, 100);
        setupGameLogs(scenario,
                gameLog(scenario));

        reportScenario(1, true);

        PlayerRecord record = captureLogRecord();
        assertEquals(0, record.getScore());
        assertEquals(PlayerRecord.Type.LIAR, record.getType());
    }

    @Test
    public void shouldNotDecreaseScoreWhenResultMismatch_Inverted() {
        BasicScenario scenario = setupScenario(1, 0);
        setupGameLogs(scenario,
                gameLog(scenario));

        reportScenario(1, false);

        PlayerRecord record = captureLogRecord();
        assertEquals(0, record.getScore());
        assertEquals(PlayerRecord.Type.LIAR, record.getType());
    }


    private void setupGameLogs(BasicScenario scenario, GameLog... gameLogs) {
        when(logService.getGameLogs(CLIENT_ADDRESS, scenario))
                .thenReturn(Arrays.asList(gameLogs));
    }

    private PlayerRecord record(BasicScenario scenario, boolean passed, int score) {
        return record(scenario, passed, score, PlayerRecord.Type.VALID_BUG);
    }

    private PlayerRecord record(BasicScenario scenario, boolean passed, int score, PlayerRecord.Type type) {
        return new PlayerRecord("", CLIENT_ADDRESS, scenario, passed, score, "", type);
    }

    private GameLog gameLog(BasicScenario scenario,PlayerRecord ... records) {
        return new GameLog(scenario, records);
    }

    private PlayerRecord captureLogRecord() {
        verify(logService, atLeastOnce()).playerLog(recordCaptor.capture());
        return recordCaptor.getValue();
    }

    private BasicScenario setupScenario(int scenarioId, int bugWeight) {
        BasicScenario scenario = createScneario(1, bugWeight);
        when(releaseEngine.getScenario(scenarioId)).thenReturn(scenario);
        return scenario;
    }

    private BasicScenario setupScenario(int scenarioId, boolean hasBug) {
        return setupScenario(scenarioId, hasBug ? 100 : 0);
    }

    private boolean reportScenario(int scenarioId, boolean testPassed) {
        return scoreService.testResult("vasya", CLIENT_ADDRESS, scenarioId, testPassed);
    }


    private BasicScenario createScneario(int scenarioId, int bugWeight) {
        BasicScenario scenario = new MockScenario(scenarioId, "");
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
