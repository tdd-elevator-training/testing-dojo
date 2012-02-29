package org.automation.dojo.web;


import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.*;

public class SearchPage extends FunctionalTestCase {

    private static final int MORE_THAN = 1;
    private static final int LESS_THAN = 2;
    private static final int IGNORE_OPTION = 0;

    public static void assertSearchForm() {
        assertPageContain("Please enter text to find");
        assertNotNull(tester.findElement(By.name("search")));
        assertNotNull(tester.findElement(By.id("search_button")));
        assertNotNull(tester.findElement(By.id("search_text")));
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

    private void enterText(String string) {
        tester.findElement(By.id("search_text")).sendKeys(string);
    }

    private void submitSearchForm() {
        tester.findElement(By.id("search_button")).submit();
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

        assertPageContain("Sorry no results for your request, but we have another devices:");
        allElementsPresent();
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

    @Test
    public void shouldAllElementsSortedByPrice() {
        enterText("");
        submitSearchForm();

        assertPageContain("List: " +
                "'Mouse 1' 30.0$ " +
                "'Mouse 3' 40.0$ " +
                "'Mouse 2' 50.0$ " +
                "'Mouse 4 - the best mouse!' 66.0$ " +
                "'Monitor 2' 120.0$ " +
                "'Monitor 1' 150.0$ " +
                "'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void shouldAllElementsSortedByPrice2() {
        enterText("the best");
        submitSearchForm();

        assertPageContain("List: " +
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
        enterPrice(IGNORE_OPTION, 120);
        submitSearchForm();

        allElementsPresent();
    }

    @Test
    public void shouldAllListIfNotFoundByPrice() {
        enterText("1");
        enterPrice(LESS_THAN, 1);
        submitSearchForm();

        allElementsPresent();
    }

    @Test
    public void shouldSavePreviousSelection() {
        enterText("some device");
        enterPrice(MORE_THAN, 111);
        submitSearchForm();

        assertFormContains("some device", MORE_THAN, 111);
    }

    private void assertFormContains(String text, int isMoreThan, int price) {
        assertEquals(text, tester.findElement(By.id("search_text")).getAttribute("value"));
        assertEquals(String.valueOf(price), tester.findElement(By.id("price")).getAttribute("value"));
        assertEquals(getPriceOption(isMoreThan), getSelected(tester.findElement(By.id("price_option"))));
    }

    private String getSelected(WebElement select) {
        return select.findElement(By.xpath("//option[@selected='']")).getAttribute("value");
    }

    private void enterPrice(int priceOption, int price) {
        setMoreThan(priceOption);
        setPrice(price);
    }

    private void setPrice(int price) {
        tester.findElement(By.id("price")).sendKeys(String.valueOf(price));
    }

    private void setMoreThan(int priceOption) {
        WebElement priceSelect = tester.findElement(By.id("price_option"));
        findOption(priceSelect, getPriceOption(priceOption)).click();
    }

    private WebElement findOption(WebElement priceSelect, String preceOption) {
        return priceSelect.findElement(By.xpath("//option[@value='" + preceOption + "']"));
    }

    private String getPriceOption(int priceOption) {
        switch (priceOption) {
            case 0 : return "";
            case 1 : return "more than";
            case 2 : return "less than";
            default: throw new IllegalArgumentException("Bad price option " + priceOption);
        }
    }
}
