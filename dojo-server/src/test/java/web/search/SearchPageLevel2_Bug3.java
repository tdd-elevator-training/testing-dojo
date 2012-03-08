package web.search;


import org.automation.dojo.web.bugs.AddExistingItemWithPriceLessThanEntered;
import org.automation.dojo.web.bugs.AddExistingItemWithPriceMoreThanEntered;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.PriceSortingAscDescLevel2Scenario;
import org.automation.dojo.web.scenario.SearchByPriceLevel2Scenario;
import org.automation.dojo.web.scenario.SearchByTextLevel2Scenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Null;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import web.FunctionalTestCase;

import java.util.Arrays;
import java.util.List;

import static org.automation.dojo.web.model.ShopService.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchPageLevel2_Bug3 extends SearchPageLevel2 {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextLevel2Scenario.class, NullBug.class,
                SearchByPriceLevel2Scenario.class, AddExistingItemWithPriceMoreThanEntered.class,
                PriceSortingAscDescLevel2Scenario.class, NullBug.class);
    }

    @Test
    public void shouldOnlyElementsLessThanSomePrice() {
        enterText("");
        enterPrice(LESS_THAN, 120);
        submitSearchForm();

        assertPageContain("'Mouse 1' 30.0$");
        assertPageContain("'Mouse 3' 40.0$");
        assertPageContain("'Mouse 2' 50.0$");
        assertPageContain("'Mouse 4 - the best mouse!' 66.0$");
        assertPageContain("'Monitor 2' 120.0$");
        assertPageContain("'Monitor 1' 150.0$");  // это баг делает
        assertPageNotContain("'Monitor 3 - the best monitor!' 190.0$");
    }
}
