package org.automation.dojo.web.bugs;

import org.apache.commons.lang.StringUtils;
import org.automation.dojo.Bug;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.servlet.RequestWorker;

import java.util.LinkedList;

/**
 * Ничего не отобразилось, когда выводится весь список при пустом запросе
 */

public class NoResultWhenExpected extends Bug<RequestWorker> {

    @Override
    public RequestWorker apply(RequestWorker result) {
        if (StringUtils.isBlank(result.getSearchText())) {
            result.setRecords(null);
            result.noResultsFound();
        }
        return result;
    }

}
