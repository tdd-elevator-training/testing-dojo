package web.search;


import org.automation.dojo.web.bugs.IgnorePriceSortingOrderBug;
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

import static org.automation.dojo.web.model.ShopService.MORE_THAN;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchPageLevel2_Bug4 extends SearchPageLevel2 {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class,
                SearchByPriceScenario.class, NullBug.class,
                PriceSortingAscDescScenario.class, IgnorePriceSortingOrderBug.class);
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

    @Test
    public void shouldFoundElementsSortedByPriceIfDesc() {
        enterText("the best");
        submitSearchForm();
        selectSortingOrder(DESC);
        submitSearchForm();

        assertPageContain("List: Description Price " +   // это баг длает
                "'Mouse 4 - the best mouse!' 66.0$ " +
                "'Monitor 3 - the best monitor!' 190.0$");
    }
}
