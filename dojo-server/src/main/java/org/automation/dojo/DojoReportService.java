package org.automation.dojo;

import org.springframework.beans.factory.annotation.Autowired;

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
//        logService.playerLog(new PlayerRecord(clientName, clientAddress, scenarioNumber, testPassed));
        return releaseEngine.getScenario(scenarioNumber).hasBug();
    }
}
