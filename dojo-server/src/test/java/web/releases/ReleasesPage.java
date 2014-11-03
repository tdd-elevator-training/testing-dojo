package web.releases;


import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.controllers.Admin;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import web.FunctionalTestCase;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

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
        assertEquals("Now we have major 1 and minor " +
                "[Scenario SearchByTextScenario with bug NullBug]",
                indicator.getText());
    }

    @Test
    public void shouldSwitchMajorWhenClick() {
        goTo(nextMajor.getAttribute("href"));

        assertEquals("Now we have major 2 and minor " +
                "[Scenario SearchByTextScenario with bug NullBug, " +
                "Scenario SearchByPriceScenario with bug NullBug, " +
                "Scenario PriceSortingAscDescScenario with bug NullBug]",
                indicator.getText());
    }

    @Test
    public void shouldSwitchMinorWhenClickForMajor0() {
        when(dice.next(anyInt())).thenReturn(0);

        goTo(nextMinor.getAttribute("href"));

        assertEquals("Now we have major 1 and minor " +
                "[Scenario SearchByTextScenario with bug NoResultWhenExpectedBug]",
                indicator.getText());
    }

    @Test
    public void shouldSwitchMinorWhenClickForMajor1() {
        when(dice.next(anyInt())).thenReturn(0);

        goTo(nextMajor.getAttribute("href"));
        goTo(nextMinor.getAttribute("href"));

        assertEquals("Now we have major 2 and minor " +
                        "[Scenario SearchByTextScenario with bug NoResultWhenExpectedBug, " +
                        "Scenario SearchByPriceScenario with bug AddExistingItemWithPriceLessThanEnteredBug, " +
                        "Scenario PriceSortingAscDescScenario with bug BrokenSortingBug]",
                indicator.getText());
    }

    @Test
    public void shouldStopIfNoMoreMajorRevisions() {
        assertEquals("Now we have major 1 and minor " +
                "[Scenario SearchByTextScenario with bug NullBug]",
                indicator.getText());

        goTo(nextMajor.getAttribute("href"));

        assertEquals("Now we have major 2 and minor " +
                "[Scenario SearchByTextScenario with bug NullBug, " +
                "Scenario SearchByPriceScenario with bug NullBug, " +
                "Scenario PriceSortingAscDescScenario with bug NullBug]",
                indicator.getText());

        goTo(nextMajor.getAttribute("href"));

        assertEquals("Now we have major 3 and minor " +
                "[Scenario SearchByTextScenario with bug NullBug, " +
                "Scenario SearchByPriceScenario with bug NullBug, " +
                "Scenario PriceSortingAscDescScenario with bug NullBug, " +
                "Scenario AddToUserCartScenario with bug NullBug, " +
                "Scenario CalculateCartSumScenario with bug NullBug]",
                indicator.getText());

        goTo(nextMajor.getAttribute("href"));
        goTo(nextMajor.getAttribute("href"));

        assertEquals("Now we have major 3 and minor " +
                "[Scenario SearchByTextScenario with bug NullBug, " +
                "Scenario SearchByPriceScenario with bug NullBug, " +
                "Scenario PriceSortingAscDescScenario with bug NullBug, " +
                "Scenario AddToUserCartScenario with bug NullBug, " +
                "Scenario CalculateCartSumScenario with bug NullBug]",
                indicator.getText());
    }

}
