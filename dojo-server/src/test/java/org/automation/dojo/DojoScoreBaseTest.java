package org.automation.dojo;

import org.automation.dojo.web.scenario.BasicScenario;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author serhiy.zelenin
 */
public abstract class DojoScoreBaseTest {
    @Captor
    ArgumentCaptor<PlayerRecord> recordCaptor;
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

}
