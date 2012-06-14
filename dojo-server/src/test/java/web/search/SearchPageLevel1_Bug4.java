package web.search;


import org.automation.dojo.web.bugs.AddSomeOtherElementIfListNotEmptyBug;
import org.automation.dojo.web.bugs.NoSearchTextMaxLengthBug;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.ComparisonFailure;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SearchPageLevel1_Bug4 extends SearchPageLevel1 {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NoSearchTextMaxLengthBug.class);
    }

    @Test
    public void shouldCutStringWhenSearchTextMaxLength() {
        try  {
            super.shouldCutStringWhenSearchTextMaxLength();
            fail();
        } catch (AssertionError error) {
            assertEquals("expected:<200> but was:<1000>",
                    error.getMessage()); // это баг длает
        }
    }

    @Test
    @Ignore
    public void shouldBugWhenSearchTextIsTooLong() {
        try  {
            submitSearchTextWithLength(200001);  // TODO тут сильно зависает вебдрайвер на вставке текста в поле ввода
            fail();
        } catch (NoSuchElementException error) {
            tester.getPageSource().contains("java.lang.IllegalStateException: Form too large200027>200000");
        }
    }

}
