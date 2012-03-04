package org.automation.dojo;

import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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
                    "Bug #"+ currentBug.getId(), PlayerRecord.Type.DUPLICATE));
            return scenario.bugsFree();
        }

        List<PlayerRecord> reportedBugs = findFailedRecords(gameLogs);
        boolean reportMismatchedWithScenarioState = testPassed ^ scenario.bugsFree();
        boolean isLiar = reportMismatchedWithScenarioState && !reportedBugs.isEmpty();

        if (isLiar) {
            Bug reportedBug = reportedBugs.get(reportedBugs.size() - 1).getScenario().getBug();
            int reportedWeight = currentGame.liarReported()? 0 : reportedBug.getWeight();
            logService.playerLog(new PlayerRecord(clientName, clientAddress,
                    scenario, testPassed, -2*reportedWeight, "Liar! Current scenario #" + scenario.getId() +
                    (scenario.bugsFree()? " is bugs free." : " contains bug.") +
                    "Previously reported bug #"+ reportedBug.getId(), PlayerRecord.Type.LIAR));
            return scenario.bugsFree();
        }

        if (reportMismatchedWithScenarioState) {
            logService.playerLog(new PlayerRecord(clientName, clientAddress,
                    scenario, testPassed, 0, "Fix the test! It shows wrong result. Current scenario #" + scenario.getId() +
                    (scenario.bugsFree()? " is bugs free." : " contains bug."), PlayerRecord.Type.LIAR));
            return scenario.bugsFree();
        }

        int weight = currentBug.getWeight();
        int score = weight/(reportedBugs.size() + 1);
        logService.playerLog(new PlayerRecord(clientName, clientAddress,
                scenario, testPassed, score, "Scores for bug #" + currentBug.getId() +
                " scenario #"+scenario.getId(), PlayerRecord.Type.VALID_BUG));
        return scenario.bugsFree();
    }

    private List<PlayerRecord> findFailedRecords(List<GameLog> gameLogs) {
        ArrayList<PlayerRecord> result = new ArrayList<PlayerRecord>();
        for (GameLog gameLog : gameLogs) {
            PlayerRecord failedRecord = gameLog.findReportedBugs();
            if (failedRecord != null) {
                result.add(failedRecord);
            }
        }
        return result;
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
                                " for scenario #" + scenario.getId(), PlayerRecord.Type.MISSED));
            }
        }
    }
}
