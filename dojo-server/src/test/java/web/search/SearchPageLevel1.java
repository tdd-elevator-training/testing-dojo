package web.search;


import web.FunctionalTestCase;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchPageLevel1 extends FunctionalTestCase {

    private WebElement search;
    private WebElement searchButton;
    protected WebElement searchText;

    @Override
    protected int getMajorRelease() {
        return 0;
    }

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class);
    }

    @Override
    protected String getPageUrl() {
        return "/search";
    }

    @Override
    protected void resetAllElements() {
        searchButton = tester.findElement(By.id("search_button"));
        searchText = tester.findElement(By.id("search_text"));
        search = tester.findElement(By.name("search"));
    }

    @Test
    public void shouldSearchPageAsWelcomePage() {
        isSearchForm();
    }

    @Test
    public void shouldFoundSomeRecordsWhenSearchItByPartOfDescription() {
        enterText("mouse");
        search();

        isSearchForm();
        isInformation("List:");
        isElements("'Mouse 1'",
                "'Mouse 2'",
                "'Mouse 3'",
                "'Mouse 4 - the best mouse!'");
    }

    protected void isInformation(String expected) {
        WebElement element = tester.findElement(By.xpath("//*[@id='search_info']"));
        assertEquals(expected, element.getText());
    }

    protected void isElements(String... expected) {
        List<WebElement> elements = getListOfProduct("element_description");
        List<String> expectedList = Arrays.asList(expected);
        List<String> actual = getStrings(elements);
        assertEquals(expectedList.toString(), actual.toString());
    }

    protected List<String> getStrings(List<WebElement> elements) {
        List<String> result = new LinkedList<String>();
        for (WebElement element : elements) {
            result.add(element.getText());
        }
        return result;
    }

    protected List<WebElement> getListOfProduct(String name) {
        return tester.findElements(By.xpath("//tr[contains(@id,'productId')]/td[@id='" + name + "']"));
    }

    @Test
    public void shouldEmptyListWhenFirstComeIn() {
        assertPageNotContain("List:");
    }

    @Test
    public void shouldFoundSomeAnotherRecordsWhenSearchItByPartOfDescription() {
        enterText("monitor");
        search();

        isSearchForm();
        isInformation("List:");
        isElements("'Monitor 1'",
                "'Monitor 2'",
                "'Monitor 3 - the best monitor!'");
    }

    @Test
    public void shouldAllListWhenNotFound() {
        enterText("keyboard");
        search();

        isNoResultsFound();
        isAllInList();
    }

    @Test
    public void shouldSavePreviousSelection() {
        enterText("some device");
        search();

        assertEquals("some device", getSearchText());
    }

    protected String getSearchText() {
        return searchText.getAttribute("value");
    }

    protected void isNoResultsFound() {
        isInformation("Sorry no results for your request, but we have another devices:");
    }

    protected void isAllInList() {
        isElements("'Mouse 1'",
                "'Mouse 2'",
                "'Mouse 3'",
                "'Mouse 4 - the best mouse!'",
                "'Monitor 1'",
                "'Monitor 2'",
                "'Monitor 3 - the best monitor!'");
    }

    protected void isSearchForm() {
        assertPageContain("Please enter text to find");

        assertNotNull(search);
        assertNotNull(searchButton);
        assertNotNull(searchText);
    }

    protected void enterText(String string) {
        searchText.clear();
        searchText.sendKeys(string);
    }

    protected void search() {
        searchButton.submit();
        resetAllElements();
    }

    @Test
    public void shouldAllListWhenFindEmptyString() {
        enterText("");
        search();

        isInformation("List:");
        isAllInList();
    }

}
