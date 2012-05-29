package org.automation.dojo.web.bugs;

import org.automation.dojo.web.servlet.RequestWorker;

public class NoSearchTextMaxLengthBug extends Bug<RequestWorker>{

    public NoSearchTextMaxLengthBug(int id) {
        super(id);
    }

    @Override
    public RequestWorker apply(RequestWorker request) {
        request.setSearchTextMaxLength(Integer.MAX_VALUE);
        return request;
    }

}
