package org.automation.dojo.web.bugs;

import org.apache.commons.lang.StringUtils;
import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.List;

/**
 * Если выбран режим поиска по прайсу (опция "меньше чем"), тогда добавлять в
 * конец списка один элемент с ценой больше, если он не был пуст.
 */
public class AddExistingItemWithPriceMoreThanEntered extends Bug<RequestWorker> {

    @Override
    public RequestWorker apply(RequestWorker result) {
        List<Record> list = result.getRecords();
        if (list != null &&
           !list.isEmpty() &&
           !result.isNoResultsFound() &&
           !StringUtils.isEmpty(result.getStringPrice()) &&
           result.getPriceOptionIndex() == ShopService.LESS_THAN)
        {
            Record otherRecord = findLessThan(result);
            if (otherRecord != null) {
                list.add(otherRecord);
            }
        }
        return result;
    }

    private Record findLessThan(RequestWorker request) {
        ShopService shop = ApplicationContextLocator.getInstance().getBean("shopService");
        List<Record> all = shop.selectByText(request.getSearchText());
        List<Record> filtered = shop.priceFilter(all, ShopService.MORE_THAN, request.getPrice());
        if (filtered.size() < 2) { // два потому как один элемент уже включен, условие LESS_THAN = это LESS_THAN_AND_EQUALS
            return null;
        }
        return filtered.get(filtered.size() - 2);
    }


}
