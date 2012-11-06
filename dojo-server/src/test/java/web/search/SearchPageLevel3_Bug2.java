package web.search;


import org.automation.dojo.web.bugs.BrokenChartSumBug;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.*;
import org.junit.ComparisonFailure;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SearchPageLevel3_Bug2 extends SearchPageLevel3 {

    @Override
    protected int getMajorRelease() {
        return 2;
    }

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class,
                SearchByPriceScenario.class, NullBug.class,
                PriceSortingAscDescScenario.class, NullBug.class,
                AddToUserCartScenario.class, NullBug.class,
                CalculateCartSumScenario.class, BrokenChartSumBug.class);
    }

    @Test
    public void shouldCalculateTotalPriceCorrectly(){
        try  {
            super.shouldCalculateTotalPriceCorrectly();
            fail();
        } catch (ComparisonFailure error) {
            assertEquals("100646.0$", error.getActual());// это баг длает
            assertEquals("146.0$", error.getExpected());
        }
    }
}