/*
package org.automation.dojo;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import org.fest.assertions.Index;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletException;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetResultServletTest {

    private MockHttpServletResponse response;
    private MockHttpServletRequest request;
    private GetResultServlet servlet;

    @Mock ScenarioService service;
    @Captor ArgumentCaptor<Integer> scenarioCaptor;
    @Captor ArgumentCaptor<Boolean> successCaptor;
    @Captor ArgumentCaptor<String> addressCaptor;
    @Captor ArgumentCaptor<String> nameCaptor;

    @Before
    public void setUp() throws Exception {
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        servlet = new GetResultServlet();
        servlet.setScenarioService(service);
    }


    @Test
    public void shouldReturnSucceedWhenScenarioPassed() throws IOException, ServletException {
        when(service.testResult(anyString(), anyString(), anyInt(), anyBoolean())).thenReturn(true);
        request.setupAddParameter("scenario1", "passed");

        servlet.doPost(request, response);

        assertEquals("scenario1=passed", response.getOutputStreamContent().trim());
    }

    @Test
    public void shouldReportScenarioResultWhenPassed() throws IOException, ServletException {
        setupRequest("vasya", "10.10.0.1", "passed");

        servlet.doPost(request, response);

        captureTestResultValues();
        assertResultReported("vasya", "10.10.0.1", 1, true);
    }

    @Test
    public void shouldReportScenarioResultWhenFailed() throws IOException, ServletException {
        setupRequest("petya", "10.10.0.2", "failed");

        servlet.doPost(request, response);

        captureTestResultValues();
        assertResultReported("petya", "10.10.0.2", 1, false);
    }

    @Test
    public void shouldReportSeveralScenarioResultWhenSeveralReported() throws IOException, ServletException {
        setupRequest("petya", "10.10.0.2", "passed", "failed");

        servlet.doPost(request, response);

        captureTestResultValues();
        assertResultReported("petya", "10.10.0.2", 1, true);
        assertResultReported("petya", "10.10.0.2", 2, false);
    }

    @Test
    public void shouldHaveServiceActualResultsWhenReported() throws IOException, ServletException {
        when(service.testResult("masha", "10.10.0.1", 1, true)).thenReturn(false);
        setupRequest("masha", "10.10.0.1", "passed");

        servlet.doPost(request, response);

        captureTestResultValues();
        assertEquals("scenario1=failed", response.getOutputStreamContent().trim());
    }

    @Test
    public void shouldReportProperlyWhenNotStandardRequestParamsReceived() throws IOException, ServletException {
        request.setupAddParameter("scenario5", "true");
        request.setupAddParameter("scenario11", "false");

        servlet.doPost(request, response);

        captureTestResultValues();
        assertResultReported(null, "127.0.0.1", 5, true);
        assertResultReported(null, "127.0.0.1", 11, false);
    }
    
    private void assertResultReported(String expectedName, String expectedAddress, int scenarioNumber, boolean expectedResult) {
        int index = scenarioCaptor.getAllValues().indexOf(scenarioNumber);
        assertThat(scenarioCaptor.getAllValues()).contains(scenarioNumber, Index.atIndex(index));
        assertThat(successCaptor.getAllValues()).contains(expectedResult, Index.atIndex(index));
        assertThat(nameCaptor.getAllValues()).contains(expectedName, Index.atIndex(index));
        assertThat(addressCaptor.getAllValues()).contains(expectedAddress, Index.atIndex(index));
    }

    private void captureTestResultValues() {
        verify(service, atLeastOnce()).testResult(
                nameCaptor.capture(), addressCaptor.capture(),
                scenarioCaptor.capture(), successCaptor.capture());
    }

    private void setupRequest(String name, String address, String ... scenarioResults) {
        request.setRemoteAddr(address);
        request.setupAddParameter("name", name);

        for (int i = 0; i < scenarioResults.length; i++) {
            request.setupAddParameter("scenario"+(i+1), scenarioResults[i]);
        }
    }
}
*/
