package web;

import org.junit.AfterClass;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FunctionalTestCase {


	protected static WebDriver tester;

	@Before
	public void init() throws Exception {
	    int port = ServerRunner.getInstance().start();

	    tester = new HtmlUnitDriver();
	    tester.get("http://localhost:" + port + "/Shop");
	}

	@AfterClass
	public static void end() throws Exception {
		ServerRunner.getInstance().stop();
	}

	public FunctionalTestCase() {
		super();
	}

    protected static void assertPageContain(String string) {
        String page = getPageText();
        assertTrue(String.format("Expected page contains '%s' but was '%s'.", string, page),
                page.contains(string));
    }

    private static String getPageText() {
        return tester.getPageSource().replaceAll("<.*?>", "").replaceAll("\\n", "").replaceAll("\\r", "").replaceAll("(  )+", " ");
    }

    protected void assertPageNotContain(String string) {
        String page = getPageText();
        assertFalse(String.format("Expected page NOT contains '%s' but was '%s'.", string, page),
                page.contains(string));
    }
}