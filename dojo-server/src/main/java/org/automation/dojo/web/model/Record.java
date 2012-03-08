package org.automation.dojo.web.model;

import org.apache.commons.lang.builder.EqualsBuilder;

public class Record implements Cloneable {
    private String description;
    private double price;

    public Record(String description, double price) {
        this.description = description;
        this.price = price;
    }

    public Record(Record record) {
        this(record.getDescription(), record.getPrice());
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("Record '%s' with price $%s", description, price);
    }

    @Override
    public Record clone() {
        return new Record(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

}
