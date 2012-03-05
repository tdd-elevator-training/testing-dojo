package org.automation.dojo;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;
import org.fest.assertions.ListAssert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.*;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author serhiy.zelenin
 */
public class GameLogServiceTest {

    private static final String CLIENT_NAME = "test";
    private GameLogService gameLogService;

    @Before
    public void setUp() throws Exception {
        gameLogService = new GameLogService();
        gameLogService.registerPlayer(CLIENT_NAME);
    }

    @Test
    public void shouldStoreGameLogsWhenOneScenario() {
        MockScenario scenario = new MockScenario(1, "scenario1", null);
        
        gameLogService.createGameLog(new Release(scenario));

        List<GameLog> gameLogs = gameLogService.getGameLogs("127.0.0.1", scenario);
        assertEquals(1, gameLogs.size());
        assertAllGameLogsForScenario(gameLogs, scenario);
        Iterables.all(gameLogs, new EmptyPlayerLogs());
    }

    @Test
    public void shouldStoreGameLogsWhenSeveralScenario() {
        MockScenario scenario1 = new MockScenario(1, "scenario1", null);

        gameLogService.createGameLog(new Release(scenario1, new MockScenario(2, "scenario2", null)));

        List<GameLog> gameLogs = gameLogService.getGameLogs("127.0.0.1", scenario1);

        assertEquals(1, gameLogs.size());
        assertAllGameLogsForScenario(gameLogs, scenario1);
        Iterables.all(gameLogs, new EmptyPlayerLogs());
    }

    @Test
    public void shouldReturnPlayerLogs() {
        MockScenario scenario = new MockScenario(1, "scenario1", null);
        gameLogService.registerPlayer("vasya");
        gameLogService.createGameLog(new Release(scenario));
        gameLogService.playerLog(record(scenario, CLIENT_NAME));
        gameLogService.playerLog(record(scenario, "vasya"));

        List<GameLog> gameLogs = gameLogService.getGameLogs("vasya", scenario);

        assertFalse(Iterables.all(gameLogs, new EmptyPlayerLogs()));
        assertTrue(Iterables.all(gameLogs, new AllRecordsFor("vasya")));
    }

    @Test
    public void shouldReturnPlayerLogsForScenario() {
        MockScenario scenario1 = new MockScenario(1, "scenario1", null);
        MockScenario scenario2 = new MockScenario(2, "scenario2", null);

        gameLogService.createGameLog(new Release(scenario1, scenario2));
        gameLogService.playerLog(record(scenario1, CLIENT_NAME));
        gameLogService.playerLog(record(scenario2, CLIENT_NAME));
        
        List<GameLog> gameLogs = gameLogService.getGameLogs(CLIENT_NAME, scenario1);

        assertEquals(1, gameLogs.size());
        assertAllGameLogsForScenario(gameLogs, scenario1);
        assertTrue(Iterables.all(gameLogs, new AllRecordsForScenario(scenario1)));
    }

    @Test
    public void shouldStoreUniqueAddresses() {
        gameLogService.registerPlayer("vasya");
        gameLogService.registerPlayer(CLIENT_NAME);
        gameLogService.registerPlayer(CLIENT_NAME);

        assertThat(gameLogService.getRegisteredPlayers()).doesNotHaveDuplicates().contains(CLIENT_NAME, "vasya");
    } 
    
    @Test
    public void shouldReturnGameLogsForPlayer() {
        MockScenario scenario1 = new MockScenario(1, "", null);
        MockScenario scenario2 = new MockScenario(2, "", null);
        gameLogService.createGameLog(new Release(scenario1, scenario2));
        gameLogService.playerLog(record(scenario1, CLIENT_NAME));
        gameLogService.playerLog(record(scenario2, CLIENT_NAME));

        List<ReleaseLog> releaseLogs = gameLogService.getReleaseLogs();
        ReleaseLog releaseLog = releaseLogs.get(0);

        List<PlayerRecord> records = releaseLog.getRecordsForPlayer(CLIENT_NAME);

        assertTrue(Iterables.all(records, new RecordsForPlayer(CLIENT_NAME)));
        
    }

    @Test
    public void testShouldRegister(){
        assertTrue(gameLogService.registerPlayer("vasya"));
    }

    @Test
    public void testShouldNotRegisterWhenNameAndAddressRegistered(){
        assertTrue(gameLogService.registerPlayer("vasya"));
        assertFalse(gameLogService.registerPlayer("vasya"));
    }

    @Test
    public void shouldSkipLogWhenPlayerUnregistered(){
        MockScenario scenario = new MockScenario(1, "", null);
        gameLogService.createGameLog(new Release(scenario));
        try {
            gameLogService.playerLog(record(scenario, "vasya"));
            fail();
        } catch (IllegalArgumentException e) {
        }
    } 
    
    private PlayerRecord record(MockScenario scenario, String playerName) {
        return new PlayerRecord(playerName, scenario, false, 100, "", PlayerRecord.Type.LIAR);
    }

    private ListAssert assertAllGameLogsForScenario(List<GameLog> gameLogs, MockScenario scenario) {
        return assertThat(gameLogs).onProperty("scenario").contains(scenario);
    }

    private static class EmptyPlayerLogs implements Predicate<GameLog> {
        public boolean apply(GameLog gameLog) {
            return Iterables.isEmpty(gameLog.getPlayerRecords());
        }
    }

    private static class AllRecordsFor implements Predicate<GameLog> {
        private final String expectedPlayer;

        public AllRecordsFor(String expectedPlayer) {
            this.expectedPlayer = expectedPlayer;
        }

        public boolean apply(GameLog gameLog) {
            return Iterables.all(gameLog.getPlayerRecords(), new RecordsForPlayer(expectedPlayer));
        }

    }

    private static class RecordsForPlayer implements Predicate<PlayerRecord> {
        private String expectedPlayer;

        public RecordsForPlayer(String expectedPlayer) {
            this.expectedPlayer = expectedPlayer;
        }

        public boolean apply(PlayerRecord input) {
            return input.getPlayerName().equals(expectedPlayer);
        }
    }

    private class AllRecordsForScenario implements Predicate<GameLog> {
        public BasicScenario scenario;

        private AllRecordsForScenario(BasicScenario scenario) {
            this.scenario = scenario;
        }

        public boolean apply(GameLog gameLog) {
            return Iterables.all(gameLog.getPlayerRecords(), new Predicate<PlayerRecord>() {

                public boolean apply(PlayerRecord record  ) {
                    return record.getScenario().equals(scenario);
                }
            });
        }
    }
}
