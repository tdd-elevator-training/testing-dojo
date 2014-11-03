package web;

import org.automation.dojo.*;
import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.stubbing.OngoingStubbing;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class FunctionalTestCase {

    protected static int port;
    protected static WebDriver tester;
    protected static String baseUrl;
    private static ReleaseEngine releaseEngine;
    protected static LogService logService;
    public static String CONTEXT = "/at-dojo";
    protected static Dice dice;

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
        port = ServerRunner.getInstance().start(CONTEXT);
        baseUrl = "http://localhost:" + port + CONTEXT;
        tester = new HtmlUnitDriver(true);

        releaseEngine = ApplicationContextLocator.getBean("releaseEngine");

        logService = ApplicationContextLocator.getBean("logService");

        dice = mock(Dice.class);
        ShopBugsQueue bugsQueue = ApplicationContextLocator.getBean("bugsQueue");
        bugsQueue.setDice(dice);
    }

    @Before
    public void setupReleases() throws Exception {
        switchToMajorRelease(getMajorRelease());
        switchToMinorRelease((List<Class<? extends Serializable>>) getMinorRelease());

        tester.get(baseUrl + getPageUrl());
        resetAllElements();
    }

    public void registerUser(String userName) {
        logService.registerPlayer(userName);
    }

    protected void switchToMinorRelease(List<Class<? extends Serializable>> minorRelease) {
        Release release = releaseEngine.getCurrentRelease();
        List<BasicScenario> scenarios = release.getScenarios();
        List<Integer> randoms = new LinkedList<Integer>();
        for (int i = 0; i < scenarios.size(); i++) {
            BasicScenario scenario = scenarios.get(i);

            Class<Bug> bugToSet = null;
            for (int j = 0; j < minorRelease.size()/2; j++) {
                Class<BasicScenario> scenarioToSet = (Class<BasicScenario>) minorRelease.get(j*2);

                if (scenarioToSet.equals(scenario.getClass())) {
                    bugToSet = (Class<Bug>) minorRelease.get(j * 2 + 1);
                    break;
                }
            }

            if (bugToSet.equals(NullBug.class)) {
                randoms.add(scenario.getPossibleBugs().size()); // for select NullBug
            } else {
                List<? extends Bug> possibleBugs = scenario.getPossibleBugs();
                for (int j = 0; j < possibleBugs.size(); j++) {
                    Bug bug = possibleBugs.get(j);
                    if (bug.getClass().equals(bugToSet)) {
                        randoms.add(j);
                        break;
                    }
                }
            }
        }

        if (randoms.isEmpty()) {
            throw new RuntimeException("Чето не так");
        }
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (Integer bugIndex : randoms) {
            when = when.thenReturn(new Integer(bugIndex.intValue()));
        }

        releaseEngine.nextMinorRelease();

        if (minorRelease.equals(releaseEngine.getMinorInfo())) {
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
        if (!url.contains(CONTEXT)) { // TODO hotfix
            url = url.replace(baseUrl.replace(CONTEXT, ""), baseUrl);
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