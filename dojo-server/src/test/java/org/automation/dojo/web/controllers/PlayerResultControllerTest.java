package org.automation.dojo.web.controllers;

import org.automation.dojo.ScoreService;
import org.automation.dojo.TestStatus;
import org.automation.dojo.TimeService;
import org.fest.assertions.Index;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerResultControllerTest {

    private MockHttpServletResponse response;
    private MockHttpServletRequest request;
    private PlayerResultController controller;

    @Mock ScoreService scoreService;
    @Mock TimeService timeService;

    @Captor ArgumentCaptor<Integer> scenarioCaptor;
    @Captor ArgumentCaptor<TestStatus> testResultCaptor;
    @Captor ArgumentCaptor<String> nameCaptor;
    @Captor ArgumentCaptor<Long> timeStampCaptor;

    @Before
    public void setUp() throws Exception {
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        when(timeService.now()).thenReturn(new Date());
        controller = new PlayerResultController(scoreService, timeService);
    }


    @Test
    public void shouldReturnSucceedWhenScenarioPassed() throws IOException, ServletException {
        when(scoreService.testResult(anyString(), anyInt(), Matchers.<TestStatus>anyObject(),
                anyLong())).thenReturn(true);
        request.addParameter("scenario1", "passed");

        controller.service(request, response);

        assertEquals("scenario1=passed", response.getContentAsString().trim());
    }

    @Test
    public void shouldReportScenarioResultWhenPassed() throws IOException, ServletException {
        setupRequest("vasya", "passed");

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported("vasya", 1, true, TestStatus.PASSED);
    }

    @Test
    public void shouldReportScenarioResultWhenFailed() throws IOException, ServletException {
        setupRequest("petya", "failed");

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported("petya", 1, false, TestStatus.FAILED);
    }

    @Test
    public void shouldReportSeveralScenarioResultWhenSeveralReported() throws IOException, ServletException {
        setupRequest("petya", "passed", "failed");

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported("petya", 1, true, TestStatus.PASSED);
        assertResultReported("petya", 2, false, TestStatus.FAILED);
    }

    @Test
    public void shouldReportExceptionFailure() throws IOException, ServletException {
        setupRequest("petya", "exception");

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported("petya", 1, false, TestStatus.EXCEPTION);
    }

    @Test
    public void shouldHaveServiceActualResultsWhenReported() throws IOException, ServletException {
        when(scoreService.testResult("masha", 1, TestStatus.PASSED, timeService.now().getTime())).thenReturn(false);
        setupRequest("masha", "passed");

        controller.service(request, response);

        captureTestResultValues();
        assertEquals("scenario1=failed", response.getContentAsString().trim());
    }

    @Test
    public void shouldReportProperlyWhenNotStandardRequestParamsReceived() throws IOException, ServletException {
        request.addParameter("scenario5", "true");
        request.addParameter("scenario11", "false");

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported(null, 5, true, TestStatus.PASSED);
        assertResultReported(null, 11, false, TestStatus.FAILED);
    }

    @Test
    public void shouldReportFailureWhenOneOfScenarioResultsFailed() throws IOException, ServletException {
        request.addParameter("scenario1", "passed");
        request.addParameter("scenario1", "failed");

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported(null, 1, false, TestStatus.FAILED);
    }

    @Test
    public void shouldReportExceptionWhenOneOfScenarioResultsException() throws IOException, ServletException {
        request.addParameter("scenario1", "passed");
        request.addParameter("scenario1", "failed");
        request.addParameter("scenario1", "exception");

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported(null, 1, false, TestStatus.EXCEPTION);
    }

    @Test
    public void shouldReportFromRobotFramework() throws IOException, ServletException {
        request.addParameter("scenario1", "FAIL");

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported(null, 1, true, TestStatus.FAILED);
    }

    @Test
    public void shouldReportForExistingScenariosWhenNonExistenceScenarioInList() throws IOException, ServletException {
        request.addParameter("scenario123", "FAIL");
        request.addParameter("scenario1", "PASS");
        when(scoreService.testResult(Matchers.<String>any(), eq(123), Matchers.<TestStatus>any(),
                anyLong())).thenThrow(new IllegalArgumentException());

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported(null, 1, true, TestStatus.PASSED);
    }

    @Test
    public void shouldSendTimeWhenReportSeveralResults() throws IOException, ServletException {
        setupRequest("vasya", "false", "true");
        when(timeService.now()).thenReturn(new Date(12345L));

        controller.service(request, response);

        captureTestResultValues();
        assertThat(timeStampCaptor.getAllValues()).contains(12345L, 12345L);
    }

    private void assertResultReported(String expectedName, int scenarioNumber, boolean expectedResult,
            TestStatus expectedTestStatus) {
        int index = scenarioCaptor.getAllValues().indexOf(scenarioNumber);
        assertThat(scenarioCaptor.getAllValues()).contains(scenarioNumber, Index.atIndex(index));
        assertThat(nameCaptor.getAllValues()).contains(expectedName, Index.atIndex(index));
        assertThat(testResultCaptor.getAllValues()).contains(expectedTestStatus, Index.atIndex(index));
    }


    private void captureTestResultValues() {
        verify(scoreService, atLeastOnce()).testResult(
                nameCaptor.capture(), scenarioCaptor.capture(), testResultCaptor.capture(),
                timeStampCaptor.capture());
    }

    private void setupRequest(String name, String... scenarioResults) {
        request.addParameter("name", name);

        for (int i = 0; i < scenarioResults.length; i++) {
            request.addParameter("scenario" + (i + 1), scenarioResults[i]);
        }
    }
}
