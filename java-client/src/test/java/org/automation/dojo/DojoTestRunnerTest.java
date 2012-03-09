package org.automation.dojo;

import org.fest.assertions.ListAssert;
import org.fest.assertions.StringAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

public class DojoTestRunnerTest {

    private FakeHttpServer server;
    private ArrayList<Failure> failures;

    @Before
    public void setUp() throws Exception {
        server = new FakeHttpServer(1111);
        server.start();
        failures = new ArrayList<Failure>();
    }

    @After
    public void tearDown() {
        server.stop();

    }

    @Test
    public void shouldFailToCreateRunnerWhenNoReportAnnotation() {
        try {
            new DojoTestRunner(NotAnnotatedTest.class);
            fail();
        } catch (InitializationError initializationError) {
        }
    }

    @Test
    public void shouldReportSuccessWhenPassed() throws InitializationError, IOException {
        server.setResponse("scenario1=passed");

        runTests(OneSuccessTest.class);

        assertRequestContains("scenario1=passed");
    }

    private StringAssert assertRequestContains(String expectedSubstring) {
        return assertThat(server.getRequest()).contains(expectedSubstring);
    }

    @Test
    public void shouldSendUserWhenTestRun() throws InitializationError {
        server.setResponse("scenario1=passed");

        runTests(OneSuccessTest.class);

        assertRequestContains("name=Sergey");
    }

    @Test
    public void shouldSendSeveralResultsWhenPassed() throws InitializationError {
        server.setResponse("scenario1=passed\nscenario2=passed");

        runTests(SeveralPassedTests.class);

        assertRequestContains("scenario1=passed");
        assertRequestContains("scenario2=passed");
    }

    @Test
    public void shouldSendFailuresWhenTestFails() throws InitializationError {
        server.setResponse("scenario33=failed");

        runTests(OneFailedTest.class);

        assertRequestContains("scenario33=failed");
    } 
    
    @Test
    public void shouldReportFailureWhenNoScenarioAnnotationGiven() throws InitializationError {
        runTests(NoScenarioAnnotation.class);

        assertFailuresHasException(NotAnnotatedTestException.class);
    }

    private ListAssert assertFailuresHasException(Class<NotAnnotatedTestException> exception) {
        return assertThat(failures).onProperty("exception").onProperty("class")
                .isEqualTo(Arrays.asList(exception));
    }

    private void runTests(Class<?> klass) throws InitializationError {
        DojoTestRunner runner = new DojoTestRunner(klass);
        RunNotifier notifier = new RunNotifier();
        notifier.addListener(new RunListener(){
            @Override
            public void testFailure(Failure failure) throws Exception {
                failures.add(failure);
            }

            @Override
            public void testFinished(Description description) throws Exception {
            }
        });
        runner.run(notifier);
    }
}
