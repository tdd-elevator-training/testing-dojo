package org.automation.dojo.web.servlet;

import org.apache.commons.lang.StringUtils;
import org.automation.dojo.Bug;
import org.automation.dojo.web.model.Record;

import java.util.LinkedList;

public class NoResultWhenExpected extends Bug<RequestWorker> {

    @Override
    public RequestWorker apply(RequestWorker result) {
        if (StringUtils.isBlank(result.getSearchText())) {
            result.setRecords(new LinkedList<Record>());
            result.noResultsFound();
        }
        return result;
    }

}
