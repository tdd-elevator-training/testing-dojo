package org.automation.dojo;

import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author serhiy.zelenin
 */
@RunWith(MockitoJUnitRunner.class)
public class DojoScoreServiceTest extends DojoScoreBaseTest {
    private static final String PLAYER_NAME = "vasyad";


    @Test
    public void shouldAddScoreWhenBugFound() {
        BasicScenario scenario = setupScenario(1, 88);
        setupGameLogs(scenario, gameLog(scenario));

        reportScenarioSuite(1, TestStatus.FAILED);

        PlayerRecord record = captureLastLogRecord();

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

        reportScenarioSuite(1, TestStatus.FAILED);

        PlayerRecord capturedRecord = captureLastLogRecord();
        
        assertEquals(100 / 2, capturedRecord.getScore());
    }

    @Test
    public void shouldAddHalfScoreWhenFoundAgainAfterPassed_SkipZeros(){
        BasicScenario scenario = setupScenario(1, 100);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario, false, 100), record(scenario, false, 0)),
                gameLog(scenario, record(scenario, true, 0)),
                gameLog(scenario)); //new minor release record, no reports yet

        reportScenarioSuite(1, TestStatus.FAILED);

        PlayerRecord capturedRecord = captureLastLogRecord();

        assertEquals(100 / 2, capturedRecord.getScore());
    }

    @Test
    public void shouldNotAddScoreWhenReportedBugAgainForSameMinorRelease(){
        BasicScenario scenario = setupScenario(1, 100);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario, false, 100)));

        reportScenarioSuite(1, TestStatus.FAILED);

        assertEquals(0, captureLastLogRecord().getScore());
    }

    @Test
    public void shouldDecreaseScoreWhenBugNotReported() {
        BasicScenario scenario = setupScenario(1, 100);
        setupGameLogs(scenario, gameLog(scenario));

        setupRegisteredPlayers();

        scoreService.nextRelease(new Release(scenario));

        PlayerRecord record = captureLastLogRecord();
        assertEquals(-100, record.getScore());
        assertTrue(record.isPassed());
    }

    private void setupRegisteredPlayers() {
        setupRegisteredPlayers(PLAYER_NAME);
    }

    @Test
    public void shouldDecreaseScoreWhenBugNotFound() {
        BasicScenario scenario = setupScenario(1, 50);
        setupGameLogs(scenario, gameLog(scenario, record(scenario, true, 0)));
        setupRegisteredPlayers();

        scoreService.nextRelease(new Release(scenario));

        assertEquals(-50, captureLastLogRecord().getScore());
    }

    @Test
    public void shouldNotDecreaseScoreWhenNoBugsInPreviousRelease() {
        BasicScenario scenario = scenario(1, 0);
        setupGameLogs(scenario, gameLog(scenario));
        setupRegisteredPlayers();

        scoreService.nextRelease(new Release(scenario));

        verify(logService, never()).playerLog(Matchers.<PlayerRecord>anyObject());
    }

    @Test
    public void shouldNotDecreaseScoreWhenBugReportedOnPreviousMinorRelease() {
        BasicScenario scenario = scenario(1, 100);
        setupGameLogs(scenario, gameLog(scenario, record(scenario, false, 100)));
        setupRegisteredPlayers();

        scoreService.nextRelease(new Release(scenario));

        verify(logService, never()).playerLog(Matchers.<PlayerRecord>anyObject());
    }

    @Test
    public void shouldDecreaseScoreWhenLiar() {
        configurationService.setLiarWeight(30);
        BasicScenario scenario = setupScenario(1, 0);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario(1, 123), false, 123)),
                gameLog(scenario));

        reportScenarioSuite(1, TestStatus.FAILED);

        //we report a bug for a bug free scenario
        PlayerRecord record = captureLastLogRecord();
        assertEquals(-30, record.getScore());
        assertFalse(record.isPassed());
    }

    @Test
    public void shouldDecreaseScoreWhenInvertedLiar() {
        configurationService.setLiarWeight(30);
        BasicScenario scenario = setupScenario(1, 123);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario, false, 123)));

        reportScenarioSuite(1, TestStatus.PASSED);

        //we report test passed for bugged scenario which failed in the same release
        PlayerRecord record = captureLastLogRecord();
        assertEquals(-30*2, record.getScore());
        assertTrue(record.isPassed());
        assertEquals(PlayerRecord.Type.LIAR, record.getType());
    }

    @Test
    public void shouldNotDecreaseScoreWhenLiarReportedForSameRelease() {
        BasicScenario scenario = setupScenario(1, 100);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario, false, 100)),
                gameLog(scenario, record(scenario, true, -200, PlayerRecord.Type.LIAR)));

        reportScenarioSuite(1, TestStatus.PASSED);

        PlayerRecord record = captureLastLogRecord();
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

        reportScenarioSuite(1, TestStatus.FAILED);

        PlayerRecord record = captureLastLogRecord();
        assertEquals(0, record.getScore());
        assertEquals(PlayerRecord.Type.LIAR, record.getType());
    }

    @Test
    public void shouldDecreaseScoreWhenResultMismatch() {
        BasicScenario scenario = setupScenario(1, 123);
        setupGameLogs(scenario,
                gameLog(scenario));

        reportScenarioSuite(1, TestStatus.PASSED);

        PlayerRecord record = captureLastLogRecord();
        assertEquals(-20, record.getScore());
        assertEquals(PlayerRecord.Type.LIAR, record.getType());
    }

    @Test
    public void shouldNotDecreaseScoreWhenResultMismatch2() {
        BasicScenario scenario = setupScenario(1, 100);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario, true, -100, PlayerRecord.Type.LIAR)));

        reportScenarioSuite(1, TestStatus.PASSED);

        PlayerRecord record = captureLastLogRecord();
        assertEquals(0, record.getScore());
        assertEquals(PlayerRecord.Type.LIAR, record.getType());
    }

    @Test
    public void shouldDecreaseScoreWhenResultMismatch_Inverted() {
        BasicScenario scenario = setupScenario(1, 0);
        setupGameLogs(scenario,
                gameLog(scenario));

        reportScenarioSuite(1, TestStatus.FAILED);

        PlayerRecord record = captureLastLogRecord();
        assertEquals(-20, record.getScore());
        assertEquals(PlayerRecord.Type.LIAR, record.getType());
    }

    @Test
    public void shouldNotDecreaseScoreWhenResultMismatch_Inverted() {
        BasicScenario scenario = setupScenario(1, 0);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario, false, -100, PlayerRecord.Type.LIAR)));

        reportScenarioSuite(1, TestStatus.FAILED);

        PlayerRecord record = captureLastLogRecord();
        assertEquals(0, record.getScore());
        assertEquals(PlayerRecord.Type.LIAR, record.getType());
    }

    @Test
    public void shouldLogZeroWhenReportedNoBugsForBugsFreeScenario() {
        BasicScenario scenario = setupScenario(1, 0);
        setupGameLogs(scenario,
                gameLog(scenario));

        reportScenarioSuite(1, TestStatus.PASSED);

        PlayerRecord record = captureLastLogRecord();
        assertEquals(0, record.getScore());
        assertEquals(PlayerRecord.Type.PASSED, record.getType());
    }

    @Test
    public void shouldAddCompleteScoreWhenFoundNewBugAfterPassed(){
        BasicScenario scenario = setupScenario(1, 100, 2);
        setupGameLogs(scenario,
                gameLog(scenario, record(scenario(1, 100, 1), false, 100)),
                gameLog(scenario)); //new minor release record, no reports yet

        reportScenarioSuite(1, TestStatus.FAILED);

        PlayerRecord capturedRecord = captureLastLogRecord();

        assertEquals(100, capturedRecord.getScore());
    }

    @Test
    public void shouldDecreaseWhenTestException() {
        configurationService.setExceptionWeight(11);
        BasicScenario scenario = setupScenario(1, true);
        setupGameLogs(scenario, gameLog(scenario));

        reportScenarioSuite(1, TestStatus.EXCEPTION);

        PlayerRecord capturedRecord = captureLastLogRecord();

        assertEquals(100, capturedRecord.getScore());
        assertEquals(PlayerRecord.Type.VALID_BUG, capturedRecord.getType());
    }


    @Test
    public void shouldNotReportLiarWhenRunningSeveralTestsDifferentScenarios() {
        BasicScenario scenario1 = setupScenario(1, true);
        setupGameLogs(scenario1, gameLog(scenario1));
        BasicScenario scenario2 = setupScenario(2, true);
        setupGameLogs(scenario2, gameLog(scenario2));

        TestSuiteResult suiteResult = new TestSuiteResult(PLAYER_NAME, 111L);
        suiteResult.addTestResult(1, TestStatus.PASSED);
        suiteResult.addTestResult(1, TestStatus.FAILED);
        suiteResult.addTestResult(2, TestStatus.PASSED);

        scoreService.suiteResult(suiteResult);

        List<PlayerRecord> records = captureLogRecords();
        assertScenariosReported(records, 1, 1, 2);
    }


    @Test
    public void shouldIgnoreReportForNonExistentScenario() {
        BasicScenario scenario = setupScenario(1, true);
        setupGameLogs(scenario, gameLog(scenario));

        TestSuiteResult suiteResult = new TestSuiteResult(PLAYER_NAME, 111L);
        suiteResult.addTestResult(2, TestStatus.PASSED);

        scoreService.suiteResult(suiteResult);

        verify(logService, never()).playerLog(recordCaptor.capture());
    }

    @Test
    public void shouldNotReportLiarTwiceInSameScenarioSuiteResults() {
        BasicScenario scenario = setupScenario(1, true);
        setupGameLogs(scenario, gameLog(scenario));

        TestSuiteResult suiteResult = new TestSuiteResult(PLAYER_NAME, 111L);
        suiteResult.addTestResult(1, TestStatus.PASSED);
        suiteResult.addTestResult(1, TestStatus.PASSED);

        scoreService.suiteResult(suiteResult);

        List<PlayerRecord> records = captureLogRecords();
        assertTypesReported(records, PlayerRecord.Type.LIAR, PlayerRecord.Type.LIAR);
        assertScoresReported(records, -configurationService.getLiarWeight(), 0);
    }

    @Test
    public void shouldNotReportLiarTwiceInSameScenarioSuiteResults_InvertedLiar() {
        BasicScenario scenario = setupScenario(1, false);
        setupGameLogs(scenario, gameLog(scenario));

        TestSuiteResult suiteResult = new TestSuiteResult(PLAYER_NAME, 111L);
        suiteResult.addTestResult(1, TestStatus.FAILED);
        suiteResult.addTestResult(1, TestStatus.FAILED);

        scoreService.suiteResult(suiteResult);

        List<PlayerRecord> records = captureLogRecords();
        assertTypesReported(records, PlayerRecord.Type.LIAR, PlayerRecord.Type.LIAR);
        assertScoresReported(records, -configurationService.getLiarWeight(), 0);
    }

    @Test
    public void shouldReportAllScenariosForSuite() {
        BasicScenario scenario = setupScenario(1, true);
        setupGameLogs(scenario, gameLog(scenario));

        TestSuiteResult suiteResult = new TestSuiteResult(PLAYER_NAME, 111L);
        suiteResult.addTestResult(1, TestStatus.FAILED);
        suiteResult.addTestResult(1, TestStatus.PASSED);

        scoreService.suiteResult(suiteResult);

        List<PlayerRecord> records = captureLogRecords();
        assertTypesReported(records, PlayerRecord.Type.VALID_BUG, PlayerRecord.Type.PASSED);
    }

    @Test
    public void shouldReportDuplicateWhenSecondFailInSuite() {
        BasicScenario scenario = setupScenario(1, true);
        setupGameLogs(scenario, gameLog(scenario));

        TestSuiteResult suiteResult = new TestSuiteResult(PLAYER_NAME, 111L);
        suiteResult.addTestResult(1, TestStatus.FAILED);
        suiteResult.addTestResult(1, TestStatus.FAILED);

        scoreService.suiteResult(suiteResult);

        List<PlayerRecord> records = captureLogRecords();
        assertTypesReported(records, PlayerRecord.Type.VALID_BUG, PlayerRecord.Type.DUPLICATE);
        assertScoresReported(records, 100, 0);
    }

    @Test
    public void shouldReportFailuresForSeveralScenarios() {
        BasicScenario scenario1 = setupScenario(1, true);
        setupGameLogs(scenario1, gameLog(scenario1));
        BasicScenario scenario2 = setupScenario(2, true);
        setupGameLogs(scenario2, gameLog(scenario2));

        TestSuiteResult suiteResult = new TestSuiteResult(PLAYER_NAME, 111L);
        suiteResult.addTestResult(1, TestStatus.FAILED);
        suiteResult.addTestResult(2, TestStatus.FAILED);

        scoreService.suiteResult(suiteResult);

        List<PlayerRecord> records = captureLogRecords();
        assertTypesReported(records, PlayerRecord.Type.VALID_BUG, PlayerRecord.Type.VALID_BUG);
        assertScenariosReported(records, 1, 2);
    }

    @Test
    public void shouldReturnScenarioResults() {
        setupScenario(1, true);
        setupScenario(2, false);

        TestSuiteResult suiteResult = new TestSuiteResult(PLAYER_NAME, 111L);
        suiteResult.addTestResult(1, TestStatus.FAILED);
        suiteResult.addTestResult(2, TestStatus.FAILED);

        BasicScenario scenario1 = scenario(1, 100);
        setupGameLogs(scenario1, gameLog(scenario1, record(scenario1, false, 100)));
        BasicScenario scenario2 = scenario(2, 100);
        setupGameLogs(scenario2, gameLog(scenario2, record(scenario2, false, 100)));

        List<PlayerRecord> result = scoreService.suiteResult(suiteResult);

        assertFalse(result.get(0).isPassed());
        assertFalse(result.get(1).isPassed());
    }


    private void assertTypesReported(List<PlayerRecord> records, PlayerRecord.Type... types) {
        assertEquals(types.length, records.size());
        for (int i = 0; i < types.length; i++) {
            PlayerRecord.Type type = types[i];
            assertEquals(type, records.get(i).getType());
        }
    }

    private void assertScoresReported(List<PlayerRecord> records, int... scores) {
        assertEquals(scores.length, records.size());
        for (int i = 0; i < scores.length; i++) {
            assertEquals(scores[i], records.get(i).getScore());
        }
    }

    private void assertScenariosReported(List<PlayerRecord> records, int ... scenarioIds) {
        assertEquals(scenarioIds.length, records.size());
        for (int i = 0; i < scenarioIds.length; i++) {
            assertEquals(scenarioIds[i], records.get(i).getScenario().getId());
        }
    }

    private void setupGameLogs(BasicScenario scenario, GameLog... gameLogs) {
        when(logService.getGameLogs(PLAYER_NAME, scenario))
                .thenReturn(Arrays.asList(gameLogs));
    }

    private GameLog gameLog(BasicScenario scenario,PlayerRecord ... records) {
        return gameLog(scenario, new Date(), records);
    }
    
    private GameLog gameLog(BasicScenario scenario, Date releaseLog, PlayerRecord ... records) {
        return new GameLog(scenario, releaseLog, records);
    }

    private BasicScenario setupScenario(int scenarioId, int bugWeight) {
        return setupScenario(scenarioId, bugWeight, 1);
    }

    private BasicScenario setupScenario(int scenarioId, int bugWeight, int bugId) {
        BasicScenario scenario = scenario(scenarioId, bugWeight, bugId);
        when(releaseEngine.getScenario(scenarioId)).thenReturn(scenario);
        return scenario;
    }

    private BasicScenario setupScenario(int scenarioId, boolean hasBug) {
        return setupScenario(scenarioId, hasBug ? 100 : 0);
    }

    private void reportScenarioSuite(int scenarioId, TestStatus testStatus) {
        reportScenarioSuite(scenarioId, testStatus, new Date().getTime());
    }

    private void reportScenarioSuite(int scenarioId, TestStatus testStatus, long timeStamp) {
        TestSuiteResult suite = new TestSuiteResult(PLAYER_NAME, timeStamp);
        suite.addTestResult(scenarioId, testStatus);
        scoreService.suiteResult(suite);
    }


    private BasicScenario scenario(int scenarioId, int bugWeight) {
        return scenario(scenarioId, bugWeight, 1);
    }

    private BasicScenario scenario(int scenarioId, int bugWeight, int bugId) {
        BasicScenario scenario = new MockScenario(scenarioId, "", null);
        if (bugWeight > 0) {
            Bug bug = new Bug(bugId);
            bug.setWeight(bugWeight);
            scenario.setBug(bug);
        }else{
            scenario.setBug(Bug.NULL_BUG);
        }
        return scenario;
    }
}
