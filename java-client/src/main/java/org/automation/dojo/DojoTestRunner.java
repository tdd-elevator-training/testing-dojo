package org.automation.dojo;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DojoTestRunner extends Runner implements Filterable {
    private BlockJUnit4ClassRunner runner;
    private final String server;
    private final String userName;
    private List<NameValuePair> scenariosResults = new ArrayList<NameValuePair>();
    private DefaultHttpClient httpClient;

    public DojoTestRunner(Class<?> klass) throws InitializationError {
        runner = new BlockJUnit4ClassRunner(klass);
        ReportTo annotation = klass.getAnnotation(ReportTo.class);
        if (annotation == null) {
            throw new InitializationError("Annotation @" + ReportTo.class.getSimpleName() +
                    " should be defined at a class level");
        }
        server = annotation.server();
        userName = annotation.userName();
    }

    @Override
    public Description getDescription() {
        return runner.getDescription();
    }

    @Override
    public void run(RunNotifier notifier) {
        httpClient = new DefaultHttpClient();
        TestListener listener = new TestListener();
        notifier.addListener(listener);

        runner.run(notifier);

        try {
            sendResultsToServer();
        } catch (IOException e) {
            reportSendFailure(listener, e);
        }
    }

    private void reportSendFailure(TestListener listener, IOException e) {
        try {
            listener.testFailure(new Failure(getDescription(), e));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void sendResultsToServer() throws IOException {
        if (scenariosResults.isEmpty()) {
            return;
        }
        UrlEncodedFormEntity entity = null;
        scenariosResults.add(new BasicNameValuePair("name", userName));
        entity = new UrlEncodedFormEntity(scenariosResults, "UTF-8");
        HttpPost post = new HttpPost(server + "/result");
        post.setEntity(entity);
        BasicResponseHandler responseHandler = new BasicResponseHandler();
        httpClient.execute(post, responseHandler);
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
            String result = "passed";
            Failure failure = testFailures.get(description);
            if (failure != null) {
                result = (failure.getException() instanceof AssertionError) ? "failed" : "exception";
            }
            scenariosResults.add(new BasicNameValuePair("scenario" + scenario.value(), result));
        }
    }
}
