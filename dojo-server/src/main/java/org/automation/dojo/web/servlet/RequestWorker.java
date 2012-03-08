package org.automation.dojo.web.servlet;

import org.automation.dojo.web.model.Record;

import java.util.List;

public interface RequestWorker {
    List<Record> getRecords();

    void setRecords(List<Record> filtered);

    boolean isNoResultsFound();

    void noResultsFound();

    void saveFormState();

    String getSearchText();

    String getPriceSearchOption();

    Double getPrice();

    String getStringPrice();

    int getPriceOptionIndex();

    void clearNoResultsFound();

    boolean isAsc();

    String getPriceSortingOrderOption();

    void setPriceSortingOrderOption(boolean isAsc);
}
