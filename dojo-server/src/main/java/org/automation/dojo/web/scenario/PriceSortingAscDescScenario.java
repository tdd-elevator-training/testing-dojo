package org.automation.dojo.web.scenario;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.BugsQueue;
import org.automation.dojo.web.bugs.*;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.Arrays;
import java.util.List;

public class PriceSortingAscDescScenario extends BasicScenario<RequestWorker> {

    public PriceSortingAscDescScenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    public boolean activate(RequestWorker request) {
        return request.isSearchAction();
    }

    @Override
    public String process(RequestWorker request) {
        ShopService service = ApplicationContextLocator.getInstance().getBean("shopService");

        request.saveSearchPriceSortingState();

        List<Record> records = request.getRecords();
        if (records != null && !records.isEmpty()) {
            List<Record> result = service.sortByPrice(records,
                    request.isAsc());
            request.setRecords(result);
        }

        bug.apply(request);
        return null;
    }

    public List<? extends Bug> getPossibleBugs() {
        return Arrays.asList(new BrokenSortingBug(),
                new IgnorePriceSortingOrderBug());
    }

}
