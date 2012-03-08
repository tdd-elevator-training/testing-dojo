import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author serhiy.zelenin
 */
public class SearchBox {
    @FindBy(id = "search_button")
    private WebElement button;

    @FindBy(id = "search_text")
    private WebElement text;

    public void searchBy(String searchString) {
        text.sendKeys(searchString);
        text.submit();
    }
}
