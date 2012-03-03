package org.automation.dojo.web;

import org.automation.dojo.ApplicationContextLocator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FunctionalTestCase {

    @Autowired
    private static ApplicationContext context;

    private static ApplicationContextLocator instance;

    protected static WebDriver tester;
    private static int port;

    @BeforeClass
    public static void init() throws Exception {
        instance = ApplicationContextLocator.getInstance();
        instance.setApplicationContext(new FileSystemXmlApplicationContext("classpath:/org/automation/dojo/applicationContext.xml"));

        port = ServerRunner.getInstance().start();

        tester = new HtmlUnitDriver();
    }

    @Before
    public void loadPage(){
        tester.get("http://localhost:" + port + "/Shop");
    }

    @AfterClass
    public static void end() throws Exception {
        ApplicationContextLocator.clearInstance();
        ServerRunner.getInstance().stop();
    }

    public FunctionalTestCase() {
        super();
    }

    protected void assertPageContain(String string) {
        String page = getPageText();
        assertTrue(String.format("Expected page contains '%s' " +
                "but was '%s'.", string, page),
                page.contains(string));
    }

    private String getPageText() {
        return tester.getPageSource().replaceAll("<.*?>", "")
                .replaceAll("\\n", "").replaceAll("\\r", "")
                .replaceAll("(  )+", " ");
    }

    protected void assertPageNotContain(String string) {
        String page = getPageText();
        assertFalse(String.format("Expected page NOT contains " +
                "'%s' but was '%s'.", string, page),
                page.contains(string));
    }
}