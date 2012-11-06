package web.search;


import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.PriceSortingAscDescScenario;
import org.automation.dojo.web.scenario.SearchByPriceScenario;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.automation.dojo.web.servlet.RequestWorker;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.automation.dojo.web.model.ShopService.*;
import static org.junit.Assert.assertEquals;

public class SearchPageLevel2 extends SearchPageLevel1 {

    private WebElement price;
    private WebElement priceSearchOption;

    protected static final boolean DESC = RequestWorker.DESC;
    protected static final boolean ASC = RequestWorker.ASC;
    private List<WebElement> prices;

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
        price = tester.findElement(By.id("price"));
        priceSearchOption = tester.findElement(By.id("price_search_option"));
        super.resetAllElements(); // должно быть тут, потому что последней строкой должен быть поиск формы search
    }

    @Test
    public void shouldAllElementsSortedByPrice() {
        enterText("");
        search();

        isInformation("List:");
        isElements(
                "'Mouse 1' 30.0$",
                "'Mouse 3' 40.0$",
                "'Mouse 2' 50.0$",
                "'Mouse 4 - the best mouse!' 66.0$",
                "'Monitor 2' 120.0$",
                "'Monitor 1' 150.0$",
                "'Monitor 3 - the best monitor!' 190.0$");
    }

    @Override
    protected void isElements(String... expected) {
        List<String> actualList = getElementsSorted();

        List<String> expectedList = Arrays.asList(expected);
        assertEquals(expectedList.toString(), actualList.toString());
    }

    protected List<String> getElementsSorted() {
        List<String> descriptions = getStrings(getListOfProduct("element_description"));
        List<String> prices = getStrings(getListOfProduct("element_price"));
        assertEquals("списки описаний и прайсов товаров",
                descriptions.size(), prices.size());

        List<String> actualList = new LinkedList<String>();
        for (int index = 0; index < descriptions.size(); index++) {
            String description = descriptions.get(index);
            String price = prices.get(index);
            actualList.add(String.format("%s %s", description, price));
        }
        return actualList;
    }

    @Test
    public void shouldFoundElementsSortedByPrice() {
        enterText("the best");
        search();

        isInformation("List:");
        isElements(
                "'Mouse 4 - the best mouse!' 66.0$",
                "'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void shouldFoundElementsSortedByPriceIfDesc() {
        enterText("the best");
        search();
        selectSortingOrder(DESC);

        isInformation("List:");
        isElements(
                "'Monitor 3 - the best monitor!' 190.0$",
                "'Mouse 4 - the best mouse!' 66.0$");
    }

    @Test
    public void shouldOnlyElementsMoreThanSomePrice() {
        enterText("");
        enterPrice(MORE_THAN, 120);
        search();

        isElements(
                "'Monitor 2' 120.0$",
                "'Monitor 1' 150.0$",
                "'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void shouldOnlyElementsLessThanSomePrice() {
        enterText("");
        enterPrice(LESS_THAN, 120);
        search();

        isElements(
                "'Mouse 1' 30.0$",
                "'Mouse 3' 40.0$",
                "'Mouse 2' 50.0$",
                "'Mouse 4 - the best mouse!' 66.0$",
                "'Monitor 2' 120.0$");
    }

    @Test
    public void shouldIgnorePriceIfNoSelectedPriceOption() {
        enterText("");
        enterPrice(IGNORE, 120);
        search();

        isAllInList();
    }

    @Test
    public void shouldAllListIfNotFoundByPrice() {
        enterText("1");
        enterPrice(LESS_THAN, 1);
        search();

        isNoResultsFound();
        isAllInList();
    }

    @Test
    public void shouldIgnorePriceOptionWhenNotFoundByString() {
        enterText("blablablabl");
        enterPrice(LESS_THAN, 120);
        search();

        isNoResultsFound();
        isAllInList();
    }

    @Test
    public void shouldSavePreviousSelection() {
        enterText("some device");
        enterPrice(MORE_THAN, 111);
        search();

        isSearchBy("some device", MORE_THAN, 111);
    }

    @Test
    public void shouldSavePreviousSortingOrderWhen() {
        enterText("some device");
        enterPrice(MORE_THAN, 111);
        search();
        isSortingOrder(ASC);

        selectSortingOrder(DESC);

        isSortingOrder(DESC);

        selectSortingOrder(ASC);

        isSortingOrder(ASC);
    }

    protected void selectSortingOrder(boolean isAsc) {
        findOption(getPriceSortingOrderOption(), getAscDesc(isAsc)).click();
        resetAllElements();
    }

    private String getAscDesc(boolean isAsc) {
        if (isAsc) {
            return "ascending";
        } else {
            return "descending";
        }
    }

    protected void isSortingOrder(boolean isAsc) {
        assertEquals(getAscDesc(isAsc), getSelectedPriceSortingOrderOption());
    }

    protected void isSearchBy(String text, int priceOptionNumber, int price) {
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
        selectPriceOption(priceOptionNumber);
        enterPrice(price);
    }

    protected void enterPrice(int price) {
        String string = String.valueOf(price);
        enterPrice(string);
    }

    protected void enterPrice(String string) {
        this.price.sendKeys(string);
    }

    protected void selectPriceOption(int priceOptionNumber) {
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

    protected void isAllInList() {
        isElements("'Mouse 1' 30.0$",
                "'Mouse 3' 40.0$",
                "'Mouse 2' 50.0$",
                "'Mouse 4 - the best mouse!' 66.0$",
                "'Monitor 2' 120.0$",
                "'Monitor 1' 150.0$",
                "'Monitor 3 - the best monitor!' 190.0$");
    }

    public WebElement getPriceSortingOrderOption() {
        return tester.findElement(By.id("price_sorting_order_option"));
    }

    @Override
    @Test
    public void shouldFoundSomeRecordsWhenSearchItByPartOfDescription() {
        enterText("mouse");
        search();

        isSearchForm();
        isInformation("List:");
        isElements("'Mouse 1' 30.0$",
                "'Mouse 3' 40.0$",
                "'Mouse 2' 50.0$",
                "'Mouse 4 - the best mouse!' 66.0$");
    }

    @Override
    @Test
    public void shouldFoundSomeAnotherRecordsWhenSearchItByPartOfDescription() {
        enterText("monitor");
        search();

        isSearchForm();
        isInformation("List:");
        isElements("'Monitor 2' 120.0$",
                "'Monitor 1' 150.0$",
                "'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void mustBeAPriceValidation() {
        enterText("mouse");
        selectPriceOption(MORE_THAN);
        enterPrice("qwe");
        isValidationInfo("");
        search();

        isValidationInfo("price must be an positive integer");
    }

    @Test
    public void mustBeAPriceValidationWhenBlur() {
        isValidationInfo("");
        enterPrice("qwe");
        searchText.click();
        isValidationInfo("price must be an positive integer");
    }

    protected void isValidationInfo(String expected) {
        assertEquals(expected, tester.findElement(By.id("validation_info")).getText());
    }

    protected void isError(String expected) {
        assertEquals(expected, tester.findElement(By.id("error_info")).getText());
    }
}
