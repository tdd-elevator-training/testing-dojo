package web.reporting;

import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class BugFree extends AbstractBugReporting {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class);
    }

    @Test
    public void exceptionInTestCase() throws IOException {
        client.setResult(EXCEPTION, 1);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - passed: Fix the test! It shows wrong result. Current scenario is bugs free. You get -20 points");
    }

    @Test
    public void noBugsReported() throws IOException {
        client.setResult(PASS, 1);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - passed: Good! No bugs reported for bugs free scenario");
    }

    @Test
    public void reportBug() throws IOException {
        client.setResult(FAIL, 1);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - passed: Fix the test! It shows wrong result. Current scenario is bugs free. You get -20 points");
    }

    @Test
    public void alreadyReportedBug() throws IOException {
        client.setResult(PASS, 1);
        client.sendResultsToServer();

        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - passed: Good! No bugs reported for bugs free scenario");
    }

    @Test
    public void liar() throws IOException {
        client.setResult(PASS, 1);
        client.sendResultsToServer();
        turnBugsOn();
        turnBugsOff();

        client.setResult(FAIL, 1);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - passed: Fix the test! It shows wrong result. Current scenario is bugs free. You get -20 points");
        // TODO toSerg тут ожидал liar
    }

    @Test
    public void passAfterSwitch() throws IOException {
        client.setResult(PASS, 1);
        client.sendResultsToServer();
        turnBugsOn();
        turnBugsOff();

        client.setResult(PASS, 1);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - passed: Good! No bugs reported for bugs free scenario");
    }

    @Test
    public void stillPassButLiar() throws IOException {
        client.setResult(PASS, 1);
        client.sendResultsToServer();
        turnBugsOn();

        client.setResult(PASS, 1);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - failed: Fix the test! It shows wrong result. Current scenario contains bug. You get -20 points");
    }

    @Test
    public void oneTestFailAnotherPass() throws IOException {
        client.addResult(FAIL, 1);
        client.addResult(PASS, 1);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - passed: Fix the test! It shows wrong result. Current scenario is bugs free. You get -20 points");
    }
}