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
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DojoTestRunner extends Runner implements Filterable {
    private BlockJUnit4ClassRunner runner;
    private final String server;
    private final String userName;


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
        notifier.addListener(new TestListener(server, userName));
        runner.run(notifier);
    }

    @Override
    public int testCount() {
        return runner.testCount();
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        runner.filter(filter);
    }

    private static class TestListener extends RunListener {

        private String server;
        private String userName;
        private DefaultHttpClient httpClient;
        private Map<Description, Failure> testFailures = new HashMap<Description, Failure>();


        public TestListener(String server, String userName) {
            this.server = server;
            this.userName = userName;
            httpClient = new DefaultHttpClient();
        }

        @Override
        public void testFailure(Failure failure) throws Exception {
            testFailures.put(failure.getDescription(), failure);
        }

        @Override
        public void testFinished(Description description) throws Exception {
            Scenario scenario = description.getAnnotation(Scenario.class);
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("scenario" + scenario.value(),
                    testFailures.containsKey(description) ? "failed" : "passed"));
            formparams.add(new BasicNameValuePair("name", userName));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            HttpPost post = new HttpPost(server + "/result");
            post.setEntity(entity);
            BasicResponseHandler responseHandler = new BasicResponseHandler();

            httpClient.execute(post, responseHandler);
        }

    }
}
