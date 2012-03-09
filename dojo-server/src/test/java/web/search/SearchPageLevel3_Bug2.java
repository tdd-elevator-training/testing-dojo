package web.search;


import org.automation.dojo.web.bugs.BrokenChartSum;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.bugs.SomeRecordsWillNotAddToCart;
import org.automation.dojo.web.scenario.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
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
                ShowUserCartScenario.class, BrokenChartSum.class);
    }

    @Test
    public void shouldCalculateTotalPriceCorrectly(){
        shouldAddToCartSelectedRecord();

        assertPageContain("1 'Mouse 1' 30.0$");
        assertPageContain("2 'Mouse 2' 50.0$");
        assertPageContain("4 'Mouse 4 - the best mouse!' 66.0$");

        assertEquals("100646.0$", getTotalPrice().getText()); // это баг длает
    }
}