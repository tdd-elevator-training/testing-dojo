package web;


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

    @Test
    public void shouldFoundSomeAnotherRecordsWhenSearchItByPartOfDescription() {
        tester.setTextField("search_text", "monitor");
        tester.submit();

        assertSearchForm();

        tester.assertTextPresent("List:");
        tester.assertTextPresent("Monitor 1");
        tester.assertTextPresent("Monitor 2");
        tester.assertTextPresent("Monitor 3 - the best monitor!");
        tester.assertTextNotPresent("Mouse");
    }

    @Test
    public void shouldAllListWhenNotFound() {
        tester.setTextField("search_text", "keyboard");
        tester.submit();

        tester.assertTextPresent("Sorry no results for your request, but we have another devices:");
        tester.assertTextPresent("Monitor 1");
        tester.assertTextPresent("Monitor 2");
        tester.assertTextPresent("Monitor 3 - the best monitor!");
        tester.assertTextPresent("Mouse 1");
        tester.assertTextPresent("Mouse 2");
        tester.assertTextPresent("Mouse 3");
        tester.assertTextPresent("Mouse 4 - the best mouse!");
    }

    @Test
    public void shouldAllElementsSortedByPrice() {
        tester.setTextField("search_text", "");
        tester.submit();

        tester.assertTextPresent("List: " +
                "unchecked 'Mouse 1' 30.0$ " +
                "unchecked 'Mouse 3' 40.0$ " +
                "unchecked 'Mouse 2' 50.0$ " +
                "unchecked 'Mouse 4 - the best mouse!' 66.0$ " +
                "unchecked 'Monitor 2' 120.0$ " +
                "unchecked 'Monitor 1' 150.0$ " +
                "unchecked 'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void shouldAllElementsSortedByPrice2() {
        tester.setTextField("search_text", "the best");
        tester.submit();

        tester.assertTextPresent("List: " +
                "unchecked 'Mouse 4 - the best mouse!' 66.0$ " +
                "unchecked 'Monitor 3 - the best monitor!' 190.0$");
    }

}
