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
        GameLog currentGame = gameLogs.get(gameLogs.size() - 1);
        if (!testPassed && alreadyReportedForThisGame(currentGame)) {
            logService.playerLog(new PlayerRecord(clientName, clientAddress,
                    scenario, testPassed, 0));
            return scenario.hasBug();
        }
        
        PlayerRecord lastPlayerRecord = null;
        int countBugsReportedForScenario = 0;
        
        for (GameLog gameLog : gameLogs) {
            List<PlayerRecord> playerRecords = gameLog.getPlayerRecords();
            for (PlayerRecord playerRecord : playerRecords) {
                if (!playerRecord.isPassed()) {
                    lastPlayerRecord = playerRecord;
                    countBugsReportedForScenario ++;
                }
            }
        }
        
        int weight = scenario.getBug().getWeight();
        int score = lastPlayerRecord == null ? weight : weight / (countBugsReportedForScenario + 1);
        logService.playerLog(new PlayerRecord(clientName, clientAddress,
                scenario, testPassed, score));
        return scenario.hasBug();
    }

    private boolean alreadyReportedForThisGame(GameLog game) {
        List<PlayerRecord> playerRecords = game.getPlayerRecords();
        for (PlayerRecord record : playerRecords) {
            if (!record.isPassed()) {
                return true;
            }
        }
        return false;
    }
}
