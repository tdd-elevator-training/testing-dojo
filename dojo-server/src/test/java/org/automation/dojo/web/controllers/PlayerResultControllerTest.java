package org.automation.dojo.web.controllers;

import org.automation.dojo.ScoreService;
import org.automation.dojo.TestResult;
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

    @Mock ScoreService service;
    @Captor ArgumentCaptor<Integer> scenarioCaptor;
    @Captor ArgumentCaptor<TestResult> testResultCaptor;
    @Captor ArgumentCaptor<String> nameCaptor;

    @Before
    public void setUp() throws Exception {
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        controller = new PlayerResultController(service);
    }


    @Test
    public void shouldReturnSucceedWhenScenarioPassed() throws IOException, ServletException {
        when(service.testResult(anyString(), anyInt(), Matchers.<TestResult>anyObject())).thenReturn(true);
        request.addParameter("scenario1", "passed");

        controller.service(request, response);

        assertEquals("scenario1=passed", response.getContentAsString().trim());
    }

    @Test
    public void shouldReportScenarioResultWhenPassed() throws IOException, ServletException {
        setupRequest("vasya", "passed");

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported("vasya", 1, true, TestResult.PASSED);
    }

    @Test
    public void shouldReportScenarioResultWhenFailed() throws IOException, ServletException {
        setupRequest("petya", "failed");

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported("petya", 1, false, TestResult.FAILED);
    }

    @Test
    public void shouldReportSeveralScenarioResultWhenSeveralReported() throws IOException, ServletException {
        setupRequest("petya", "passed", "failed");

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported("petya", 1, true, TestResult.PASSED);
        assertResultReported("petya", 2, false, TestResult.FAILED);
    }

    @Test
    public void shouldReportExceptionFailure() throws IOException, ServletException {
        setupRequest("petya", "exception");

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported("petya", 1, false, TestResult.EXCEPTION);
    }

    @Test
    public void shouldHaveServiceActualResultsWhenReported() throws IOException, ServletException {
        when(service.testResult("masha", 1, TestResult.PASSED)).thenReturn(false);
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
        assertResultReported(null, 5, true, TestResult.PASSED);
        assertResultReported(null, 11, false, TestResult.FAILED);
    }

    @Test
    public void shouldReportFailureWhenOneOfScenarioResultsFailed() throws IOException, ServletException {
        request.addParameter("scenario1", "passed");
        request.addParameter("scenario1", "failed");

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported(null, 1, false, TestResult.FAILED);
    }

    @Test
    public void shouldReportExceptionWhenOneOfScenarioResultsException() throws IOException, ServletException {
        request.addParameter("scenario1", "passed");
        request.addParameter("scenario1", "failed");
        request.addParameter("scenario1", "exception");

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported(null, 1, false, TestResult.EXCEPTION);
    }

    @Test
    public void shouldReportFromRobotFramework() throws IOException, ServletException {
        request.addParameter("scenario1", "FAIL");

        controller.service(request, response);

        captureTestResultValues();
        assertResultReported(null, 1, true, TestResult.FAILED);
    }

    private void assertResultReported(String expectedName, int scenarioNumber, boolean expectedResult,
            TestResult expectedTestResult) {
        int index = scenarioCaptor.getAllValues().indexOf(scenarioNumber);
        assertThat(scenarioCaptor.getAllValues()).contains(scenarioNumber, Index.atIndex(index));
        assertThat(nameCaptor.getAllValues()).contains(expectedName, Index.atIndex(index));
        assertThat(testResultCaptor.getAllValues()).contains(expectedTestResult, Index.atIndex(index));
    }


    private void captureTestResultValues() {
        verify(service, atLeastOnce()).testResult(
                nameCaptor.capture(), scenarioCaptor.capture(), testResultCaptor.capture());
    }

    private void setupRequest(String name, String... scenarioResults) {
        request.addParameter("name", name);

        for (int i = 0; i < scenarioResults.length; i++) {
            request.addParameter("scenario" + (i + 1), scenarioResults[i]);
        }
    }
}
