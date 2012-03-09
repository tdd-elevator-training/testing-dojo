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
 * начало списка один элемент с ценой меньше, если он не был пуст.
 */
public class AddExistingItemWithPriceLessThanEntered extends Bug<RequestWorker> {

    public AddExistingItemWithPriceLessThanEntered() {
        super(4);
    }

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
        List<Record> filtered = shop.priceFilter(all,
                ShopService.LESS_THAN, request.getPrice() +
                0.000001); // 0.000001 - хак, чтобы не учитывать текущий элемент
        if (filtered.size() == 0) {
            return null;
        }
        return filtered.get(0);
    }


}
