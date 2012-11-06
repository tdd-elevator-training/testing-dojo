package web.search;


import org.automation.dojo.web.bugs.FoundNotExistsProductBug;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
