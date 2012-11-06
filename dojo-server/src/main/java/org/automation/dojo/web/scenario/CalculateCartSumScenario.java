package org.automation.dojo.web.scenario;

import org.automation.dojo.BugsQueue;
import org.automation.dojo.web.bugs.BrokenChartSumBug;
import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.bugs.BugsFactory;
import org.automation.dojo.web.model.UserCart;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.List;

public class CalculateCartSumScenario extends BasicScenario<RequestWorker> {

    public CalculateCartSumScenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    public boolean activate(RequestWorker request) {
        return request.isCartAction();
    }

    @Override
    public String process(RequestWorker request) {
        UserCart cart = request.getUserCart();
        request.setTotalSum(cart.getTotalPrice());

        bug.apply(request);
        return null;
    }

    public List<? extends Bug> getPossibleBugs() {
        return BugsFactory.getBugs(BrokenChartSumBug.class);
    }

}
