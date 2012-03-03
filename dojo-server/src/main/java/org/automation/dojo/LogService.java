package org.automation.dojo;

import org.automation.dojo.web.scenario.Scenario;

import java.util.List;

/**
 * @author serhiy.zelenin
 */
public interface LogService {
    void playerLog(PlayerRecord record);

    List<GameLog> getGameLogs(String clientAddress, Scenario scenario);

    List<String> getUniqueClientAddresses();

    void createGameLog(Release previousRelease);
}
