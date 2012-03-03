package org.automation.dojo.web.releases;


import org.automation.dojo.web.FunctionalTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationTestContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ReleasesPage extends FunctionalTestCase {

    private WebElement nextMajor;
    private WebElement nextMinor;

    @Before
    public void resetAllElements() {
        nextMajor = tester.findElement(By.linkText("Next major"));
        nextMinor = tester.findElement(By.linkText("Next minor"));
    }

    @Override
    public String getPageUrl() {
        return "/releases";
    }

    @Test
    public void shouldShowTwoLinksWhenFirstLoad() {
        assertNotNull(nextMajor);
        assertNotNull(nextMinor);
    }

    @Test
    public void shouldSwitchMajorWhenClick() {
         goTo(nextMajor.getAttribute("href"));
         assertEquals("[nextMajorRelease]", MockReleaseEngine.pullHistory());
    }

    @Test
    public void shouldSwitchMinorWhenClick() {
         goTo(nextMinor.getAttribute("href"));
         assertEquals("[nextMinorRelease]", MockReleaseEngine.pullHistory());
    }
}
