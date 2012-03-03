package org.automation.dojo.web.bugs;

import org.automation.dojo.Bug;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.List;
import java.util.Random;

/**
 * Вдруг перестает выводиться цена в одном из товаров
 */
public class HideSomePriceBug extends Bug<RequestWorker> {

    @Override
    public RequestWorker apply(RequestWorker result) {
        List<Record> records = result.getRecords();
        if (records != null && !records.isEmpty()) {
            int index = new Random().nextInt(records.size());
            Record record = records.get(index);
            record.setPrice(0);
        }
        return result;
    }

}
