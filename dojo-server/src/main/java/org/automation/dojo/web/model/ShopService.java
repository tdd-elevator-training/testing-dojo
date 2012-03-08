package org.automation.dojo.web.model;

import java.util.List;

public interface ShopService {

    static final int MORE_THAN = 1;
    static final int LESS_THAN = 2;
    static final int IGNORE = 0;

    List<Record> selectByText(String foundString);

    List<Record> priceFilter(List<Record> records, int priceOptionIndex, Double price);
}
