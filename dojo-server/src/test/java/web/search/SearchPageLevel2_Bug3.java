package web.search;


import org.automation.dojo.web.bugs.AddExistingItemWithPriceMoreThanEnteredBug;
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

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchPageLevel2_Bug3 extends SearchPageLevel2 {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class,
                SearchByPriceScenario.class, AddExistingItemWithPriceMoreThanEnteredBug.class,
                PriceSortingAscDescScenario.class, NullBug.class);
    }

    @Override
    @Test
    public void shouldOnlyElementsLessThanSomePrice() {
        try  {
            super.shouldOnlyElementsLessThanSomePrice();
            fail();
        } catch (ComparisonFailure error) {
            assertEquals("['Mouse 1' 30.0$, " +
                    "'Mouse 3' 40.0$, " +
                    "'Mouse 2' 50.0$, " +
                    "'Mouse 4 - the best mouse!' 66.0$, " +
                    "'Monitor 2' 120.0$, " +
                    "'Monitor 1' 150.0$]", // это баг делает
                    error.getActual());

            assertEquals("['Mouse 1' 30.0$, " +
                    "'Mouse 3' 40.0$, " +
                    "'Mouse 2' 50.0$, " +
                    "'Mouse 4 - the best mouse!' 66.0$, " +
                    "'Monitor 2' 120.0$]",
                    error.getExpected());
        }
    }
}
