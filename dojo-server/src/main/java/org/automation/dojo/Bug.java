package org.automation.dojo;

/**
 * @author serhiy.zelenin
 */
public class Bug {
    public static Bug NULL_BUG = new NullBug();

    private int id;

    public Bug(int id) {
        this.id = id;
    }

    public Bug() {
    }

    public int getId() {
        return id;
    }
}
