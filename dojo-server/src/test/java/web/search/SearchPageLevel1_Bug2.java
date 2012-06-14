package web.search;


import org.automation.dojo.web.bugs.FoundNotExistsProductBug;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SearchPageLevel1_Bug2 extends SearchPageLevel1 {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, FoundNotExistsProductBug.class);
    }

    @Test
    public void shouldAllListWhenNotFound() {
        enterText("keyboard");
        search();

        try { // это баг делает
            isNoResultsFound();
            throw new RuntimeException("Expected exception");
        } catch (AssertionError e) {
        }
        try { // это баг делает
            isAllInList();
            throw new RuntimeException("Expected exception");
        } catch (AssertionError e) {
        }
    }

}
