package org.automation.dojo.web.bugs;

import org.apache.commons.lang.StringUtils;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.List;
import java.util.Random;

/**
 * Добавлять существующий продукт НЕ с тем именем, по которому ищем И ТОЛЬКО если результат поиска не пуст
 */
public class ChangeDescriptionIfListNotEmptyBug extends Bug<RequestWorker> {

    @Override
    public RequestWorker apply(RequestWorker result) {
        List<Record> list = result.getRecords();
        if (list != null &&
                !list.isEmpty() &&
                !result.isNoResultsFound() &&
                !StringUtils.isEmpty(result.getSearchText()))
        {
            int index = new Random().nextInt(list.size());
            Record record = list.get(index);
            list.set(index, new Record(getRandomString(), record.getPrice()));
        }
        return result;
    }

    public String getRandomString() {
        return String.valueOf(this.hashCode());
    }

}
