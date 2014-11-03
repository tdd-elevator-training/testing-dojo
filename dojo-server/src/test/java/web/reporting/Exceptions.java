package web.reporting;

import org.automation.dojo.DojoTestClient;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class Exceptions extends AbstractBugReporting {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class);
    }

    @Test
    public void notExistUser() throws IOException {
        client = new DojoTestClient("http://localhost:" + port + CONTEXT, "vasia");

        client.setResult(EXCEPTION, 1);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Player vasia does not exist!");
    }

    @Test
    public void notExistsScenario() throws IOException {
        client.setResult(PASS, 10);
        String response = client.sendResultsToServer();
        assertThat(response).contains("No current scenario found with id : 10");
    }
}