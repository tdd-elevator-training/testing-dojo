package org.automation.dojo.web;


import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.automation.dojo.web.model.ShopService.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
         nextMajor.click();

    }

    @Test
    public void shouldSwitchMinorWhenClick() {
         nextMinor.click();

    }
}
