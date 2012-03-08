package org.automation.dojo.web.scenario;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.web.bugs.AddExistingItemWithPriceLessThanEntered;
import org.automation.dojo.web.bugs.AddExistingItemWithPriceMoreThanEntered;
import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.BugsQueue;
import org.automation.dojo.web.bugs.BrokenSortingBug;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.Arrays;
import java.util.List;

public class SearchByPriceLevel2Scenario extends BasicScenario<RequestWorker> {

    public SearchByPriceLevel2Scenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    public String process(RequestWorker request) {
        ShopService service = ApplicationContextLocator.getInstance().getBean("shopService");

        List<Record> records = request.getRecords();
        if (records != null &&
            !request.isNoResultsFound()) {
            List<Record> result = service.priceFilter(records,
                    request.getPriceOptionIndex(), request.getPrice());

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
        return Arrays.asList(new AddExistingItemWithPriceLessThanEntered(),
                new AddExistingItemWithPriceMoreThanEntered());
    }

}
