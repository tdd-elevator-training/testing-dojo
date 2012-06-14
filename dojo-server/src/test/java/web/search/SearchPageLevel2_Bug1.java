package web.search;


import org.automation.dojo.web.bugs.AddExistingItemWithPriceLessThanEnteredBug;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class SearchPageLevel2_Bug1 extends SearchPageLevel2 {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class,
                SearchByPriceScenario.class, AddExistingItemWithPriceLessThanEnteredBug.class,
                PriceSortingAscDescScenario.class, NullBug.class);
    }

    @Test
    public void shouldOnlyElementsMoreThanSomePrice() {
        try  {
            super.shouldOnlyElementsMoreThanSomePrice();
            fail();
        } catch (ComparisonFailure error) {
            assertEquals("['Mouse 1' 30.0$, " + // это баг длает
                    "'Monitor 2' 120.0$, " +
                    "'Monitor 1' 150.0$, " +
                    "'Monitor 3 - the best monitor!' 190.0$]",
                    error.getActual());

            assertEquals("['Monitor 2' 120.0$, " +
                    "'Monitor 1' 150.0$, " +
                    "'Monitor 3 - the best monitor!' 190.0$]",
                    error.getExpected());
        }
    }
}
