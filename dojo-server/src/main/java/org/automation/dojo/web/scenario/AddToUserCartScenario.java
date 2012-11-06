package org.automation.dojo.web.scenario;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.BugsQueue;
import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.bugs.BugsFactory;
import org.automation.dojo.web.bugs.SomeRecordsWillNotAddToCart;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.model.UserCart;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.List;

public class AddToUserCartScenario extends BasicScenario<RequestWorker> {

    public AddToUserCartScenario(int id, String description, BugsQueue bugsQueue) {
        super(id, description, bugsQueue);
    }

    @Override
    public boolean activate(RequestWorker request) {
        return request.isCartAction();
    }

    @Override
    public String process(RequestWorker request) {
        request.saveSearchTextState();
        request.saveSearchPriceState();
        request.saveSearchPriceSortingState();

        ShopService service = ApplicationContextLocator.getBean("shopService");

        UserCart cart = request.getUserCart();
        service.addToUserCart(cart, request.getRecordIds());
        request.setRecords(cart.getRecords());

        bug.apply(request);
        return null;
    }

    public List<? extends Bug> getPossibleBugs() {
        return BugsFactory.getBugs(SomeRecordsWillNotAddToCart.class);
    }


}
