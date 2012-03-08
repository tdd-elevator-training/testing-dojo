package web.search;


import org.automation.dojo.web.scenario.PriceSortingAscDescLevel2Scenario;
import web.FunctionalTestCase;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.SearchByPriceLevel2Scenario;
import org.automation.dojo.web.scenario.SearchByTextLevel2Scenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.automation.dojo.web.model.ShopService.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchPageLevel2 extends FunctionalTestCase {

    private WebElement search;
    private WebElement searchButton;
    private WebElement searchText;
    private WebElement price;
    private WebElement priceOption;

    @Override
    protected int getMajorRelease() {
        return 1;
    }

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextLevel2Scenario.class, NullBug.class,
                SearchByPriceLevel2Scenario.class, NullBug.class,
                PriceSortingAscDescLevel2Scenario.class, NullBug.class);
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
        price = tester.findElement(By.id("price"));
        priceOption = tester.findElement(By.id("price_search_option"));
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
    public void shouldAllElementsSortedByPrice() {
        enterText("");
        submitSearchForm();

        assertPageContain("List: Description Price " +
                "'Mouse 1' 30.0$ " +
                "'Mouse 3' 40.0$ " +
                "'Mouse 2' 50.0$ " +
                "'Mouse 4 - the best mouse!' 66.0$ " +
                "'Monitor 2' 120.0$ " +
                "'Monitor 1' 150.0$ " +
                "'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void shouldFoundElementsSortedByPrice() {
        enterText("the best");
        submitSearchForm();

        assertPageContain("List: Description Price " +
                "'Mouse 4 - the best mouse!' 66.0$ " +
                "'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void shouldOnlyElementsMoreThanSomePrice() {
        enterText("");
        enterPrice(MORE_THAN, 120);
        submitSearchForm();

        assertPageNotContain("'Mouse 1' 30.0$");
        assertPageNotContain("'Mouse 3' 40.0$");
        assertPageNotContain("'Mouse 2' 50.0$");
        assertPageNotContain("'Mouse 4 - the best mouse!' 66.0$");
        assertPageContain("'Monitor 2' 120.0$");
        assertPageContain("'Monitor 1' 150.0$");
        assertPageContain("'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void shouldOnlyElementsLessThanSomePrice() {
        enterText("");
        enterPrice(LESS_THAN, 120);
        submitSearchForm();

        assertPageContain("'Mouse 1' 30.0$");
        assertPageContain("'Mouse 3' 40.0$");
        assertPageContain("'Mouse 2' 50.0$");
        assertPageContain("'Mouse 4 - the best mouse!' 66.0$");
        assertPageContain("'Monitor 2' 120.0$");
        assertPageNotContain("'Monitor 1' 150.0$");
        assertPageNotContain("'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void shouldIgnorePriceIfNoSelectedPriceOption() {
        enterText("");
        enterPrice(IGNORE, 120);
        submitSearchForm();

        allElementsPresent();
    }

    @Test
    public void shouldAllListIfNotFoundByPrice() {
        enterText("1");
        enterPrice(LESS_THAN, 1);
        submitSearchForm();

        assertNotFound();
        allElementsPresent();
    }

    @Test
    public void shouldIgnorePriceOptionWhenNotFoundByString() {
        enterText("blablablabl");
        enterPrice(LESS_THAN, 120);
        submitSearchForm();

        assertNotFound();
        allElementsPresent();
    }

    @Test
    public void shouldSavePreviousSelection() {
        enterText("some device");
        enterPrice(MORE_THAN, 111);
        submitSearchForm();

        assertFormContains("some device", MORE_THAN, 111);
    }

    private void assertFormContains(String text, int priceOptionNumber, int price) {
        assertEquals(text, getSearchText());
        assertEquals(String.valueOf(price), gtePrice());
        assertEquals(getPriceOption(priceOptionNumber), getSelectedPriceOption());
    }

    private String getSelectedPriceOption() {
        return getSelected(this.priceOption);
    }

    private String getSearchText() {
        return searchText.getAttribute("value");
    }

    private String gtePrice() {
        return this.price.getAttribute("value");
    }

    private String getSelected(WebElement select) {
        return select.findElement(By.xpath("//option[@selected='']")).getAttribute("value");
    }

    protected void enterPrice(int priceOptionNumber, int price) {
        setMoreThan(priceOptionNumber);
        setPrice(price);
    }

    private void setPrice(int price) {
        tester.findElement(By.id("price")).sendKeys(String.valueOf(price));
    }

    private void setMoreThan(int priceOptionNumber) {
        findOption(this.priceOption, getPriceOption(priceOptionNumber)).click();
    }

    private WebElement findOption(WebElement priceSelect, String preceOption) {
        return priceSelect.findElement(By.xpath("//option[@value='" + preceOption + "']"));
    }

    private String getPriceOption(int priceOptionNumber) {
        switch (priceOptionNumber) {
            case 0:
                return "";
            case 1:
                return "more than";
            case 2:
                return "less than";
            default:
                throw new IllegalArgumentException("Bad price option " + priceOptionNumber);
        }
    }

    private void assertNotFound() {
        assertPageContain("Sorry no results for your request, but we have another devices:");
    }

    private void allElementsPresent() {
        assertPageContain("'Mouse 1' 30.0$");
        assertPageContain("'Mouse 3' 40.0$");
        assertPageContain("'Mouse 2' 50.0$");
        assertPageContain("'Mouse 4 - the best mouse!' 66.0$");
        assertPageContain("'Monitor 2' 120.0$");
        assertPageContain("'Monitor 1' 150.0$");
        assertPageContain("'Monitor 3 - the best monitor!' 190.0$");
    }

    private void assertSearchForm() {
        assertPageContain("Please enter text to find");

        assertNotNull(search);
        assertNotNull(searchButton);
        assertNotNull(searchText);
    }

    protected void enterText(String string) {
        searchText.sendKeys(string);
    }

    protected void submitSearchForm() {
        searchButton.submit();
        resetAllElements();
    }

}
