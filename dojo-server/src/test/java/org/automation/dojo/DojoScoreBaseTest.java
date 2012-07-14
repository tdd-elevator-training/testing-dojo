package org.automation.dojo;

import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author serhiy.zelenin
 */
public abstract class DojoScoreBaseTest {
    protected static final String PLAYER_NAME = "vasyad";
    @Captor
    ArgumentCaptor<PlayerRecord> recordCaptor;
    @Captor
    ArgumentCaptor<List<PlayerRecord>> recordCaptor2;
    @Mock
    LogService logService;
    @Mock ReleaseEngine releaseEngine;
    protected ConfigurationService configurationService;

    protected DojoScoreService scoreService;

    @Before
    public void setUp() throws Exception {
        configurationService = new ConfigurationService();
        scoreService = new DojoScoreService(logService, releaseEngine, configurationService);
    }

    protected PlayerRecord captureLastLogRecord() {
        verify(logService, atLeastOnce()).playerLog(recordCaptor.capture());
        return recordCaptor.getValue();
    }

    protected List<PlayerRecord> captureLogRecords() {
        verify(logService, atLeastOnce()).playerLog(recordCaptor.capture());
        return recordCaptor.getAllValues();
    }


    protected void setupRegisteredPlayers(String... players) {
        when(logService.getRegisteredPlayers()).thenReturn(Arrays.asList(players));
    }
    
    protected PlayerRecord record(BasicScenario scenario, boolean passed, int score) {
        return record(scenario, passed, score, PlayerRecord.Type.VALID_BUG);
    }

    protected PlayerRecord record(BasicScenario scenario, boolean passed, int score, PlayerRecord.Type type) {
        return new PlayerRecord("", scenario, passed, score, "", type);
    }

    protected PlayerRecord record(String player, BasicScenario scenario, long logTime) {
        return record(player, scenario, logTime, PlayerRecord.Type.VALID_BUG);
    }

    protected PlayerRecord record(String player, BasicScenario scenario, long logTime, PlayerRecord.Type type) {
        return new PlayerRecord(player, scenario, false, 0, "", type, new Date(logTime));
    }

    protected MockScenario setupScenario(int scenarioId, int bugWeight) {
        return setupScenario(scenarioId, bugWeight, 1);
    }

    protected MockScenario setupScenario(int scenarioId, int bugWeight, int bugId) {
        MockScenario scenario = scenario(scenarioId, bugWeight, bugId);
        when(releaseEngine.getScenario(scenarioId)).thenReturn(scenario);
        return scenario;
    }

    protected MockScenario setupScenario(int scenarioId, boolean hasBug) {
        return setupScenario(scenarioId, hasBug ? 100 : 0);
    }

    protected void reportScenarioSuite(int scenarioId, TestStatus testStatus) {
        reportScenarioSuite(scenarioId, testStatus, new Date().getTime());
    }

    void reportScenarioSuite(int scenarioId, TestStatus testStatus, long timeStamp) {
        TestSuiteResult suite = new TestSuiteResult(PLAYER_NAME, timeStamp);
        suite.addTestResult(scenarioId, testStatus);
        scoreService.suiteResult(suite);
    }

    protected MockScenario scenario(int scenarioId, int bugWeight) {
        return scenario(scenarioId, bugWeight, 1);
    }

    protected MockScenario scenario(int scenarioId, int bugWeight, int bugId) {
        MockScenario scenario = new MockScenario(scenarioId, "", null);
        if (bugWeight > 0) {
            Bug bug = new Bug(bugId);
            bug.setWeight(bugWeight);
            scenario.setBug(bug);
        }else{
            scenario.setBug(Bug.NULL_BUG);
        }
        return scenario;
    }

    protected void setupGameLogs(BasicScenario scenario, GameLog... gameLogs) {
        setupGameLogs(scenario, PLAYER_NAME, gameLogs);
    }

    protected void setupGameLogs(BasicScenario scenario, String playerName, GameLog... gameLogs) {
        when(logService.getGameLogs(playerName, scenario))
                .thenReturn(Arrays.asList(gameLogs));
    }

    protected void setupGameLogs(String playerName, BasicScenario ... scenarios) {
        for (BasicScenario scenario : scenarios) {
            when(logService.getGameLogs(playerName, scenario))
                    .thenReturn(Arrays.asList(gameLog(scenario)));
        }
    }

    protected GameLog gameLog(BasicScenario scenario, PlayerRecord... records) {
        return gameLog(scenario, new Date(), records);
    }

    private GameLog gameLog(BasicScenario scenario, Date releaseLog, PlayerRecord ... records) {
        return new GameLog(scenario, releaseLog, records);
    }
}
