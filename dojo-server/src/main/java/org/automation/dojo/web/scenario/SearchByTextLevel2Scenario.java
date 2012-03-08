package org.automation.dojo.web.scenario;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.BugsQueue;
import org.automation.dojo.web.bugs.AddSomeOtherElementIfListNotEmptyBug;
import org.automation.dojo.web.bugs.FoundNotExistsProductBug;
import org.automation.dojo.web.bugs.NoResultWhenExpectedBug;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.Arrays;
import java.util.List;

public class SearchByTextLevel2Scenario extends BasicScenario<RequestWorker> {

    public SearchByTextLevel2Scenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    public String process(RequestWorker request) {
        ShopService service = ApplicationContextLocator.getInstance().getBean("shopService");

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
        return "search_level2.jsp";
    }

    public List<? extends Bug> getPossibleBugs() {
        return Arrays.asList(new NoResultWhenExpectedBug(),
                new AddSomeOtherElementIfListNotEmptyBug(),
                new FoundNotExistsProductBug());
    }

}
