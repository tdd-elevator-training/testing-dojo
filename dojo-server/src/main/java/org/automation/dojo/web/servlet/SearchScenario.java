package org.automation.dojo.web.servlet;

import org.automation.dojo.Bug;
import org.automation.dojo.BugsQueue;
import org.automation.dojo.Scenario;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.model.ShopServiceFactory;

import java.util.Arrays;
import java.util.List;

public class SearchScenario extends Scenario<RequestWorker> {

    public SearchScenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    public String process(RequestWorker request) {
        ShopService service = ShopServiceFactory.gtInstance();

        request.saveFormState();

        String foundString = request.getSearchText();
        if (foundString != null) {
            List<Record> result = service.select(foundString, request.getPriceOptionIndex(), request.getPrice());

            if (result.isEmpty()) {
                result = service.select("", ShopService.IGNORE, 0);
                request.noResultsFound();
            }

            request.setRecords(result);
        }

        bug.apply(request);
        return "search.jsp";
    }

    public List<? extends Bug> getPossibleBugs() {
        return Arrays.asList(new NoResultWhenExpected());
    }

}