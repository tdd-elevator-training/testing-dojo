package org.automation.dojo.web.servlet;

import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.UserCart;

import java.util.List;

public interface RequestWorker {

    public static boolean ASC = true;
    public static boolean DESC = !ASC;

    List<Record> getRecords();

    void setRecords(List<Record> filtered);

    boolean isNoResultsFound();

    void noResultsFound();

    String getSearchText();

    void saveSearchTextState();

    void saveSearchPriceState();

    void saveSearchPriceSortingState();

    String getPriceSearchOption();

    Double getPrice();

    String getStringPrice();

    int getPriceOptionIndex();

    void clearNoResultsFound();

    boolean isAsc();

    String getPriceSortingOrderOption();

    void setPriceSortingOrderOption(boolean isAsc);

    List<Integer> getRecordIds();

    boolean isSearchAction();

    boolean isCartAction();

    UserCart getUserCart();

    void setTotalSum(double price);

    void setValidatePriceNumber(boolean isEnabled);

    void setSearchTextMaxLength(int maxLength);
}
