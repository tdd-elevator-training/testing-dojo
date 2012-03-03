package org.automation.dojo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author serhiy.zelenin
 */
@RunWith(MockitoJUnitRunner.class)
public class DojoReportServiceTest {
    @Captor ArgumentCaptor<PlayerRecord> recordCaptor;
    @Mock LogService logService;
    @Mock ReleaseEngine releaseEngine;

    private DojoReportService reportService;


    @Before
    public void setUp() throws Exception {
        reportService = new DojoReportService(logService, releaseEngine);
    }

    @Test
    public void shouldReturnFailureWhenReportFailure() {
        setupScenario(1, true);

        assertFalse(reportScenario(1, false));
    }

    @Test
    public void shouldReturnPassedWhenReportPassed() {
        setupScenario(1, false);

        assertTrue(reportScenario(1, true));
    }

    @Test
    public void shouldAddScoreWhenBugFound() {
        setupScenario(1, 88);

        reportScenario(1, false);

        verify(logService, atLeastOnce()).playerLog(recordCaptor.capture());
        PlayerRecord record = recordCaptor.getValue();

        assertEquals(88, record.getScore());
        assertEquals(1, record.getScenario());
        assertFalse(record.isPassed());
    }

    @Test
    public void shouldAddHalfScoreWhenFoundAgainAfterPassed(){
//        assertEquals(100/2);
    }

    private void setupScenario(int scenarioId, int bugWeight) {
        when(releaseEngine.getScenario(scenarioId)).thenReturn(createScneario(1, bugWeight));
    }

    private void setupScenario(int scenarioId, boolean hasBug) {
        setupScenario(scenarioId, hasBug ? 100 : 0);
    }

    private boolean reportScenario(int scenarioId, boolean testPassed) {
        return reportService.testResult("vasya", "10.10.1.1", scenarioId, testPassed);
    }


    private Scenario createScneario(int scenarioId, int bugWeight) {
        Scenario scenario = new Scenario(scenarioId, null);
        if (bugWeight > 0) {
            Bug bug = new Bug(1);
            bug.setWeight(bugWeight);
            scenario.setBug(bug);
        }else{
            scenario.setBug(Bug.NULL_BUG);
        }
        return scenario;
    }
}
