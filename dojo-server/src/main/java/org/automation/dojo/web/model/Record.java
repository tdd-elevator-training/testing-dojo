package org.automation.dojo.web.model;

public class Record {
    private String description;
    private double price;

    public Record(String description, double price) {
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}
