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

    private WebElement getAddToCartButton() {
        return tester.findElement(By.id("add_to_cart_button"));
    }

    private WebElement getAddToCartForm() {
        return tester.findElement(By.name("add_to_cart"));
    }

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class,
                SearchByPriceScenario.class, NullBug.class,
                PriceSortingAscDescScenario.class, NullBug.class,
                AddToUserCartScenario.class, NullBug.class,
                ShowUserCartScenario.class, NullBug.class);
    }

    @Override
    @Test
    public void shouldSearchPageAsWelcomePage() {
        super.shouldSearchPageAsWelcomePage();

        assertAddToCartFormNotPresent();
    }

    @Test
    public void shouldCartFormPresentIfSomeFound() {
        enterText("mouse");
        submitSearchForm();

        assertSearchForm();
        assertAddToCartForm();
    }

    private void assertAddToCartForm() {
        assertNotNull(getAddToCartForm());
        assertNotNull(getAddToCartButton());
    }

    private void assertAddToCartFormNotPresent() {
        try {
            getAddToCartForm();
            fail("Expected exception");
        } catch (Exception e) {
        }
        try {
            getAddToCartButton();
            fail("Expected exception");
        } catch (Exception e) {
        }
    }

    @Test
    public void shouldAddToCartSelectedRecord(){

    }

}
