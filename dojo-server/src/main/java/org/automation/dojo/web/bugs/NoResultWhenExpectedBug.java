package org.automation.dojo.web.bugs;

import org.apache.commons.lang.StringUtils;
import org.automation.dojo.web.servlet.RequestWorker;

/**
 * Ничего не отобразилось, когда выводится весь список при пустом запросе
 */
// TODO написать юнит тест
public class NoResultWhenExpectedBug extends Bug<RequestWorker> {

    public NoResultWhenExpectedBug(int id) {
        super(id);
    }

    @Override
    public RequestWorker apply(RequestWorker result) {
        if (StringUtils.isBlank(result.getSearchText())) {
            result.setRecords(null);
            result.noResultsFound();
        }
        return result;
    }

}
