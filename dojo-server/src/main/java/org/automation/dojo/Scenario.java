package org.automation.dojo;

/**
 * @author serhiy.zelenin
 */
public class Scenario {
    private int id;
    private Bug bug;

    public Scenario(int id) {

        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Bug getBug() {
        return bug;
    }
}
