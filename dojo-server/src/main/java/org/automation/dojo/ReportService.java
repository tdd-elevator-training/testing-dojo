package org.automation.dojo;

public interface ReportService {
    boolean testResult(String clientName, String clientAddress, int scenarioNumber, boolean testPassed);
}
