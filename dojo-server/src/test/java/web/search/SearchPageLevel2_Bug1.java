package web.search;


import org.automation.dojo.web.bugs.AddExistingItemWithPriceLessThanEntered;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.PriceSortingAscDescScenario;
import org.automation.dojo.web.scenario.SearchByPriceScenario;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.automation.dojo.web.model.ShopService.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchPageLevel2_Bug1 extends SearchPageLevel2 {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class,
                SearchByPriceScenario.class, AddExistingItemWithPriceLessThanEntered.class,
                PriceSortingAscDescScenario.class, NullBug.class);
    }

    @Test
    public void shouldOnlyElementsMoreThanSomePrice() {
        enterText("");
        enterPrice(MORE_THAN, 120);
        submitSearchForm();

        assertPageContain("'Mouse 1' 30.0$"); // это баг делает
        assertPageNotContain("'Mouse 3' 40.0$");
        assertPageNotContain("'Mouse 2' 50.0$");
        assertPageNotContain("'Mouse 4 - the best mouse!' 66.0$");
        assertPageContain("'Monitor 2' 120.0$");
        assertPageContain("'Monitor 1' 150.0$");
        assertPageContain("'Monitor 3 - the best monitor!' 190.0$");
    }
}
