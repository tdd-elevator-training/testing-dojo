package web.search;


import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.bugs.SomeRecordsWillNotAddToCart;
import org.automation.dojo.web.scenario.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.automation.dojo.web.model.ShopService.LESS_THAN;
import static org.automation.dojo.web.model.ShopService.MORE_THAN;
import static org.junit.Assert.*;

@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchPageLevel3_Bug1 extends SearchPageLevel3 {

    @Override
    protected int getMajorRelease() {
        return 2;
    }

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class,
                SearchByPriceScenario.class, NullBug.class,
                PriceSortingAscDescScenario.class, NullBug.class,
                AddToUserCartScenario.class, SomeRecordsWillNotAddToCart.class,
                ShowUserCartScenario.class, NullBug.class);
    }

    @Test
    public void shouldAddToCartSelectedRecord(){
        getListFor("mouse");

        assertPageContain("List: Code Description Price " +
                "1 'Mouse 1' 30.0$ " +
                "3 'Mouse 3' 40.0$ " +
                "2 'Mouse 2' 50.0$ " +
                "4 'Mouse 4 - the best mouse!' 66.0$ ");

        List<WebElement> checkboxes = getSelectCheckboxes();
        assertEquals(4, checkboxes.size());

        checkboxes.get(0).click();
        checkboxes.get(2).click();
        checkboxes.get(3).click();

        submitAddToCartForm();
        assertCartPage();
        assertPageContain("1 'Mouse 1' 30.0$");
        assertPageNotContain("3 'Mouse 3' 40.0$");
        assertPageContain("2 'Mouse 2' 50.0$");
        assertPageNotContain("4 'Mouse 4 - the best mouse!' 66.0$"); // это баг длает
    }

    @Test
    public void shouldSaveShoppingCart(){
        getListFor("mouse");

        assertPageContain("List: Code Description Price " +
                "1 'Mouse 1' 30.0$ " +
                "3 'Mouse 3' 40.0$ " +
                "2 'Mouse 2' 50.0$ " +
                "4 'Mouse 4 - the best mouse!' 66.0$ ");

        List<WebElement> checkboxes = getSelectCheckboxes();
        assertEquals(4, checkboxes.size());

        checkboxes.get(0).click();
        checkboxes.get(3).click();

        submitAddToCartForm();
        assertCartPage();

        enterText("monitor");
        submitSearchForm();

        assertPageContain("List: Code Description Price " +
                "6 'Monitor 2' 120.0$ " +
                "5 'Monitor 1' 150.0$ " +
                "7 'Monitor 3 - the best monitor!' 190.0$");
        checkboxes = getSelectCheckboxes();
        assertEquals(3, checkboxes.size());

        checkboxes.get(0).click();
        checkboxes.get(2).click();

        submitAddToCartForm();
        assertCartPage();

        assertPageContain("1 'Mouse 1' 30.0$");
        assertPageNotContain("3 'Mouse 3' 40.0$");
        assertPageNotContain("2 'Mouse 2' 50.0$");
        assertPageNotContain("4 'Mouse 4 - the best mouse!' 66.0$"); // это баг длает
        assertPageContain("6 'Monitor 2' 120.0$ ");
        assertPageNotContain("5 'Monitor 1' 150.0$ ");
        assertPageNotContain("7 'Monitor 3 - the best monitor!' 190.0$"); // это баг длает
    }

    @Test
    public void shouldCalculateTotalPriceCorrectly(){
        shouldAddToCartSelectedRecord();

        assertPageContain("1 'Mouse 1' 30.0$");
        assertPageContain("2 'Mouse 2' 50.0$");
        assertPageNotContain("4 'Mouse 4 - the best mouse!' 66.0$"); // это баг длает

        assertEquals("80.0$", getTotalPrice().getText()); // это баг длает
    }
}