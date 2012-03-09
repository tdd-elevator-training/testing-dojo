package org.automation.dojo.web.model;

import org.apache.commons.lang.builder.EqualsBuilder;

public class Record implements Cloneable {
    private String description;
    private double price;
    private int id;

    public Record(int id, String description, double price) {
        this.id = id;
        this.description = description;
        this.price = price;
    }

    public Record(Record record) {
        this(record.id, record.getDescription(), record.getPrice());
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean itsMe(Integer id) {
        return this.id == id;
    }
}
