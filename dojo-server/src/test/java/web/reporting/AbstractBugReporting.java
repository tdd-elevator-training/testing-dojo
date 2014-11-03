package web.reporting;


import org.automation.dojo.DojoTestClient;
import org.automation.dojo.web.bugs.FoundNotExistsProductBug;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Before;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import web.FunctionalTestCase;

import java.util.Arrays;

public abstract class AbstractBugReporting extends FunctionalTestCase {

    protected DojoTestClient client;
    protected static final Failure PASS = null;
    protected static final Failure FAIL = new Failure(Description.createTestDescription(Object.class, ""), new AssertionError());
    protected static final Failure EXCEPTION = new Failure(Description.createTestDescription(Object.class, ""), new Exception());


    @Override
    protected int getMajorRelease() {
        return 0;
    }

    @Override
    protected String getPageUrl() {
        return "/search";
    }

    @Override
    protected void resetAllElements() {
    }

    @Before
    public void setUp () {
        String userName = "apofig";
        client = new DojoTestClient("http://localhost:" + port + CONTEXT, userName);
        registerUser(userName);
        logService.clearLogs();
    }

    protected void turnBugsOn() {
        switchToMinorRelease(getMinorReleaseAsString(Arrays.asList(SearchByTextScenario.class, FoundNotExistsProductBug.class)));
    }

    protected void turnBugsOff() {
        switchToMinorRelease(getMinorReleaseAsString(Arrays.asList(SearchByTextScenario.class, NullBug.class)));
    }
}