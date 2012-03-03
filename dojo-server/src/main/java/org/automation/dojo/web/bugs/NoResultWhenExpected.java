package org.automation.dojo.web.bugs;

import org.apache.commons.lang.StringUtils;
import org.automation.dojo.Bug;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.servlet.RequestWorker;

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
