package org.automation.dojo.web.bugs;

import java.io.Serializable;

/**
 * @author serhiy.zelenin
 */
public class Bug<T> implements Serializable {

    private static final long serialVersionUID = -3599003903578159919L;

    public static Bug NULL_BUG = BugsFactory.getBug(NullBug.class);

    private int id;
    private int weight = 100;

    public Bug(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Bug bug = (Bug) o;

        if (id != bug.id) {
            return false;
        }

        return true;
    }
}
