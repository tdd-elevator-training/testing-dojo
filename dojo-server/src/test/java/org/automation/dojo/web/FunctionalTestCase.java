package org.automation.dojo.web;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.ReleaseEngine;
import org.automation.dojo.web.bugs.AddSomeOtherElementIfListNotEmptyBug;
import org.automation.dojo.web.scenario.SearchByTextLevel1Scenario;
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

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class FunctionalTestCase {

    @Autowired
    private ApplicationContext context;
    private ApplicationContextLocator instance;

    protected static WebDriver tester;
    private static String baseUrl;
    private ReleaseEngine releaseEngine;

    @BeforeClass
    public static void init() throws Exception {
        int port = ServerRunner.getInstance().start();
        baseUrl = "http://localhost:" + port + "/Shop";

        tester = new HtmlUnitDriver();
    }

    @Before
    public void loadPage(){
        instance = ApplicationContextLocator.getInstance();
        instance.setApplicationContext(context);
        releaseEngine = (ReleaseEngine) context.getBean("releaseEngine");

        switchToMajorRelease(getMajorRelease());
        switchToMinorRelease(getMinorReleaseAsString((List<Class>) getMinorRelease()));

        tester.get(baseUrl + getPageUrl());
        resetAllElements();
    }

    private String getMinorReleaseAsString(List<Class> minorRelease) {
        List<String> result = new LinkedList<String>();
        for (int count = 0; count < minorRelease.size()/2; count++) {
            Class scenarioClass = minorRelease.get(count*2);
            Class bugClass = minorRelease.get(count*2 + 1);
            result.add(String.format("Scenario %s with bug %s",
                    scenarioClass.getSimpleName(), bugClass.getSimpleName()));
        }
        return result.toString();
    }

    private void switchToMinorRelease(String minorRelease) {
        int countLoop = 0;
        do {
            releaseEngine.nextMinorRelease();
            countLoop++;
        } while (!minorRelease.equals(releaseEngine.getMinorInfo()) && countLoop < 1000);
        if (countLoop == 1000) {
            throw new IllegalArgumentException(minorRelease + " not found");
        }
    }

    private void switchToMajorRelease(int majorRelease) {
        releaseEngine.setMajorRelease(majorRelease);
    }

    protected abstract int getMajorRelease();

    protected abstract String getPageUrl();

    protected abstract void resetAllElements();

    protected abstract List<?> getMinorRelease();

    @AfterClass
    public static void end() throws Exception {
        ApplicationContextLocator.clearInstance();
        ServerRunner.getInstance().stop();
    }

    public void goTo(String url) {
        if (!url.contains("Shop")) { // TODO hotfix
            url = url.replace(baseUrl.replace("/Shop", ""), baseUrl);
        }
        if (!url.contains(baseUrl)) {
            url = baseUrl + url;
        }
        tester.get(url);
        resetAllElements();
    }

    public FunctionalTestCase() {
        super();
    }

    protected void assertPageContain(String string) {
        String page = getPageText();
        assertTrue(String.format("Expected page contains '%s'\n" +
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