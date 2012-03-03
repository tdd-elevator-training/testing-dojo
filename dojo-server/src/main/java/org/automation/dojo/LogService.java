package org.automation.dojo;

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
