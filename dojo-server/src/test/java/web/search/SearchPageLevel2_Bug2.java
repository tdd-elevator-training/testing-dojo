package web.search;


import org.automation.dojo.web.scenario.PriceSortingAscDescLevel2Scenario;
import web.FunctionalTestCase;
import org.automation.dojo.web.bugs.BrokenSortingBug;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.SearchByPriceLevel2Scenario;
import org.automation.dojo.web.scenario.SearchByTextLevel2Scenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.automation.dojo.web.model.ShopService.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchPageLevel2_Bug2 extends SearchPageLevel2 {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextLevel2Scenario.class, NullBug.class,
                SearchByPriceLevel2Scenario.class, NullBug.class,
                PriceSortingAscDescLevel2Scenario.class, BrokenSortingBug.class);
    }

    @Test(expected = AssertionError.class) // это баг делает
    public void shouldAllElementsSortedByPrice() {
        enterText("");
        submitSearchForm();

        assertPageContain("List: Description Price " +
                "'Mouse 1' 30.0$ " +
                "'Mouse 3' 40.0$ " +
                "'Mouse 2' 50.0$ " +
                "'Mouse 4 - the best mouse!' 66.0$ " +
                "'Monitor 2' 120.0$ " +
                "'Monitor 1' 150.0$ " +
                "'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test(expected = AssertionError.class) // это баг делает
    public void shouldFoundElementsSortedByPrice() {
        enterText("the best");
        submitSearchForm();

        assertPageContain("List: Description Price " +
                "'Mouse 4 - the best mouse!' 66.0$ " +
                "'Monitor 3 - the best monitor!' 190.0$");
    }
}