package org.automation.dojo.web.bugs;

import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.List;
import java.util.Random;

/**
 * Поменять местами 2 элемента в списке с разными ценами
 */
// TODO написать юнит тест
public class BrokenSortingBug extends Bug<RequestWorker> {

    public BrokenSortingBug() {
        super(6);
    }

    @Override
    public RequestWorker apply(RequestWorker result) {
        List<Record> records = result.getRecords();
        if (records != null && !records.isEmpty() && records.size() > 1) {
            int i = 0;
            int j = 0;
            do {
                i = new Random().nextInt(records.size());
                j = new Random().nextInt(records.size());
            } while (i == j);
            Record record = records.get(i);
            records.set(i, records.get(j));
            records.set(j, record);
        }
        return result;
    }

}
