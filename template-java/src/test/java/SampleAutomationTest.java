import org.automation.dojo.DojoTestRunner;
import org.automation.dojo.ReportTo;
import org.automation.dojo.Scenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;

@RunWith(DojoTestRunner.class)
@ReportTo(server = "http://127.0.0.1:8080", userName = "JohnDoe")
public class SampleAutomationTest {

    @Test
    @Scenario(1)
    public void searchForProduct(){
        WebDriver webDriver = new HtmlUnitDriver();
        webDriver.get("http://127.0.0.1:8080");

        WebElement searchButton = webDriver.findElement(By.id("search_button"));
        WebElement searchText = webDriver.findElement(By.id("search_text"));

        searchText.sendKeys("mouse");
        searchButton.submit();

    }
}
