package org.automation.dojo.web;


import org.junit.*;
import org.openqa.selenium.By;

import static org.junit.Assert.*;

public class SearchPage extends FunctionalTestCase {

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
        typeText("mouse");
        submitSearchForm();

        assertSearchForm();

        assertPageContain("List:");
        assertPageContain("Mouse 1");
        assertPageContain("Mouse 2");
        assertPageContain("Mouse 3");
        assertPageContain("Mouse 4 - the best mouse!");
    }

    private void typeText(String string) {
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
        typeText("monitor");
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
        typeText("keyboard");
        submitSearchForm();

        assertPageContain("Sorry no results for your request, but we have another devices:");
        assertPageContain("Monitor 1");
        assertPageContain("Monitor 2");
        assertPageContain("Monitor 3 - the best monitor!");
        assertPageContain("Mouse 1");
        assertPageContain("Mouse 2");
        assertPageContain("Mouse 3");
        assertPageContain("Mouse 4 - the best mouse!");
    }

    @Test
    public void shouldAllElementsSortedByPrice() {
        typeText("");
        submitSearchForm();

        assertPageContain("List: " +
                "'Mouse 1' 30.0$ " +
                "'Mouse 3' 40.0$ " +
                "'Mouse 2' 50.0$ " +
                "'Mouse 4 - the best mouse!' " +
                "66.0$ 'Monitor 2' 120.0$ " +
                "'Monitor 1' 150.0$ " +
                "'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void shouldAllElementsSortedByPrice2() {
        typeText("the best");
        submitSearchForm();

        assertPageContain("List: " +
                "'Mouse 4 - the best mouse!' 66.0$ " +
                "'Monitor 3 - the best monitor!' 190.0$");
    }

}
