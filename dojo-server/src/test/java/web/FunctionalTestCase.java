package web;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.LogService;
import org.automation.dojo.ReleaseEngine;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class FunctionalTestCase {

    protected static int port;
    protected static WebDriver tester;
    protected static String baseUrl;
    private static ReleaseEngine releaseEngine;
    protected static LogService logService;

    public void join() {
        System.out.println(baseUrl);
        System.out.println("major - " + releaseEngine.getMajorInfo());
        System.out.println("minor - " + releaseEngine.getMinorInfo());
        try {
            ServerRunner.getInstance().join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeClass
    public static void init() throws Exception {
        port = ServerRunner.getInstance().start();
        baseUrl = "http://localhost:" + port + "/Shop";
        tester = new HtmlUnitDriver(true);

        releaseEngine = ApplicationContextLocator.getBean("releaseEngine");
        logService = ApplicationContextLocator.getBean("logService");
    }

    @Before
    public void setupReleases() throws Exception {
        switchToMajorRelease(getMajorRelease());
        switchToMinorRelease(getMinorReleaseAsString((List<Class<? extends Serializable>>) getMinorRelease()));

        tester.get(baseUrl + getPageUrl());
        resetAllElements();
    }

    public void registerUser(String userName) {
        logService.registerPlayer(userName);
    }

    protected String getMinorReleaseAsString(List<Class<? extends Serializable>> minorRelease) {
        List<String> result = new LinkedList<String>();
        for (int count = 0; count < minorRelease.size()/2; count++) {
            Class scenarioClass = minorRelease.get(count*2);
            Class bugClass = minorRelease.get(count*2 + 1);
            result.add(String.format("Scenario %s with bug %s",
                    scenarioClass.getSimpleName(), bugClass.getSimpleName()));
        }
        return result.toString();
    }

    protected void switchToMinorRelease(String minorRelease) {
        int countLoop = 0;
        final int MAX = 10000;
        do {
            releaseEngine.nextMinorRelease();
            countLoop++;
        } while (!minorRelease.equals(releaseEngine.getMinorInfo()) && countLoop < MAX);
        if (countLoop == MAX) {
            throw new IllegalArgumentException("Release " + minorRelease + " not found");
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
        return tester.getPageSource()
                .replaceAll("\\n", "").replaceAll("\\r", "")
                .replaceAll(String.valueOf((char)160), " ")  // это так &nbsp; заменяется
                .replaceAll("(  )+", " ")
                .replaceAll("<option.*?</option>", "")
                .replaceAll("<.*?>", "")
                .replaceAll("(  )+", " ")
                .replaceAll("(  )+", " ")
                .replaceAll("(  )+", " ");
    }

    protected void assertPageNotContain(String string) {
        String page = getPageText();
        assertFalse(String.format(
                "Expected page NOT contains '%s'\n" +
                        "but was '%s'.", string, page),
                page.contains(string));
    }
}