package org.automation.dojo.web.bugs;

import org.apache.commons.lang.StringUtils;
import org.automation.dojo.Bug;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.servlet.RequestWorker;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

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
