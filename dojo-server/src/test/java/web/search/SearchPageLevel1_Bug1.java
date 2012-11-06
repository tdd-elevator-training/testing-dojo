package web.search;


import org.automation.dojo.web.bugs.NoResultWhenExpectedBug;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SearchPageLevel1_Bug1 extends SearchPageLevel1 {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NoResultWhenExpectedBug.class);
    }

    @Override
    @Test
    public void shouldAllListWhenFindEmptyString() {
        enterText("");
        search();

        try { // это баг делает
            assertPageContain("List:");
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
