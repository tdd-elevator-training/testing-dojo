package org.automation.dojo.web.controllers;

import org.automation.dojo.*;
import org.automation.dojo.web.scenario.BasicScenario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlayerResultControllerTest {

    private MockHttpServletResponse response;
    private MockHttpServletRequest request;
    private PlayerResultController controller;

    @Mock ScoreService scoreService;
    @Mock ReleaseEngine releaseEngine;
    @Mock TimeService timeService;

    @Captor ArgumentCaptor<Integer> scenarioCaptor;
    @Captor ArgumentCaptor<TestStatus> testResultCaptor;
    @Captor ArgumentCaptor<String> nameCaptor;
    @Captor ArgumentCaptor<Long> timeStampCaptor;

    @Captor ArgumentCaptor<TestSuiteResult> suiteCaptor;

    @Before
    public void setUp() throws Exception {
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        when(timeService.now()).thenReturn(new Date());
        controller = new PlayerResultController(scoreService, timeService, releaseEngine);
    }


    @Test
    public void shouldReturnSucceedWhenScenarioPassed() throws IOException, ServletException {
        Collection<PlayerRecord> scenariosState = scenariosState(1, true);
        when(scoreService.suiteResult(any(TestSuiteResult.class))).thenReturn(scenariosState);
        request.addParameter("scenario1", "passed");

        controller.service(request, response);

        assertThat(response.getContentAsString()).contains("Scenario #1 - passed");
    }

    class MockPlayerRecord extends PlayerRecord  {
        public MockPlayerRecord(int scenarioId, boolean passed, String message) {
            super(null, Mockito.mock(BasicScenario.class), passed, 0, message, Type.PASSED);
            when(scenario.bugsFree()).thenReturn(passed);
            when(scenario.getId()).thenReturn(scenarioId);
        }
    }

    private Collection<PlayerRecord> scenariosState(int scenarioId, boolean passed) {
        Collection<PlayerRecord>  result = new LinkedList<PlayerRecord>();
        result.add(new MockPlayerRecord(scenarioId, passed, "Some message"));
        return result;
    }

    @Test
    public void shouldReportScenarioResultWhenPassed() throws IOException, ServletException {
        setupRequest("vasya", "passed");

        controller.service(request, response);

        captureTestResultValues();
        assertLastResultReported("vasya", 1, true, TestStatus.PASSED);
    }

    @Test
    public void shouldReportScenarioResultWhenFailed() throws IOException, ServletException {
        setupRequest("petya", "failed");

        controller.service(request, response);

        captureTestResultValues();
        assertLastResultReported("petya", 1, false, TestStatus.FAILED);
    }

    @Test
    public void shouldReportSeveralScenarioResultWhenSeveralReported() throws IOException, ServletException {
        setupRequest("petya", "passed", "failed");

        controller.service(request, response);

        captureTestResultValues();
        assertLastResultReported("petya", 1, true, TestStatus.PASSED);
        assertLastResultReported("petya", 2, false, TestStatus.FAILED);
    }

    @Test
    public void shouldReportExceptionFailure() throws IOException, ServletException {
        setupRequest("petya", "exception");

        controller.service(request, response);

        captureTestResultValues();
        assertLastResultReported("petya", 1, false, TestStatus.EXCEPTION);
    }

    @Test
    public void shouldHaveServiceActualResultsWhenReported() throws IOException, ServletException {
        Collection<PlayerRecord> playerRecords = scenariosState(1, false);
        when(scoreService.suiteResult(any(TestSuiteResult.class))).thenReturn(playerRecords);
        setupRequest("masha", "passed");

        controller.service(request, response);

        captureTestResultValues();
        assertThat(response.getContentAsString()).contains("Scenario #1 - failed");
    }

    @Test
    public void shouldReportProperlyWhenNotStandardRequestParamsReceived() throws IOException, ServletException {
        request.addParameter("scenario5", "true");
        request.addParameter("scenario11", "false");

        controller.service(request, response);

        captureTestResultValues();
        assertLastResultReported(null, 5, true, TestStatus.PASSED);
        assertLastResultReported(null, 11, false, TestStatus.FAILED);
    }

    @Test
    public void shouldReportFailureWhenOneOfScenarioResultsFailed() throws IOException, ServletException {
        request.addParameter("scenario1", "passed");
        request.addParameter("scenario1", "failed");

        controller.service(request, response);

        captureTestResultValues();
        assertLastResultReported(null, 1, false, TestStatus.FAILED);
    }

    @Test
    public void shouldReportExceptionWhenOneOfScenarioResultsException() throws IOException, ServletException {
        request.addParameter("scenario1", "passed");
        request.addParameter("scenario1", "failed");
        request.addParameter("scenario1", "exception");

        controller.service(request, response);

        captureTestResultValues();
        assertLastResultReported(null, 1, false, TestStatus.EXCEPTION);
    }

    @Test
    public void shouldReportFromRobotFramework() throws IOException, ServletException {
        request.addParameter("scenario1", "FAIL");

        controller.service(request, response);

        captureTestResultValues();
        assertLastResultReported(null, 1, true, TestStatus.FAILED);
    }


    @Test
    public void shouldSendTimeWhenReportSeveralResults() throws IOException, ServletException {
        setupRequest("vasya", "false", "true");
        when(timeService.now()).thenReturn(new Date(12345L));

        controller.service(request, response);

        captureTestResultValues();
        TestSuiteResult suite = suiteCaptor.getValue();
        assertThat(suite.getTimestamp()).isEqualTo(12345L);
        Map<Integer,List<TestStatus>> scenarioResults = suite.getScenarioResults();
        assertEquals(2, scenarioResults.size());
    }

    @Test
    public void shouldReturnScenarioStatusesForSeveralScenarios() throws IOException, ServletException {
        setupRequest("vasya", "false", "true");
        Collection<PlayerRecord> scenarioStates = scenariosState(1, true);
        scenarioStates.add(new MockPlayerRecord(2, false, "Some other message"));
        when(scoreService.suiteResult(Matchers.<TestSuiteResult>anyObject())).thenReturn(scenarioStates);

        controller.service(request, response);

        assertThat(response.getContentAsString()).contains("Scenario #1 - passed");
        assertThat(response.getContentAsString()).contains("Scenario #2 - failed");
    }

    private void assertLastResultReported(String expectedName, int scenarioNumber, boolean expectedResult,
                                          TestStatus expectedTestStatus) {
        TestSuiteResult suite = suiteCaptor.getValue();
        assertEquals(expectedName, suite.getPlayerName());
        List<TestStatus> testStatuses = suite.getScenarioResults().get(scenarioNumber);
        assertEquals(expectedTestStatus, testStatuses.get(testStatuses.size() - 1));
    }


    private void captureTestResultValues() {
        verify(scoreService, atLeastOnce()).suiteResult(suiteCaptor.capture());
    }

    private void setupRequest(String name, String... scenarioResults) {
        request.addParameter("name", name);

        for (int i = 0; i < scenarioResults.length; i++) {
            request.addParameter("scenario" + (i + 1), scenarioResults[i]);
        }
    }
}
