import org.automation.dojo.DojoTestRunner;
import org.automation.dojo.ReportTo;
import org.automation.dojo.Scenario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

@RunWith(DojoTestRunner.class)
@ReportTo(server = "http://127.0.0.1:8080", userName = "JohnDoe")
public class SampleAutomationTest {
    private WebDriver webDriver;
    @Before
    public void setUp() {
        webDriver = new HtmlUnitDriver(true);
    }
    @Test
    @Scenario(1)
    public void searchForProduct() {
        //Given open google.com
        webDriver.get("http://google.com");

        // When I search by "automated testing dojo"
        searchBy("automated testing dojo");

        //Then I see a link to Sergey's blog
        //And I see a link to Alexander's blog
        assertContainsLink("szelenin.blogspot.com");
        assertContainsLink("apofig.blogspot.com");
    }

    private void assertContainsLink(String address) {
        assertFalse(webDriver.findElements(By.xpath("//a[contains(@href, '" + address + "')]")).isEmpty());
    }

    private void searchBy(String text) {
        WebElement searchText = webDriver.findElement(By.name("q"));
        searchText.sendKeys(text);
        searchText.submit();
    }
}
