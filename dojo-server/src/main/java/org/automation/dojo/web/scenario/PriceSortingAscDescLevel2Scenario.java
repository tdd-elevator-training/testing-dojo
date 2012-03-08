package org.automation.dojo.web.scenario;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.BugsQueue;
import org.automation.dojo.web.bugs.*;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.Arrays;
import java.util.List;

public class PriceSortingAscDescLevel2Scenario extends BasicScenario<RequestWorker> {

    public PriceSortingAscDescLevel2Scenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    public String process(RequestWorker request) {
        ShopService service = ApplicationContextLocator.getInstance().getBean("shopService");

        List<Record> records = request.getRecords();
        if (records != null && !records.isEmpty()) {
            List<Record> result = service.sortByPrice(records,
                    request.isAsc());
            request.setPriceSortingOrderOption(request.isAsc());
            request.setRecords(result);
        }

        bug.apply(request);
        return "search_level2.jsp";
    }

    public List<? extends Bug> getPossibleBugs() {
        return Arrays.asList(new BrokenSortingBug(),
                new IgnorePriceSortingOrderBug());
    }

}
