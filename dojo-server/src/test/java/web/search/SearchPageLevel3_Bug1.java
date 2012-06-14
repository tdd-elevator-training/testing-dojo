package web.search;


import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.bugs.SomeRecordsWillNotAddToCart;
import org.automation.dojo.web.scenario.*;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SearchPageLevel3_Bug1 extends SearchPageLevel3 {

    @Override
    protected int getMajorRelease() {
        return 2;
    }

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class,
                SearchByPriceScenario.class, NullBug.class,
                PriceSortingAscDescScenario.class, NullBug.class,
                AddToUserCartScenario.class, SomeRecordsWillNotAddToCart.class,
                CalculateCartSumScenario.class, NullBug.class);
    }

    @Test
    public void shouldAddToCartSelectedRecord(){
        try  {
            super.shouldAddToCartSelectedRecord();
            fail();
        } catch (ComparisonFailure error) {
            assertEquals("['Mouse 1' 30.0$, " + // это баг делает
                    "'Mouse 2' 50.0$]",
                    error.getActual());

            assertEquals("['Mouse 1' 30.0$, " +
                    "'Mouse 2' 50.0$, " +
                    "'Mouse 4 - the best mouse!' 66.0$]",
                    error.getExpected());
        }
    }

    @Test
    public void shouldSaveShoppingCart(){
        try  {
            super.shouldSaveShoppingCart();
            fail();
        } catch (ComparisonFailure error) {
            assertEquals("['Mouse 1' 30.0$, " + // это баг делает
                    "'Monitor 2' 120.0$]",
                    error.getActual());

            assertEquals("['Mouse 1' 30.0$, " +
                    "'Mouse 4 - the best mouse!' 66.0$, " +
                    "'Monitor 2' 120.0$, " +
                    "'Monitor 3 - the best monitor!' 190.0$]",
                    error.getExpected());
        }
    }

    @Test
    public void shouldCalculateTotalPriceCorrectly(){
         shouldAddToCartSelectedRecord();

        isElements(
                "'Mouse 1' 30.0$",
                "'Mouse 2' 50.0$");
                //"'Mouse 4 - the best mouse!' 66.0$"); // это баг делает

        isPrice("80.0$"); // это баг делает
    }
}