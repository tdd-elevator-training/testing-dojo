package org.automation.dojo.web.bugs;

import org.apache.commons.lang.StringUtils;
import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.servlet.RequestWorker;
import org.automation.dojo.web.servlet.RequestWorkerImpl;

import java.util.List;

/**
 * Если выбран режим поиска по прайсу (опция "больше чем"), тогда добавлять в
 * список один элемент с ценой меньше, если он не был пуст.
 */
public class AddExistingItemWithPriceLessThanEntered extends Bug<RequestWorker> {

    private static final int MORE_THAN = 1;

    @Override
    public RequestWorker apply(RequestWorker result) {
        List<Record> list = result.getRecords();
        if (list != null &&
           !list.isEmpty() &&
           !result.isNoResultsFound() &&
           !StringUtils.isEmpty(result.getStringPrice()) &&
           result.getPriceOptionIndex() == ShopService.MORE_THAN)
        {
            Record otherRecord = findLessThan(result);
            if (otherRecord != null) {
                list.add(0, otherRecord);
            }
        }
        return result;
    }

    private Record findLessThan(RequestWorker request) {
        ShopService shop = ApplicationContextLocator.getInstance().getBean("shopService");
        List<Record> all = shop.selectByText(request.getSearchText());
        List<Record> filtered = shop.priceFilter(all, ShopService.LESS_THAN, request.getPrice());
        if (filtered.size() < 2) { // два потому как один элемент уже включен, условие LESS_THAN = это LESS_THAN_AND_EQUALS
            return null;
        }
        return filtered.get(filtered.size() - 2);
    }


}
