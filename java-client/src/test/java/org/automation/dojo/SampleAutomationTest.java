package org.automation.dojo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static junit.framework.Assert.fail;

/**
 * @author serhiy.zelenin
 */
@ReportTo(server = "http://localhost:8080", userName = "Sergey")
@RunWith(DojoTestRunner.class)
public class SampleAutomationTest {
    private WebDriver webDriver;
    private static String baseUrl = "http://localhost:8080";
    
    @Before
    public void setUp() {
        webDriver = new HtmlUnitDriver();
    }

    @Test
    @Scenario(1)
    public void searchProductScenario(){
        webDriver.get(baseUrl + "/search");

        WebElement search = webDriver.findElement(By.name("search"));
        WebElement searchButton = webDriver.findElement(By.id("search_button"));
        WebElement searchText = webDriver.findElement(By.id("search_text"));

        searchText.sendKeys("mouse");
        searchButton.submit();
//        fail();
    }
}
