package org.automation.dojo;

import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;

import java.util.Collection;
import java.util.List;

/**
 * @author serhiy.zelenin
 */
public interface LogService {
    void playerLog(PlayerRecord record);

    List<GameLog> getGameLogs(String clientAddress, BasicScenario scenario);

    Collection<String> getUniqueClientAddresses();

    void createGameLog(Release previousRelease);
}
