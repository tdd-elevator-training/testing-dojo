package web.search;


import org.automation.dojo.web.bugs.DisabledPriceValidationBug;
import org.automation.dojo.web.bugs.NullBug;
import org.automation.dojo.web.scenario.PriceSortingAscDescScenario;
import org.automation.dojo.web.scenario.SearchByPriceScenario;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.automation.dojo.web.model.ShopService.MORE_THAN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SearchPageLevel2_Bug5 extends SearchPageLevel2 {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NullBug.class,
                SearchByPriceScenario.class, DisabledPriceValidationBug.class,
                PriceSortingAscDescScenario.class, NullBug.class);
    }

    @Override
    protected void resetAllElements() {
        try {
            super.resetAllElements();
        } catch (Exception e) {
            // do nothing
        }
    }

    @Override
    @Test
    public void mustBeAPriceValidation() {
        isValidationInfo("");
        enterPrice("qwe");
        enterText("mouse");
        selectPriceOption(MORE_THAN);
        isValidationInfo("");
        search();

        isError("java.lang.NumberFormatException: For input string: \"qwe\""); // это баг делает
    }

    @Test
    public void mustBeAPriceValidationWhenBlur() {
        isValidationInfo("");
        enterPrice("qwe");
        searchText.click();
        isValidationInfo(""); // это баг делает
    }


}
