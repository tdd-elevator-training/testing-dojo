package org.automation.dojo;

import org.automation.dojo.web.model.Record;

import java.util.List;

/**
 * @author serhiy.zelenin
 */
public class Bug<T> {
    public static Bug NULL_BUG = new NullBug();

    private int id;
    private int weight;

    public Bug(int id) {
        this.id = id;
    }

    public Bug() {
    }

    public int getId() {
        return id;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public T apply(T result) {
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
