package org.automation.dojo.web;


import org.junit.*;


public class SearchPage extends FunctionalTestCase {

    public static void assertSearchForm() {
		tester.assertTextPresent("Please enter text to find");
        tester.assertFormPresent("search");
        tester.assertButtonPresent("search_button");
        tester.assertElementPresent("search_text");
	}

    @Before
    public void initStartPage() {
        tester.beginAt("/");
    }

    @Test
    public void shouldSearchPageAsWelcomePage() {
    	assertSearchForm();
    }

    @Test
    public void shouldFoundSomeRecordsWhenSearchItByPartOfDescription() {
        tester.setTextField("search_text", "mouse");
        tester.submit();

        assertSearchForm();

        tester.assertTextPresent("List:");
        tester.assertTextPresent("Mouse 1");
        tester.assertTextPresent("Mouse 2");
        tester.assertTextPresent("Mouse 3");
        tester.assertTextPresent("Mouse 4 - the best mouse!");
    }

    @Test
    public void shouldEmptyListWhenFirstComeIn() {
        tester.assertTextNotPresent("List:");
    }

}
