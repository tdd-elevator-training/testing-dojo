package web.releases;


import org.automation.dojo.web.controllers.Admin;
import web.FunctionalTestCase;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReleasesPage extends FunctionalTestCase {

    private WebElement nextMajor;
    private WebElement nextMinor;
    private WebElement indicator;

    @Override
    protected void resetAllElements() {
        nextMajor = tester.findElement(By.linkText("Next major"));
        nextMinor = tester.findElement(By.linkText("Next minor"));
        indicator = tester.findElement(By.id("indicator"));
    }

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class);
    }

    @Override
    protected int getMajorRelease() {
        return 0;
    }

    @Override
    protected String getPageUrl() {
        return "/" + Admin.ADMIN_PAGE;
    }

    @Test
    public void shouldShowTwoLinksWhenFirstLoad() {
        assertNotNull(nextMajor);
        assertNotNull(nextMinor);
    }

    @Test
    public void shouldMajorRelease0WithoutBugsAtStart(){
        assertMach("Now we have major 1 and minor " +
                "\\[Scenario SearchByTextScenario with bug NullBug\\]",
                indicator.getText());
    }

    @Test
    public void shouldSwitchMajorWhenClick() {
        goTo(nextMajor.getAttribute("href"));

        assertMach("Now we have major 2 and minor " +
                "\\[Scenario SearchByTextScenario with bug NullBug, " +
                "Scenario SearchByPriceScenario with bug NullBug, " +
                "Scenario PriceSortingAscDescScenario with bug NullBug\\]",
                indicator.getText());
    }

    @Test
    public void shouldSwitchMinorWhenClickForMajor0() {
        goTo(nextMinor.getAttribute("href"));

        assertMach("Now we have major 1 and minor " +
                "\\[Scenario SearchByTextScenario with bug .*\\]",
                indicator.getText());
    }

    @Test
    public void shouldSwitchMinorWhenClickForMajor1() {
        goTo(nextMajor.getAttribute("href"));
        goTo(nextMinor.getAttribute("href"));

        assertMach("Now we have major 2 and minor " +
                "\\[Scenario SearchByTextScenario with bug .*" +
                "Scenario SearchByPriceScenario with bug .*, " +
                "Scenario PriceSortingAscDescScenario with bug .*\\]",
                indicator.getText());
    }

    @Test
    public void shouldStopIfNoMoreMajorRevisions() {
        assertMach("Now we have major 1 and minor " +
                "\\[Scenario SearchByTextScenario with bug NullBug\\]",
                indicator.getText());

        goTo(nextMajor.getAttribute("href"));

        assertMach("Now we have major 2 and minor " +
                "\\[Scenario SearchByTextScenario with bug NullBug, " +
                "Scenario SearchByPriceScenario with bug NullBug, " +
                "Scenario PriceSortingAscDescScenario with bug NullBug\\]",
                indicator.getText());

        goTo(nextMajor.getAttribute("href"));

        assertMach("Now we have major 3 and minor " +
                "\\[Scenario SearchByTextScenario with bug NullBug, " +
                "Scenario SearchByPriceScenario with bug NullBug, " +
                "Scenario PriceSortingAscDescScenario with bug NullBug, " +
                "Scenario AddToUserCartScenario with bug NullBug, " +
                "Scenario CalculateCartSumScenario with bug NullBug\\]",
                indicator.getText());

        goTo(nextMajor.getAttribute("href"));
        goTo(nextMajor.getAttribute("href"));

        assertMach("Now we have major 3 and minor " +
                "\\[Scenario SearchByTextScenario with bug NullBug, " +
                "Scenario SearchByPriceScenario with bug NullBug, " +
                "Scenario PriceSortingAscDescScenario with bug NullBug, " +
                "Scenario AddToUserCartScenario with bug NullBug, " +
                "Scenario CalculateCartSumScenario with bug NullBug\\]",
                indicator.getText());
    }

    private void assertMach(String regexp, String text) {
        assertTrue(String.format("\nExpected regexp '%s'\n" +
                                 "        but was '%s'", regexp, text),
                Pattern.compile(regexp).matcher(text).matches());
    }
}
