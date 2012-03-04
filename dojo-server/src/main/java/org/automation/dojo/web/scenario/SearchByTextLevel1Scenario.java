package org.automation.dojo.web.scenario;

import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.bugs.ChangeDescriptionIfListNotEmptyBug;
import org.automation.dojo.web.bugs.FoundNotExistsProductBug;
import org.automation.dojo.web.bugs.NoResultWhenExpectedBug;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.model.ShopServiceFactory;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.Arrays;
import java.util.List;

public class SearchByTextLevel1Scenario extends BasicScenario<RequestWorker> {

    public SearchByTextLevel1Scenario(int id, String description) {
        super(id, description);
    }

    @Override
    public String process(RequestWorker request) {
        ShopService service = ShopServiceFactory.gtInstance();

        request.saveFormState();

        String foundString = request.getSearchText();
        if (foundString != null) {
            List<Record> result = service.selectByText(foundString);

            if (result.isEmpty()) {
                result = service.selectByText("");
                request.noResultsFound();
            }

            request.setRecords(result);
        }

        bug.apply(request);
        return "search_level1.jsp";
    }

    public List<? extends Bug> getPossibleBugs() {
        return Arrays.asList(new NoResultWhenExpectedBug(),
                new ChangeDescriptionIfListNotEmptyBug(),
                new FoundNotExistsProductBug());
    }

}
