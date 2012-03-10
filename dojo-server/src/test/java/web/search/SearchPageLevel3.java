package web.search;


import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.*;
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
import static org.junit.Assert.fail;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchPageLevel3 extends SearchPageLevel2 {

    @Override
    protected int getMajorRelease() {
        return 2;
    }

    @Override
    protected void resetAllElements() {
        super.resetAllElements();
    }

    protected WebElement getAddToCartButton() {
        return tester.findElement(By.id("add_to_cart_button"));
    }

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class,
                SearchByPriceScenario.class, NullBug.class,
                PriceSortingAscDescScenario.class, NullBug.class,
                AddToUserCartScenario.class, NullBug.class,
                CalculateCartSumScenario.class, NullBug.class);
    }

    @Override
    @Test
    public void shouldSearchPageAsWelcomePage() {
        super.shouldSearchPageAsWelcomePage();

        assertAddToCartFormNotPresent();
    }

    @Test
    public void shouldCartFormPresentIfSomeFound() {
        getListFor("mouse");

        isSearchForm();
        isAddToCart();
    }

    protected void isAddToCart() {
        assertNotNull(getAddToCartButton());
    }

    protected void assertAddToCartFormNotPresent() {
        try {
            getAddToCartButton();
            fail("Expected exception");
        } catch (Exception e) {
        }
    }

    @Test
    public void shouldAddToCartSelectedRecord(){
        getListFor("mouse");

        isInformation("List:");
        isElements(
                "'Mouse 1' 30.0$",
                "'Mouse 3' 40.0$",
                "'Mouse 2' 50.0$",
                "'Mouse 4 - the best mouse!' 66.0$");
        select(1, 3, 4);

        addToCart();
        isCartPage();
        isElements(
                "'Mouse 1' 30.0$",
                "'Mouse 2' 50.0$",
                "'Mouse 4 - the best mouse!' 66.0$");
    }

    private void select(int...indexes) {
        List<WebElement> checkBoxes = getSelectCheckboxes();
        for (int index : indexes) {
            checkBoxes.get(index - 1).click();
        }
    }

    protected void addToCart() {
        getAddToCartButton().click();
        resetAllElements();
    }

    @Test
    public void shouldSaveSearchFormStateWhenGoToCart() {
        enterText("mouse");
        enterPrice(LESS_THAN, 30);
        search();

        select(1);
        addToCart();

        isSearchBy("mouse", LESS_THAN, 30);
    }

    protected void gotoCart() {
        enterText("");
        search();

        select(1);
        addToCart();
    }

    @Test
    public void shouldSaveSearchFormStateWhenGoFromCart() {
        gotoCart();

        enterText("mouse");
        enterPrice(LESS_THAN, 30);
        search();

        isSortingOrder(ASC);
        isSearchBy("mouse", LESS_THAN, 30);
    }

    @Test
    public void shouldSaveSearchFormPriceSortingOrder() {
        getListFor("mouse");
        selectSortingOrder(DESC);
        search();

        select(1);
        addToCart();

        search();
        isSortingOrder(DESC);
    }

    @Test
    public void shouldSearchAtShoppingCartWorksCorrectly(){
        gotoCart();

        enterText("mo");
        enterPrice(MORE_THAN, 120);
        search();

        isInformation("List:");
        isElements(
                "'Monitor 2' 120.0$",
                "'Monitor 1' 150.0$",
                "'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void shouldSaveShoppingCart(){
        getListFor("mouse");

        isElements(
                "'Mouse 1' 30.0$",
                "'Mouse 3' 40.0$",
                "'Mouse 2' 50.0$",
                "'Mouse 4 - the best mouse!' 66.0$");

        select(1, 4);

        addToCart();
        isCartPage();

        enterText("monitor");
        search();

        isElements(
                "'Monitor 2' 120.0$",
                "'Monitor 1' 150.0$",
                "'Monitor 3 - the best monitor!' 190.0$");
        select(1, 3);

        addToCart();
        isCartPage();

        isElements(
                "'Mouse 1' 30.0$",
                "'Mouse 4 - the best mouse!' 66.0$",
                "'Monitor 2' 120.0$",
                "'Monitor 3 - the best monitor!' 190.0$");
    }

    @Test
    public void shouldCalculateTotalPriceCorrectly(){
        getListFor("mouse");

        isInformation("List:");
        isElements(
                "'Mouse 1' 30.0$",
                "'Mouse 3' 40.0$",
                "'Mouse 2' 50.0$",
                "'Mouse 4 - the best mouse!' 66.0$");
        select(1, 3, 4);

        addToCart();
        isCartPage();
        isPrice("146.0$");
    }

    protected void isPrice(String expected) {
        assertEquals(expected, getTotalPrice().getText());
    }

    protected void isCartPage() {
        isInformation("Your cart list:");
    }

    protected void getListFor(String text) {
        enterText("");
        enterText(text);
        search();
    }

    public List<WebElement> getSelectCheckboxes() {
        return tester.findElements(By.xpath("//input[contains(@id,'record')]"));
    }

    public WebElement getTotalPrice() {
        return tester.findElement(By.id("total_price"));
    }
}