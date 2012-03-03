package org.automation.dojo;

import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DojoScoreService implements ScoreService {
    @Autowired
    private LogService logService;

    @Autowired
    private ReleaseEngine releaseEngine;

    public DojoScoreService() {
    }

    public DojoScoreService(LogService logService, ReleaseEngine releaseEngine) {
        this.logService = logService;
        this.releaseEngine = releaseEngine;
    }

    public boolean testResult(String clientName, String clientAddress, int scenarioNumber, boolean testPassed) {
        BasicScenario scenario = releaseEngine.getScenario(scenarioNumber);
        List<GameLog> gameLogs = logService.getGameLogs(clientAddress, scenario);
        //last log will be a log for current release
        GameLog currentGame = lastGameLog(gameLogs);
        Bug currentBug = scenario.getBug();
        if (!testPassed && currentGame.bugReported()) {
            logService.playerLog(new PlayerRecord(clientName, clientAddress,
                    scenario, testPassed, 0, "Bug already reported for this Minor Release. " +
                    "Bug #"+ currentBug.getId()));
            return scenario.bugsFree();
        }
        
        int countBugsReportedForScenario = 1;

        for (GameLog gameLog : gameLogs) {
            boolean bugAlreadyReported = gameLog.bugReported();
            if (bugAlreadyReported) {
                countBugsReportedForScenario ++;
            }
        }
        
        int weight = currentBug.getWeight();
        int score = weight/countBugsReportedForScenario;
        logService.playerLog(new PlayerRecord(clientName, clientAddress,
                scenario, testPassed, score, "Scores for bug #" + currentBug.getId() +
                " scenario #"+scenario.getId()));
        return scenario.bugsFree();
    }

    private GameLog lastGameLog(List<GameLog> gameLogs) {
        return gameLogs.get(gameLogs.size() - 1);
    }

    public void nextRelease(Release previousRelease) {
        List<String> clientAddresses = logService.getUniqueClientAddresses();
        for (String clientAddress : clientAddresses) {
            List<BasicScenario> scenarios = previousRelease.getScenarios();
            for (BasicScenario scenario : scenarios) {
                if (scenario.bugsFree()) {
                    continue;
                }
                List<GameLog> gameLogs = logService.getGameLogs(clientAddress, scenario);
                GameLog gameLog = lastGameLog(gameLogs);
                if (gameLog.bugReported()) {
                    continue;
                }
                logService.playerLog(new PlayerRecord("<system>", clientAddress, scenario,
                        true, -scenario.getBug().getWeight(), 
                        "After Minor Release check. Missed bug #"+scenario.getBug().getId() + 
                                " for scenario #" + scenario.getId()));
            }
        }
    }
}
