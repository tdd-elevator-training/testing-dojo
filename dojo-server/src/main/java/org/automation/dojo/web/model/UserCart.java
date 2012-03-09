package org.automation.dojo.web.model;

import java.util.LinkedList;
import java.util.List;

public class UserCart {

    private List<Record> cart = new LinkedList<Record>();

    public void addAll(List<Record> records) {
        cart.addAll(records);
    }

    public List<Record> getRecords() {
        return copy(cart);
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
        for (Record record : cart) {
            result += record.getPrice();
        }
        return result;
    }

    public void remove(int index) {
        if (index < cart.size()) {
            cart.remove(index);
        }
    }
}
