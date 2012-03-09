package web;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.ReleaseEngine;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

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

    @Before
    public void init() throws Exception {
        int port = ServerRunner.getInstance().start();
        baseUrl = "http://localhost:" + port + "/Shop";

        tester = new HtmlUnitDriver(true);

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
        if (countLoop == 10000) {
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

    @After
    public void end() throws Exception {
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