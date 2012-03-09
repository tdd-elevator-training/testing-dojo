package org.automation.dojo.web.bugs;

import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.UserCart;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.List;

public class SomeRecordsWillNotAddToCart extends Bug<RequestWorker>{

    public SomeRecordsWillNotAddToCart(int id) {
        super(id);
    }

    @Override
    public RequestWorker apply(RequestWorker request) {
        List<Integer> recordIds = request.getRecordIds();
        if (recordIds.size() != 0) {
            UserCart cart = request.getUserCart();
            List<Record> records = cart.getRecords();
            cart.remove(records.size() - 1);
        }
        return request;
    }

}
