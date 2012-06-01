package org.automation.dojo;

import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class DojoScoreService implements ScoreService {
    @Autowired
    private LogService logService;

    @Autowired
    private ReleaseEngine releaseEngine;

    @Autowired
    private ConfigurationService configurationService;

    private ReentrantLock lock = new ReentrantLock();

    public DojoScoreService() {
    }

    public DojoScoreService(LogService logService, ReleaseEngine releaseEngine,
            ConfigurationService configurationService) {
        this.logService = logService;
        this.releaseEngine = releaseEngine;
        this.configurationService = configurationService;
    }

    public boolean testResult(String clientName, int scenarioNumber, TestResult testResult, long timeStamp) {
        BasicScenario scenario = releaseEngine.getScenario(scenarioNumber);
        List<GameLog> gameLogs = logService.getGameLogs(clientName, scenario);
        if (testResult == TestResult.EXCEPTION) {
            logService.playerLog(new PlayerRecord(clientName, scenario, false, -configurationService.getExceptionWeight(),
                    "Exception in test case! Fix it!", PlayerRecord.Type.EXCEPTION));
            return scenario.bugsFree();
        }

        //last log will be a log for current release
        GameLog currentGame = lastGameLog(gameLogs);
        Bug currentBug = scenario.getBug();
        boolean testPassed = testResult == TestResult.PASSED;
        if (!testPassed && currentGame.bugReported(currentBug)) {
            logService.playerLog(new PlayerRecord(clientName, scenario, testPassed, 0,
                    "Bug already reported for this Minor Release. " +
                            "Bug #" + currentBug.getId(), PlayerRecord.Type.DUPLICATE));
            return scenario.bugsFree();
        }

        List<PlayerRecord> reportedBugs = findFailedRecords(gameLogs, scenario.getBug());
        boolean reportMismatchedWithScenarioState = testPassed ^ scenario.bugsFree();
        boolean isLiar = reportMismatchedWithScenarioState && !reportedBugs.isEmpty();

        if (isLiar) {
            Bug reportedBug = reportedBugs.get(reportedBugs.size() - 1).getScenario().getBug();
            int reportedWeight = currentGame.liarReported() ? 0 : configurationService.getLiarWeight();
            logService.playerLog(new PlayerRecord(clientName, scenario, testPassed, -2 * reportedWeight,
                    "Liar! Current scenario #" + scenario.getId() +
                            (scenario.bugsFree() ? " is bugs free." : " contains bug.") +
                            "Previously reported bug #" + reportedBug.getId(), PlayerRecord.Type.LIAR));
            return scenario.bugsFree();
        }

        if (reportMismatchedWithScenarioState) {
            logService.playerLog(new PlayerRecord(clientName, scenario, testPassed,
                    currentGame.liarReported() ? 0 : -configurationService.getLiarWeight(),
                    "Fix the test! It shows wrong result. Current scenario #" + scenario.getId() +
                            (scenario.bugsFree() ? " is bugs free." : " contains bug."), PlayerRecord.Type.LIAR));
            return scenario.bugsFree();
        }

        if (testPassed && scenario.bugsFree()) {
            logService.playerLog(new PlayerRecord(clientName, scenario, testPassed, 0,
                    "Good! No bugs reported for bugs free scenario", PlayerRecord.Type.PASSED));
            return scenario.bugsFree();
        }

        int weight = currentBug.getWeight();
        int score = weight / (reportedBugs.size() + 1);
        logService.playerLog(
                new PlayerRecord(clientName, scenario, testPassed, score, "Scores for bug #" + currentBug.getId() +
                        " scenario #" + scenario.getId(), PlayerRecord.Type.VALID_BUG));
        return scenario.bugsFree();
    }

    private List<PlayerRecord> findFailedRecords(List<GameLog> gameLogs, Bug bug) {
        ArrayList<PlayerRecord> result = new ArrayList<PlayerRecord>();
        for (GameLog gameLog : gameLogs) {
            PlayerRecord failedRecord = gameLog.findReportedBugs(bug);
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
        Collection<String> players = logService.getRegisteredPlayers();
        for (String player : players) {
            List<BasicScenario> scenarios = previousRelease.getScenarios();
            for (BasicScenario scenario : scenarios) {
                if (scenario.bugsFree()) {
                    continue;
                }
                List<GameLog> gameLogs = logService.getGameLogs(player, scenario);
                GameLog gameLog = lastGameLog(gameLogs);
                if (gameLog.bugReported(scenario.getBug())) {
                    continue;
                }
                logService.playerLog(new PlayerRecord(player, scenario, true, -scenario.getBug().getWeight(),
                        "After Minor Release check. Missed bug #" + scenario.getBug().getId() +
                                " for scenario #" + scenario.getId(), PlayerRecord.Type.MISSED));
            }
        }
    }

    public void tick(long currentTime) {
        Collection<String> players = logService.getRegisteredPlayers();
        ReleaseLog lastReleaseLog = logService.getCurrentReleaseLog();
        if (currentTime - lastReleaseLog.getReleaseTime() < configurationService.getPenaltyTimeOut()) {
            return;
        }
        for (String player : players) {
            List<BasicScenario> scenarios = lastReleaseLog.getRelease().getScenarios();
            for (BasicScenario scenario : scenarios) {
                List<PlayerRecord> playerRecords = lastReleaseLog.getRecordsFor(player, scenario);
                long lastReport = lastReleaseLog.getReleaseTime();
                PlayerRecord lastNonTimeoutRecord = findLastNonTimeoutRecord(playerRecords);
                if (!playerRecords.isEmpty() && lastNonTimeoutRecord != null) {
                    lastReport = lastNonTimeoutRecord.getLogTime();
                }
                long currentScenarioDelay = currentTime - lastReport;
                if (currentScenarioDelay < configurationService.getPenaltyTimeOut()) {
                    continue;
                }

                int penalty = (int) (currentScenarioDelay / configurationService.getPenaltyTimeOut());

                logService.playerLog(
                        new PlayerRecord(player, scenario, false, -penalty * configurationService.getPenaltyValue(),
                                "Where are your test results? Waiting for " + (currentScenarioDelay / (1000 * 60)) + " minutes",
                                PlayerRecord.Type.TIMEOUT));
            }
        }
    }

    private PlayerRecord findLastNonTimeoutRecord(List<PlayerRecord> playerRecords) {
        for (int i = playerRecords.size() - 1; i >= 0; i--) {
            PlayerRecord record = playerRecords.get(i);
            if (record.getType() != PlayerRecord.Type.TIMEOUT) {
                return record;
            }
        }
        return null;
    }

}
