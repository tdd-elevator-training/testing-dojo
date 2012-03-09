package org.automation.dojo.web.scenario;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.BugsQueue;
import org.automation.dojo.web.bugs.BrokenSortingBug;
import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.bugs.IgnorePriceSortingOrderBug;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.Arrays;
import java.util.List;

public class ShowUserCartScenario extends BasicScenario<RequestWorker> {

    public ShowUserCartScenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    public boolean activate(RequestWorker request) {
        return request.isCartAction();
    }

    @Override
    public String process(RequestWorker request) {
        ShopService service = ApplicationContextLocator.getInstance().getBean("shopService");

        request.saveSearchTextState();
        request.saveSearchPriceState();
        request.saveSearchPriceSortingState();

        List<Record> records = service.getUserCart("apofig");
        request.setRecords(records);

        bug.apply(request);
        return null;
    }

    public List<? extends Bug> getPossibleBugs() {
        return Arrays.asList();
    }

}
