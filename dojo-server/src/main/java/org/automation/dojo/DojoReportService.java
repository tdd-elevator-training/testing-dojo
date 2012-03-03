package org.automation.dojo;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DojoReportService implements ReportService {
    @Autowired
    private LogService logService;

    @Autowired
    private ReleaseEngine releaseEngine;

    public DojoReportService() {
    }

    public DojoReportService(LogService logService, ReleaseEngine releaseEngine) {
        this.logService = logService;
        this.releaseEngine = releaseEngine;
    }

    public boolean testResult(String clientName, String clientAddress, int scenarioNumber, boolean testPassed) {
        Scenario scenario = releaseEngine.getScenario(scenarioNumber);
        List<GameLog> gameLogs = logService.getGameLogs(clientAddress, scenario);
        //last log will be a log for current release
        GameLog currentGame = lastGameLog(gameLogs);
        if (!testPassed && currentGame.bugReported()) {
            logService.playerLog(new PlayerRecord(clientName, clientAddress,
                    scenario, testPassed, 0));
            return scenario.bugsFree();
        }
        
        int countBugsReportedForScenario = 1;
        
        for (GameLog gameLog : gameLogs) {
            if (gameLog.bugReported()) {
                countBugsReportedForScenario ++;
            }
        }
        
        int weight = scenario.getBug().getWeight();
        int score = weight/countBugsReportedForScenario;
        logService.playerLog(new PlayerRecord(clientName, clientAddress,
                scenario, testPassed, score));
        return scenario.bugsFree();
    }

    private GameLog lastGameLog(List<GameLog> gameLogs) {
        return gameLogs.get(gameLogs.size() - 1);
    }

    public void nextMinorRelease(Release previousRelease) {
        List<String> clientAddresses = logService.getUniqueClientAddresses();
        for (String clientAddress : clientAddresses) {
            List<Scenario> scenarios = previousRelease.getScenarios();
            for (Scenario scenario : scenarios) {
                if (scenario.bugsFree()) {
                    continue;
                }
                List<GameLog> gameLogs = logService.getGameLogs(clientAddress, scenario);
                GameLog gameLog = lastGameLog(gameLogs);
                if (gameLog.bugReported()) {
                    continue;
                }
                logService.playerLog(new PlayerRecord("<bug should be found>", clientAddress, scenario,
                        true, -scenario.getBug().getWeight()));
            }
        }
    }
}
