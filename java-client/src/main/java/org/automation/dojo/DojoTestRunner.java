package org.automation.dojo;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.*;

public class DojoTestRunner extends Runner implements Filterable {
    private BlockJUnit4ClassRunner runner;
    private DojoTestClient dojoClient;

    public DojoTestRunner(Class<?> klass) throws InitializationError {
        runner = new BlockJUnit4ClassRunner(klass);
        ReportTo annotation = klass.getAnnotation(ReportTo.class);
        if (annotation == null) {
            throw new InitializationError("Annotation @" + ReportTo.class.getSimpleName() +
                    " should be defined at a class level");
        }
        dojoClient = new DojoTestClient(annotation.server(), annotation.userName());
    }

    @Override
    public Description getDescription() {
        return runner.getDescription();
    }

    @Override
    public void run(RunNotifier notifier) {
        TestListener listener = new TestListener();
        notifier.addListener(listener);

        runner.run(notifier);
        try {
            String response = dojoClient.sendResultsToServer();
            System.out.println("-----------------------------------------------------------------");
            System.out.println("Send: '" + dojoClient.getResult() + "'");
            System.out.println("Server answer: \n" + response);
            System.out.println("-----------------------------------------------------------------");
        } catch (IOException e) {
            reportSendFailure(listener, e);
        }
    }

    private void reportSendFailure(TestListener listener, Exception e) {
        try {
            listener.testFailure(new Failure(getDescription(), e));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public int testCount() {
        return runner.testCount();
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        runner.filter(filter);
    }

    private class TestListener extends RunListener {
        private Map<Description, Failure> testFailures = new HashMap<Description, Failure>();


        @Override
        public void testFailure(Failure failure) throws Exception {
            testFailures.put(failure.getDescription(), failure);
        }

        @Override
        public void testFinished(Description description) throws Exception {
            Scenario scenario = description.getAnnotation(Scenario.class);
            if (scenario == null) {
                return;
            }

            Failure failure = testFailures.get(description);
            dojoClient.addResult(failure, scenario.value());
        }
    }
}


