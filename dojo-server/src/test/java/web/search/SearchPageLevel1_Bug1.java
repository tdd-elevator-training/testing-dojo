package web.search;


import org.automation.dojo.web.bugs.NoResultWhenExpectedBug;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
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
