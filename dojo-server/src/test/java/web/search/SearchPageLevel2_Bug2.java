package web.search;


import org.automation.dojo.web.scenario.PriceSortingAscDescScenario;
import org.automation.dojo.web.bugs.BrokenSortingBug;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.SearchByPriceScenario;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.automation.dojo.web.model.ShopService.IGNORE;
import static org.automation.dojo.web.model.ShopService.LESS_THAN;
import static org.automation.dojo.web.model.ShopService.MORE_THAN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchPageLevel2_Bug2 extends SearchPageLevel2 {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class,
                SearchByPriceScenario.class, NullBug.class,
                PriceSortingAscDescScenario.class, BrokenSortingBug.class);
    }

    @Override
    /**
     * Так как порядок нарушен, то мы вынуждены его игнорить
     */
    protected void isElements(String... expected) {   // это делает бага
        List<String> actualList = getElementsSorted();
        String expectedString = Arrays.asList(expected).toString();
        for (String actual : actualList) {
            assertTrue("Expected contains: [" + actual + "]\n" +
                    "in: " + expectedString,
                    expectedString.contains(actual));
        }
    }
}
