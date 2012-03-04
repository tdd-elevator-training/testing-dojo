package org.automation.dojo;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.automation.dojo.web.scenario.Release;
import org.fest.assertions.ListAssert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author serhiy.zelenin
 */
public class GameLogServiceTest {

    private GameLogService gameLogService;

    @Before
    public void setUp() throws Exception {
        gameLogService = new GameLogService();
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

        gameLogService.createGameLog(new Release(scenario));
        gameLogService.playerLog(record(scenario, "127.0.0.1"));
        gameLogService.playerLog(record(scenario, "10.10.1.1"));

        List<GameLog> gameLogs = gameLogService.getGameLogs("10.10.1.1", scenario);

        assertFalse(Iterables.all(gameLogs, new EmptyPlayerLogs()));
        assertTrue(Iterables.all(gameLogs, new AllLogsForAddress("10.10.1.1")));
    }

    @Test
    public void shouldReturnPlayerLogsForScenario() {
        MockScenario scenario1 = new MockScenario(1, "scenario1", null);
        MockScenario scenario2 = new MockScenario(2, "scenario2", null);

        gameLogService.createGameLog(new Release(scenario1, scenario2));
        gameLogService.playerLog(record(scenario1, "127.0.0.1"));
        gameLogService.playerLog(record(scenario2, "127.0.0.1"));
        
        List<GameLog> gameLogs = gameLogService.getGameLogs("127.0.0.1", scenario1);

        assertEquals(1, gameLogs.size());
        assertAllGameLogsForScenario(gameLogs, scenario1);
        Iterables.all(gameLogs, new EmptyPlayerLogs());
    }

    @Test
    public void shouldStoreUniqueAddresses() {
        MockScenario scenario = new MockScenario(1, "", null);
        gameLogService.createGameLog(new Release(scenario));
        gameLogService.playerLog(record(scenario, "10.10.1.1"));
        gameLogService.playerLog(record(scenario, "127.0.0.1"));
        gameLogService.playerLog(record(scenario, "127.0.0.1"));

        assertThat(gameLogService.getUniqueClientAddresses()).doesNotHaveDuplicates().contains("10.10.1.1", "127.0.0.1");
    } 
    
    private PlayerRecord record(MockScenario scenario, String clientAddress) {
        return new PlayerRecord("test", clientAddress, scenario, false, 100, "", PlayerRecord.Type.LIAR);
    }

    private ListAssert assertAllGameLogsForScenario(List<GameLog> gameLogs, MockScenario scenario) {
        return assertThat(gameLogs).onProperty("scenario").contains(scenario);
    }

    private static class EmptyPlayerLogs implements Predicate<GameLog> {
        public boolean apply(GameLog gameLog) {
            return Iterables.isEmpty(gameLog.getPlayerRecords());
        }
    }

    private static class AllLogsForAddress implements Predicate<GameLog> {
        private final String expectedAddress;

        public AllLogsForAddress(String expectedAddress) {
            this.expectedAddress = expectedAddress;
        }

        public boolean apply(GameLog gameLog) {
            return Iterables.all(gameLog.getPlayerRecords(), new Predicate<PlayerRecord>() {
                public boolean apply(PlayerRecord input) {
                    return input.getClientAddress().equals(expectedAddress);
                }
            });
        }
    }
}
