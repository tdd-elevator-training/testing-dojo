package web.reporting;

import org.automation.dojo.web.bugs.AddExistingItemWithPriceLessThanEnteredBug;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.PriceSortingAscDescScenario;
import org.automation.dojo.web.scenario.SearchByPriceScenario;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class TwoScenarios extends AbstractBugReporting {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class,
                SearchByPriceScenario.class, AddExistingItemWithPriceLessThanEnteredBug.class,
                PriceSortingAscDescScenario.class, NullBug.class);
    }

    @Override
    protected int getMajorRelease() {
        return 1;
    }

    @Test
    public void goodReporting() throws IOException {
        client.addResult(PASS, 1);
        client.addResult(PASS, 1);
        client.addResult(PASS, 2);
        client.addResult(FAIL, 2);
        client.addResult(PASS, 3);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - passed: Good! No bugs reported for bugs free scenario");

        assertThat(response).contains("Scenario #2 - failed: Scores for bug #");
        assertThat(response).contains("You get +100 points");

        assertThat(response).contains("Scenario #3 - passed: Good! No bugs reported for bugs free scenario");
    }

    @Test
    public void badReporting() throws IOException {
        client.addResult(FAIL, 1);
        client.addResult(FAIL, 2);
        client.addResult(FAIL, 3);
        client.addResult(PASS, 1);
        client.addResult(PASS, 2);
        client.addResult(PASS, 3);
        String response = client.sendResultsToServer();
        assertThat(response).contains("Scenario #1 - passed: Fix the test! It shows wrong result. Current scenario is bugs free. You get -20 points");

        assertThat(response).contains("Scenario #2 - failed: Scores for bug #");
        assertThat(response).contains("You get +100 points");

        assertThat(response).contains("Scenario #3 - passed: Fix the test! It shows wrong result. Current scenario is bugs free. You get -20 points");
    }
}