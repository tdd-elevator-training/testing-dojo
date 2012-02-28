package org.automation.dojo;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class TestResultRule implements MethodRule {

    public Statement apply(Statement statement, FrameworkMethod frameworkMethod, Object o) {
//        HttpClient httpclient = new DefaultHttpClient();
//        httpclient.execute();
        return null;
    }
}
