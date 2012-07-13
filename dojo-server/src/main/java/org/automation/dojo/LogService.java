package org.automation.dojo;

import org.automation.dojo.web.controllers.ReleaseLogView;
import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;

import java.util.Collection;
import java.util.List;

/**
 * @author serhiy.zelenin
 */
public interface LogService {
    void playerLog(PlayerRecord record);

    List<GameLog> getGameLogs(String player, BasicScenario scenario);

    Collection<String> getRegisteredPlayers();

    void createGameLog(Release previousRelease);

    boolean registerPlayer(String name);

    List<BoardRecord> getBoardRecords();

    ReleaseLog getCurrentReleaseLog();

    List<ReleaseLogView> getLastReleaseLogsForPlayer(String playerName, int maxLogRecordsAmount);
}
