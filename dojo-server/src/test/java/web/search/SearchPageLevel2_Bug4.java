package web.search;


import org.automation.dojo.web.bugs.AddExistingItemWithPriceMoreThanEntered;
import org.automation.dojo.web.bugs.IgnorePriceSortingOrderBug;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.PriceSortingAscDescLevel2Scenario;
import org.automation.dojo.web.scenario.SearchByPriceLevel2Scenario;
import org.automation.dojo.web.scenario.SearchByTextLevel2Scenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.automation.dojo.web.model.ShopService.LESS_THAN;
import static org.automation.dojo.web.model.ShopService.MORE_THAN;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchPageLevel2_Bug4 extends SearchPageLevel2 {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextLevel2Scenario.class, NullBug.class,
                SearchByPriceLevel2Scenario.class, NullBug.class,
                PriceSortingAscDescLevel2Scenario.class, IgnorePriceSortingOrderBug.class);
    }

    @Test
    public void shouldSavePreviousSortingOrderWhen() {
        enterText("some device");
        enterPrice(MORE_THAN, 111);
        submitSearchForm();
        assertSortingOrder(ASC);

        selectSortingOrder(DESC);
        submitSearchForm();

        assertSortingOrder(ASC); // это баг делает

        selectSortingOrder(ASC);
        submitSearchForm();

        assertSortingOrder(ASC);
    }
}
