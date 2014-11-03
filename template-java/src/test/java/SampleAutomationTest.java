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

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

class Constants {
    static final String SERVER = "http://codenjoy.com/at-dojo";
    static final String APP_CONTEXT = "/search";
}

@RunWith(DojoTestRunner.class)
@ReportTo(server = Constants.SERVER, userName = "apofig")
public class SampleAutomationTest {
    private WebDriver webDriver;

    @Before
    public void setUp() {
        //Please change false to true during dojo
        webDriver = new HtmlUnitDriver(false);
    }

    @Test
    @Scenario(1)
    public void searchByKeyword() {
        // given Open application page
        openPage();

        // when I search by "mouse"
        searchBy("mouse");

        // then I see all products contains "mouse"
        assertAllContains("mouse");
    }

    @Test
    @Scenario(1)
    public void searchByAnotherKeyword() {
        // given Open application page
        openPage();

        // when I search by "monitor"
        searchBy("monitor");

        // then I see all products contains "monitor"
        assertAllContains("monitor");
    }

    private void openPage() {
        webDriver.get(Constants.SERVER + Constants.APP_CONTEXT);
    }

    private void assertAllContains(String address) {
        List<String> productList = getProductList();
        for (String el : productList) {
            assertTrue("Should all contains '" + address + "', but was \n" + productList.toString(),
                    el.toLowerCase().contains(address));
        }
    }

    private List<String> getProductList() {
        List<String> result = new LinkedList<String>();
        for (WebElement el : findElements()) {
            result.add(el.getText());
        }
        return result;
    }

    private List<WebElement> findElements() {
        return webDriver.findElements(By.id("element_description"));
    }

    private void searchBy(String text) {
        WebElement searchText = webDriver.findElement(By.id("search_text"));
        searchText.sendKeys(text);
        searchText.submit();
    }
}
