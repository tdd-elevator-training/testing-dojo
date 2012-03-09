package org.automation.dojo.web.bugs;

import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.UserCart;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.List;

public class BrokenChartSum extends Bug<RequestWorker>{

    public BrokenChartSum(int id) {
        super(id);
    }

    @Override
    public RequestWorker apply(RequestWorker request) {
        request.setTotalSum(request.getUserCart().getTotalPrice() + 100500);
        return request;
    }

}
