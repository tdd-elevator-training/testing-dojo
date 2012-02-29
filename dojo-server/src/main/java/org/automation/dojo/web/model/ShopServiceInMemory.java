package org.automation.dojo.web.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ShopServiceInMemory implements ShopService {

    private List<Record> data;

    public ShopServiceInMemory() {
        data = new LinkedList<Record>();
        data.add(new Record("Mouse 1", 30));
        data.add(new Record("Mouse 2", 50));
        data.add(new Record("Mouse 3", 40));
        data.add(new Record("Mouse 4 - the best mouse!", 66));
        data.add(new Record("Monitor 1", 150));
        data.add(new Record("Monitor 2", 120));
        data.add(new Record("Monitor 3 - the best monitor!", 190));
    }

    @Override
    public List<Record> select(String foundString, int priceOption, double price) {
        List<Record> foundByText = foundTextAtDescription(data, foundString);
        List<Record> foundByPrice = removeByPrice(priceOption, price, foundByText);
        List<Record> result = sortByPrice(foundByPrice);
        return result;
    }

    private List<Record> removeByPrice(int priceOption, double price, List<Record> records) {
        if (priceOption == IGNORE) {
            return records;
        }

        List<Record> result = new LinkedList<Record>();

        for (Record record : records) {
            if ((priceOption == MORE_THAN && record.getPrice() >= price) ||
                    (priceOption == LESS_THAN && record.getPrice() <= price)) {
                result.add(record);
            }
        }

        return result;
    }

    private List<Record> sortByPrice(List<Record> records) {
        List<Record> result = new LinkedList<Record>(records);

        Collections.sort(result, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return new Double(o1.getPrice()).compareTo(new Double(o2.getPrice()));
            }
        });

        return result;
    }

    private List<Record> foundTextAtDescription(List<Record> list, String foundString) {
        List<Record> result = new LinkedList<Record>();
        for (Record record : list) {
            if (record.getDescription().toLowerCase().contains(foundString.toLowerCase())) {
                result.add(record);
            }
        }
        return result;
    }
}
