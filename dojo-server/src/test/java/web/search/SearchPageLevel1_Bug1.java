package web.search;


import web.FunctionalTestCase;
import org.automation.dojo.web.bugs.NoResultWhenExpectedBug;
import org.automation.dojo.web.scenario.SearchByTextLevel1Scenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchPageLevel1_Bug1 extends FunctionalTestCase {

    private WebElement search;
    private WebElement searchButton;
    private WebElement searchText;

    @Override
    protected int getMajorRelease() {
        return 0;
    }

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextLevel1Scenario.class, NoResultWhenExpectedBug.class);
    }

    @Override
    protected String getPageUrl() {
        return "/search";
    }

    @Override
    protected void resetAllElements() {
        search = tester.findElement(By.name("search"));
        searchButton = tester.findElement(By.id("search_button"));
        searchText = tester.findElement(By.id("search_text"));
    }

    @Test
    public void shouldSearchPageAsWelcomePage() {
        assertSearchForm();
    }

    @Test
    public void shouldFoundSomeRecordsWhenSearchItByPartOfDescription() {
        enterText("mouse");
        submitSearchForm();

        assertSearchForm();

        assertPageContain("List:");
        assertPageContain("Mouse 1");
        assertPageContain("Mouse 2");
        assertPageContain("Mouse 3");
        assertPageContain("Mouse 4 - the best mouse!");
    }

    @Test
    public void shouldEmptyListWhenFirstComeIn() {
        assertPageNotContain("List:");
    }

    @Test
    public void shouldAllListWhenFindEmptyString() {
        enterText("");
        submitSearchForm();

        try {
            assertPageContain("List:");
            throw new RuntimeException("Expected exception");
        } catch (AssertionError e) {
        }
        try {
            allElementsPresent();
            throw new RuntimeException("Expected exception");
        } catch (AssertionError e) {
        }
    }

    @Test
    public void shouldFoundSomeAnotherRecordsWhenSearchItByPartOfDescription() {
        enterText("monitor");
        submitSearchForm();

        assertSearchForm();

        assertPageContain("List:");
        assertPageContain("Monitor 1");
        assertPageContain("Monitor 2");
        assertPageContain("Monitor 3 - the best monitor!");
        assertPageNotContain("Mouse");
    }

    @Test
    public void shouldAllListWhenNotFound() {
        enterText("keyboard");
        submitSearchForm();

        assertNotFound();
        allElementsPresent();
    }

    @Test
    public void shouldSavePreviousSelection() {
        enterText("some device");
        submitSearchForm();

        assertEquals("some device", getSearchText());
    }

    private String getSearchText() {
        return searchText.getAttribute("value");
    }

    private void assertNotFound() {
        assertPageContain("Sorry no results for your request, but we have another devices:");
    }

    private void allElementsPresent() {
        assertPageContain("'Mouse 1'");
        assertPageContain("'Mouse 3'");
        assertPageContain("'Mouse 2'");
        assertPageContain("'Mouse 4 - the best mouse!'");
        assertPageContain("'Monitor 2'");
        assertPageContain("'Monitor 1'");
        assertPageContain("'Monitor 3 - the best monitor!'");
    }

    private void assertSearchForm() {
        assertPageContain("Please enter text to find");

        assertNotNull(search);
        assertNotNull(searchButton);
        assertNotNull(searchText);
    }

    private void enterText(String string) {
        searchText.sendKeys(string);
    }

    private void submitSearchForm() {
        searchButton.submit();
        resetAllElements();
    }

}
