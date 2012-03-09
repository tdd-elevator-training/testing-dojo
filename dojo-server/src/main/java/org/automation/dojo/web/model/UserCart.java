package org.automation.dojo.web.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserCart {

    private List<Record> сart = new LinkedList<Record>();

    public void addAll(List<Record> records) {
        сart.addAll(records);
    }

    public List<Record> getRecords() {
        return copy(сart);
    }

    private List<Record> copy(List<Record> data) {
        List<Record> result = new LinkedList<Record>();
        for (Record record : data) {
            result.add(record.clone());
        }
        return result;
    }

    public double getTotalPrice() {
        double result = 0;
        for (Record record : сart) {
            result += record.getPrice();
        }
        return result;
    }
}
