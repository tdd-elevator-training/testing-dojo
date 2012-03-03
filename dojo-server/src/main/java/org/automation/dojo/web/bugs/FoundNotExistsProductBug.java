package org.automation.dojo.web.bugs;

import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.Arrays;

/**
 * Добавлять существующий продукт ТОЛЬКО если результат поиска пуст
 */
public class FoundNotExistsProductBug extends Bug<RequestWorker> {

    @Override
    public RequestWorker apply(RequestWorker result) {
        if (result.isNoResultsFound()) {
            result.setRecords(Arrays.asList(new Record(result.getSearchText(), 100500)));
            result.clearNoResultsFound();
        }
        return result;
    }

}
