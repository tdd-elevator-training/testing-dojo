package org.automation.dojo;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.runner.notification.Failure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DojoTestClient {
    private final String server;
    private final String userName;
    private DefaultHttpClient httpClient;
    private List<NameValuePair> scenariosResults = new ArrayList<NameValuePair>();
    private String result;

    public DojoTestClient(String server, String userName) {
        this.server = server;
        this.userName = userName;
    }

    public String sendResultsToServer() throws IOException {
        if (scenariosResults.isEmpty()) {
            return "";
        }
        httpClient = new DefaultHttpClient();
        scenariosResults.add(new BasicNameValuePair("name", userName));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(scenariosResults, "UTF-8");
        HttpPost post = new HttpPost(server + "/result");
        post.setEntity(entity);
        BasicResponseHandler responseHandler = new BasicResponseHandler();
        String execute = httpClient.execute(post, responseHandler);
        return execute;
    }

    public void setResult(Failure failure, int scenario) {
        scenariosResults.clear();
        addResult(failure, scenario);
    }

    public void addResult(Failure failure, int scenario) {
        String result = "passed";
        if (failure != null) {
            result = (failure.getException() instanceof AssertionError) ? "failed" : "exception";
        }
        scenariosResults.add(new BasicNameValuePair("scenario" + scenario, result));
    }

    public String getResult() {
        return scenariosResults.toString();
    }
}
