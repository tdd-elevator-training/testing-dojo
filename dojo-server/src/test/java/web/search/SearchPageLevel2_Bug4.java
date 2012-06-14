package web.search;


import org.automation.dojo.web.bugs.IgnorePriceSortingOrderBug;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.PriceSortingAscDescScenario;
import org.automation.dojo.web.scenario.SearchByPriceScenario;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.automation.dojo.web.model.ShopService.MORE_THAN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SearchPageLevel2_Bug4 extends SearchPageLevel2 {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class,
                SearchByPriceScenario.class, NullBug.class,
                PriceSortingAscDescScenario.class, IgnorePriceSortingOrderBug.class);
    }

    @Override
    @Test
    public void shouldSavePreviousSortingOrderWhen() {
        enterText("some device");
        enterPrice(MORE_THAN, 111);
        search();
        isSortingOrder(ASC);

        selectSortingOrder(DESC);

        isSortingOrder(ASC); // это баг делает

        selectSortingOrder(ASC);

        isSortingOrder(ASC);
    }

    @Override
    @Test
    public void shouldFoundElementsSortedByPriceIfDesc() {
        try  {
            super.shouldFoundElementsSortedByPriceIfDesc();
            fail();
        } catch (ComparisonFailure error) {
            assertEquals("['Mouse 4 - the best mouse!' 66.0$, " +
                    "'Monitor 3 - the best monitor!' 190.0$]", // это баг делает
                    error.getActual());

            assertEquals("['Monitor 3 - the best monitor!' 190.0$, " +
                    "'Mouse 4 - the best mouse!' 66.0$]",
                    error.getExpected());
        }
    }
}
