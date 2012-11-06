package org.automation.dojo.web.bugs;

import org.automation.dojo.web.servlet.RequestWorker;

public class BrokenChartSumBug extends Bug<RequestWorker>{

    public BrokenChartSumBug(int id) {
        super(id);
    }

    @Override
    public RequestWorker apply(RequestWorker request) {
        request.setTotalSum(request.getUserCart().getTotalPrice() + 100500);
        return request;
    }

}
