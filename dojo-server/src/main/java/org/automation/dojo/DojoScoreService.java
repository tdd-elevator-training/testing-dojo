package org.automation.dojo;

import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
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

    @Override
    public List<PlayerRecord> suiteResult(TestSuiteResult suite) {
        List<PlayerRecord> result = new LinkedList<PlayerRecord>();
        Map<Integer,List<TestStatus>> scenariosResults = suite.getScenarioResults();
        for (Map.Entry<Integer, List<TestStatus>> scenarioResults : scenariosResults.entrySet()) {
            Integer scenarioId = scenarioResults.getKey();
            BasicScenario scenario = releaseEngine.getScenario(scenarioId);
            if (scenario == null) {
                continue;
            }
            List<GameLog> gameLogs = logService.getGameLogs(suite.getPlayerName(), scenario);
            if (gameLogs.isEmpty()) {
                continue;
            }
            result.addAll(buildScenarioResults(suite, scenarioResults.getValue(), scenario, gameLogs));
        }

        reportAllResults(result);
        return result;
    }

    private void reportAllResults(List<PlayerRecord> results) {
        for (PlayerRecord result : results) {
            logService.playerLog(result);
        }
    }

    private List<PlayerRecord> buildScenarioResults(TestSuiteResult suite, List<TestStatus> testResults,
                                                    BasicScenario scenario, List<GameLog> gameLogs) {
        List<PlayerRecord> result = new LinkedList<PlayerRecord>();

        GameLog currentGame = lastGameLog(gameLogs);
        Bug currentBug = scenario.getBug();
        boolean liarReported = currentGame.liarReported();
        TestStatus suiteStatus = suite.getStatusForScenario(scenario.getId());
        boolean bugReported = currentGame.bugReported(currentBug);
        boolean liarReportedSuite = false;
        boolean bugReportedSuite = false;
        List<PlayerRecord> reportedBugs = findFailedRecords(gameLogs, scenario.getBug());

        for (TestStatus testStatus : testResults) {
            boolean testPassed = testStatus == TestStatus.PASSED;
            if (testPassed && scenario.bugsFree()) {
                result.add(new PlayerRecord(suite.getPlayerName(), scenario, true,
                        0,
                        "Good! No bugs reported for bugs free scenario",
                        PlayerRecord.Type.PASSED));
                continue;
            }

            boolean suiteFailed = suiteStatus == TestStatus.FAILED;
            if (testPassed && suiteFailed && !scenario.bugsFree()) {
                result.add(new PlayerRecord(suite.getPlayerName(), scenario, testPassed,
                        0,
                        "Passed! I see another test failed the scenario",
                        PlayerRecord.Type.PASSED));
                continue;
            }

            boolean reportMismatchedWithScenarioState = testPassed ^ scenario.bugsFree();
            boolean isLiar = reportMismatchedWithScenarioState && !reportedBugs.isEmpty();
            String scenarioIsBugFree = scenario.bugsFree() ? "is bugs free" : "contains bug";

            //Valid bug has been reported for this release but now scenario is passed. Should be punished twice.
            int liarWeight = liarReported || liarReportedSuite? 0 : configurationService.getLiarWeight();
            if (isLiar) {
                Bug reportedBug = reportedBugs.get(reportedBugs.size() - 1).getScenario().getBug();
                result.add(new PlayerRecord(suite.getPlayerName(), scenario, testPassed,
                        -2 * liarWeight,
                        "Liar! Current scenario " + scenarioIsBugFree + ". " +
                                "Previously reported bug #" + reportedBug.getId(),
                        PlayerRecord.Type.LIAR));
                liarReportedSuite = true;
                continue;
            }

            if (reportMismatchedWithScenarioState) {
                result.add(new PlayerRecord(suite.getPlayerName(), scenario, testPassed,
                        -liarWeight,
                        "Fix the test! It shows wrong result. Current scenario " + scenarioIsBugFree,
                        PlayerRecord.Type.LIAR));
                liarReportedSuite = true;
                continue;
            }


            if (!testPassed && (bugReported || bugReportedSuite)) {
                result.add(new PlayerRecord(suite.getPlayerName(), scenario, testPassed,
                        0,
                        "Bug #" + currentBug.getId() + " already reported for this Minor Release",
                        PlayerRecord.Type.DUPLICATE));
                continue;
            }

            int weight = currentBug.getWeight();
            int score = weight / (reportedBugs.size() + 1);
            result.add(new PlayerRecord(suite.getPlayerName(), scenario, testPassed,
                    score,
                    "Scores for bug #" + currentBug.getId(),
                    PlayerRecord.Type.VALID_BUG));
            bugReportedSuite = true;
        }
        return result;
    }
}
