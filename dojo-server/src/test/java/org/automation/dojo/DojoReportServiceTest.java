package org.automation.dojo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
        Scenario scenario = new Scenario(1, null);
        scenario.setBug(new Bug(1));
        when(releaseEngine.getScenario(1)).thenReturn(scenario);

        assertFalse(reportService.testResult("vasya", "10.10.1.1", 1, false));
    }

    @Test
    public void shouldReturnPassedWhenReportPassed() {
        Scenario scenario = new Scenario(1, null);
        scenario.setBug(Bug.NULL_BUG);
        when(releaseEngine.getScenario(1)).thenReturn(scenario);

        assertTrue(reportService.testResult("vasya", "10.10.1.1", 1, true));
    }


    @Test
    @Ignore
    public void shouldAddScoreWhenBugFound() {
        fail();
        new ReleaseEngine();

        assertFalse(reportService.testResult("vasya", "10.10.1.1", 1, false));

        verify(logService, atLeastOnce()).playerLog(recordCaptor.capture());
        PlayerRecord record = recordCaptor.getValue();

        assertEquals(100, record.getScore());
        assertEquals(1, record.getScenario());
        assertFalse(record.isPassed());
    }
}
