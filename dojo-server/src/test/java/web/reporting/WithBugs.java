package web.reporting;

import org.automation.dojo.web.bugs.FoundNotExistsProductBug;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class WithBugs extends AbstractBugReporting {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, FoundNotExistsProductBug.class);
    }

    @Test
    public void exceptionInTestCase() throws IOException {
        client.setResult(EXCEPTION, 1);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - failed: Scores for bug #");
        assertThat(response).contains("You get +100 points");
    }

    @Test
    public void noBugsReported() throws IOException {
        client.setResult(PASS, 1);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - failed: Fix the test! It shows wrong result. Current scenario contains bug. You get -20 points");
    }

    @Test
    public void reportBug() throws IOException {
        client.setResult(FAIL, 1);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - failed: Scores for bug #");
        assertThat(response).contains("You get +100 points");
    }

    @Test
    public void alreadyReportedBug() throws IOException {
        client.setResult(FAIL, 1);
        client.sendResultsToServer();

        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - failed: Bug #");
        assertThat(response).contains("already reported for this Minor Release");

    }

    @Test
    public void liar() throws IOException {
        client.setResult(FAIL, 1);
        client.sendResultsToServer();
        turnBugsOff();
        turnBugsOn();

        client.setResult(PASS, 1);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - failed: Liar! Current scenario contains bug. Previously reported bug #");
        assertThat(response).contains("You get -40 points");
        // TODO toSerg Я вот второго типа лжеца так и не смог найти, когда баги нет в момент рипорта см. тест liar в BugFree
    }

    @Test
    public void failAfterSwitch() throws IOException {
        client.setResult(FAIL, 1);
        client.sendResultsToServer();
        turnBugsOff();
        turnBugsOn();

        client.setResult(FAIL, 1);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - failed: Scores for bug #");
        assertThat(response).contains("You get +50 points");
    }

    @Test
    public void stillFailButLiar() throws IOException {
        client.setResult(FAIL, 1);
        client.sendResultsToServer();
        turnBugsOff();

        client.setResult(FAIL, 1);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - passed: Fix the test! It shows wrong result. Current scenario is bugs free. You get -20 points");
    }

    @Test
    public void oneTestFailAnotherPass() throws IOException {
        client.addResult(FAIL, 1);
        client.addResult(PASS, 1);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - failed: Scores for bug #");
        assertThat(response).contains("You get +100 points");
    }
}