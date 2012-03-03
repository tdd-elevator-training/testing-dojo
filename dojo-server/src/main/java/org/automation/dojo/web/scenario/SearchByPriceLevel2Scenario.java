package org.automation.dojo.web.scenario;

import org.automation.dojo.Bug;
import org.automation.dojo.BugsQueue;
import org.automation.dojo.Scenario;
import org.automation.dojo.web.bugs.NoResultWhenExpected;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.model.ShopServiceFactory;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.Arrays;
import java.util.List;

public class SearchByPriceLevel2Scenario extends Scenario<RequestWorker> {

    public SearchByPriceLevel2Scenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    public SearchByPriceLevel2Scenario() {
    }

    @Override
    public String process(RequestWorker request) {
        ShopService service = ShopServiceFactory.gtInstance();

        List<Record> records = request.getRecords();
        if (records != null) {
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
        return Arrays.asList(new NoResultWhenExpected());
    }

}
