package web.search;


import org.automation.dojo.web.scenario.PriceSortingAscDescScenario;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.SearchByPriceScenario;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.automation.dojo.web.servlet.RequestWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.automation.dojo.web.model.ShopService.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchPageLevel2 extends SearchPageLevel1 {

    private WebElement price;
    private WebElement priceSearchOption;

    protected static final boolean DESC = RequestWorker.DESC;
    protected static final boolean ASC = RequestWorker.ASC;

    @Override
    protected int getMajorRelease() {
        return 1;
    }

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class,
                SearchByPriceScenario.class, NullBug.class,
                PriceSortingAscDescScenario.class, NullBug.class);
    }

    @Override
    protected String getPageUrl() {
        return "/search";
    }

    @Override
    protected void resetAllElements() {
        super.resetAllElements();
        price = tester.findElement(By.id("price"));
        priceSearchOption = tester.findElement(By.id("price_search_option"));
    }

    @Test
    public void shouldAllElementsSortedByPrice() {
        enterText("");
        submitSearchForm();

        assertPageContain("List: Code Description Price " +
                "1 'Mouse 1' 30.0$ " +
                "3 'Mouse 3' 40.0$ " +
                "2 'Mouse 2' 50.0$ " +
                "4 'Mouse 4 - the best mouse!' 66.0$ " +
                "6 'Monitor 2' 120.0$ " +
                "5 'Monitor 1' 150.0$ " +
                "7 'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void shouldFoundElementsSortedByPrice() {
        enterText("the best");
        submitSearchForm();

        assertPageContain("List: Code Description Price " +
                "4 'Mouse 4 - the best mouse!' 66.0$ " +
                "7 'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void shouldFoundElementsSortedByPriceIfDesc() {
        enterText("the best");
        submitSearchForm();
        selectSortingOrder(DESC);
        submitSearchForm();

        assertPageContain("List: Code Description Price " +
                "7 'Monitor 3 - the best monitor!' 190.0$ " +
                "4 'Mouse 4 - the best mouse!' 66.0$");
    }

    @Test
    public void shouldOnlyElementsMoreThanSomePrice() {
        enterText("");
        enterPrice(MORE_THAN, 120);
        submitSearchForm();

        assertPageNotContain("'Mouse 1' 30.0$");
        assertPageNotContain("'Mouse 3' 40.0$");
        assertPageNotContain("'Mouse 2' 50.0$");
        assertPageNotContain("'Mouse 4 - the best mouse!' 66.0$");
        assertPageContain("'Monitor 2' 120.0$");
        assertPageContain("'Monitor 1' 150.0$");
        assertPageContain("'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void shouldOnlyElementsLessThanSomePrice() {
        enterText("");
        enterPrice(LESS_THAN, 120);
        submitSearchForm();

        assertPageContain("'Mouse 1' 30.0$");
        assertPageContain("'Mouse 3' 40.0$");
        assertPageContain("'Mouse 2' 50.0$");
        assertPageContain("'Mouse 4 - the best mouse!' 66.0$");
        assertPageContain("'Monitor 2' 120.0$");
        assertPageNotContain("'Monitor 1' 150.0$");
        assertPageNotContain("'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void shouldIgnorePriceIfNoSelectedPriceOption() {
        enterText("");
        enterPrice(IGNORE, 120);
        submitSearchForm();

        allElementsPresent();
    }

    @Test
    public void shouldAllListIfNotFoundByPrice() {
        enterText("1");
        enterPrice(LESS_THAN, 1);
        submitSearchForm();

        assertNotFound();
        allElementsPresent();
    }

    @Test
    public void shouldIgnorePriceOptionWhenNotFoundByString() {
        enterText("blablablabl");
        enterPrice(LESS_THAN, 120);
        submitSearchForm();

        assertNotFound();
        allElementsPresent();
    }

    @Test
    public void shouldSavePreviousSelection() {
        enterText("some device");
        enterPrice(MORE_THAN, 111);
        submitSearchForm();

        assertFormContains("some device", MORE_THAN, 111);
    }

    @Test
    public void shouldSavePreviousSortingOrderWhen() {
        enterText("some device");
        enterPrice(MORE_THAN, 111);
        submitSearchForm();
        assertSortingOrder(ASC);

        selectSortingOrder(DESC);
        submitSearchForm();

        assertSortingOrder(DESC);

        selectSortingOrder(ASC);
        submitSearchForm();

        assertSortingOrder(ASC);
    }

    protected void selectSortingOrder(boolean isAsc) {
        findOption(getPriceSortingOrderOption(), getAscDesc(isAsc)).click();
    }

    private String getAscDesc(boolean isAsc) {
        if (isAsc) {
            return "ascending";
        } else {
            return "descending";
        }
    }

    protected void assertSortingOrder(boolean isAsc) {
        assertEquals(getAscDesc(isAsc), getSelectedPriceSortingOrderOption());
    }

    private void assertFormContains(String text, int priceOptionNumber, int price) {
        assertEquals(text, getSearchText());
        assertEquals(String.valueOf(price), getPrice());
        assertEquals(getPriceOption(priceOptionNumber), getSelectedPriceOption());
    }

    private String getSelectedPriceOption() {
        return getSelected(priceSearchOption);
    }

    private String getSelectedPriceSortingOrderOption() {
        return getSelected(getPriceSortingOrderOption());
    }

    private String getPrice() {
        return price.getAttribute("value");
    }

    private String getSelected(WebElement select) {
        String xpath = "//select[@id='" + select.getAttribute("id") + "']//option[@selected='']";
        return tester.findElement(By.xpath(xpath)).getAttribute("value");
    }

    protected void enterPrice(int priceOptionNumber, int price) {
        setMoreThan(priceOptionNumber);
        setPrice(price);
    }

    private void setPrice(int price) {
        this.price.sendKeys(String.valueOf(price));
    }

    private void setMoreThan(int priceOptionNumber) {
        findOption(priceSearchOption, getPriceOption(priceOptionNumber)).click();
    }

    private WebElement findOption(WebElement priceSelect, String preceOption) {
        return priceSelect.findElement(By.xpath("//option[@value='" + preceOption + "']"));
    }

    private String getPriceOption(int priceOptionNumber) {
        switch (priceOptionNumber) {
            case 0:
                return "";
            case 1:
                return "more than";
            case 2:
                return "less than";
            default:
                throw new IllegalArgumentException("Bad price option " + priceOptionNumber);
        }
    }

    protected void allElementsPresent() {
        assertPageContain("'Mouse 1' 30.0$");
        assertPageContain("'Mouse 3' 40.0$");
        assertPageContain("'Mouse 2' 50.0$");
        assertPageContain("'Mouse 4 - the best mouse!' 66.0$");
        assertPageContain("'Monitor 2' 120.0$");
        assertPageContain("'Monitor 1' 150.0$");
        assertPageContain("'Monitor 3 - the best monitor!' 190.0$");
    }

    public WebElement getPriceSortingOrderOption() {
        return tester.findElement(By.id("price_sorting_order_option"));
    }
}
