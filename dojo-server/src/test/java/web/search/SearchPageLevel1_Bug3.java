package web.search;


import org.automation.dojo.web.bugs.AddSomeOtherElementIfListNotEmptyBug;
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

public class SearchPageLevel1_Bug3 extends SearchPageLevel1 {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, AddSomeOtherElementIfListNotEmptyBug.class);
    }

    @Test
    public void shouldFoundSomeRecordsWhenSearchItByPartOfDescription() {
        try  {
            super.shouldFoundSomeRecordsWhenSearchItByPartOfDescription();
            fail();
        } catch (ComparisonFailure error) {
            assertEquals("['Mouse 1', 'Mouse 2', 'Mouse 3', 'Mouse 4 - the best mouse!', 'Monitor 1']",
                    error.getActual());// это баг длает
            assertEquals("['Mouse 1', 'Mouse 2', 'Mouse 3', 'Mouse 4 - the best mouse!']", error.getExpected());
        }
    }

    @Test
    public void shouldFoundSomeAnotherRecordsWhenSearchItByPartOfDescription() {
        try  {
            super.shouldFoundSomeAnotherRecordsWhenSearchItByPartOfDescription();
            fail();
        } catch (ComparisonFailure error) {
            assertEquals("['Mouse 1', 'Monitor 1', 'Monitor 2', 'Monitor 3 - the best monitor!']", // это баг длает
                    error.getActual());

            assertEquals("['Monitor 1', 'Monitor 2', 'Monitor 3 - the best monitor!']",
                    error.getExpected());
        }
    }

}
