package org.automation.dojo.web.bugs;

import org.automation.dojo.web.servlet.RequestWorker;

/**
 * Каким бы мы не выбрали порядок сортировки, он всеравно проигнорируется
 */
public class IgnorePriceSortingOrderBug extends Bug<RequestWorker> {

    @Override
    public RequestWorker apply(RequestWorker result) {
        result.setPriceSortingOrderOption(RequestWorker.ASC);
        return result;
    }

}
