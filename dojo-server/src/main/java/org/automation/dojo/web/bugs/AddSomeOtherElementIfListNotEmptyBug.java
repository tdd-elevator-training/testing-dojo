package org.automation.dojo.web.bugs;

import org.apache.commons.lang.StringUtils;
import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.List;
import java.util.Random;

/**
 * Добавлять существующий продукт НЕ с тем именем, по которому ищем И ТОЛЬКО если результат поиска не пуст
 * Мы искали по мышке, но добавить монитор, к примеру
 */
public class AddSomeOtherElementIfListNotEmptyBug extends Bug<RequestWorker> {

    @Override
    public RequestWorker apply(RequestWorker result) {
        List<Record> list = result.getRecords();
        if (list != null &&
           !list.isEmpty() &&
           !result.isNoResultsFound() &&
           !StringUtils.isEmpty(result.getSearchText()))
        {
            Record otherRecord = findNew(list);
            addWithoutBrokenSorting(list, otherRecord);
        }
        return result;
    }

    /**
     * Добавляем новый элемент без поломки сортировки по цене
     */
    private void addWithoutBrokenSorting(List<Record> list, Record otherRecord) {
        int index = 0;
        for (index = 0; index < list.size(); index ++) {
            Record record = list.get(index);
            if (record.getPrice() > otherRecord.getPrice()) {
                break;
            }
        }
        list.add(index, otherRecord);
    }

    /**
     * Ищем такой элемент, которого еще нет в исходном списке
     */
    private Record findNew(List<Record> list) {
        ShopService shop = ApplicationContextLocator.getInstance().getBean("shopService");
        List<Record> all = shop.selectByText("");
        for (Record record : all) {
            if (!list.contains(record)) {
                return record;
            }
        }
        return all.get(0); // не должно случиться :) Возвращаю хоть что-то, чтобы не поломать игру
    }
}
