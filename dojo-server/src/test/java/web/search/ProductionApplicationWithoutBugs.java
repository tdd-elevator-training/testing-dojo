package web.search;


import org.automation.dojo.web.bugs.NoResultWhenExpectedBug;
import org.automation.dojo.web.scenario.SearchByTextScenario;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ProductionApplicationWithoutBugs extends SearchPageLevel1 {

    @Override
    protected List<?> getMinorRelease() {
        return Arrays.asList(SearchByTextScenario.class, NoResultWhenExpectedBug.class);
    }

    @Override
    protected String getPageUrl() {
        return "/production";
    }

    // all tests extends from parent
}
