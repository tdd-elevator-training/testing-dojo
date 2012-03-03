package org.automation.dojo;

/**
 * @author serhiy.zelenin
 */
public class Bug {
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
}
