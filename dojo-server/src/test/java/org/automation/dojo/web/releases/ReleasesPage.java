package org.automation.dojo.web.releases;


import org.automation.dojo.web.FunctionalTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ReleasesPage extends FunctionalTestCase {

    private WebElement nextMajor;
    private WebElement nextMinor;
    private WebElement indicator;

    @Override
    protected void resetAllElements() {
        nextMajor = tester.findElement(By.linkText("Next major"));
        nextMinor = tester.findElement(By.linkText("Next minor"));
        indicator = tester.findElement(By.id("indicator"));
    }

    @Override
    protected String getMinorRelease() {
        return "[Scenario SearchByTextLevel1Scenario with bug NullBug]";
    }

    @Override
    protected int getMajorRelease() {
        return 0;
    }

    @Override
    protected String getPageUrl() {
        return "/releases";
    }

    @Test
    public void shouldShowTwoLinksWhenFirstLoad() {
        assertNotNull(nextMajor);
        assertNotNull(nextMinor);
    }

    @Test
    public void shouldMajorRelease0WithoutBugsAtStart(){
        assertMach("Now we have major 0 and minor " +
                "\\[Scenario SearchByTextLevel1Scenario with bug NullBug\\]",
                indicator.getText());
    }

    @Test
    public void shouldSwitchMajorWhenClick() {
        goTo(nextMajor.getAttribute("href"));

        assertMach("Now we have major 1 and minor " +
                "\\[Scenario SearchByTextLevel2Scenario with bug NullBug, " +
                "Scenario SearchByPriceLevel2Scenario with bug NullBug\\]",
                indicator.getText());
    }

    @Test
    public void shouldSwitchMinorWhenClickForMajor0() {
        goTo(nextMinor.getAttribute("href"));

        assertMach("Now we have major 0 and minor " +
                "\\[Scenario SearchByTextLevel1Scenario with bug .*\\]",
                indicator.getText());
    }

    @Test
    public void shouldSwitchMinorWhenClickForMajor1() {
        goTo(nextMajor.getAttribute("href"));
        goTo(nextMinor.getAttribute("href"));

        assertMach("Now we have major 1 and minor " +
                "\\[Scenario SearchByTextLevel2Scenario with bug .*" +
                "Scenario SearchByPriceLevel2Scenario with bug .*\\]",
                indicator.getText());
    }

    private void assertMach(String regexp, String text) {
        assertTrue(String.format("Expected regexp '%s'\nbut was '%s'", regexp, text),
                Pattern.compile(regexp).matcher(text).matches());
    }
}
