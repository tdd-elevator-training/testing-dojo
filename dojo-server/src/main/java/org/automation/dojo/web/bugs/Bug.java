package org.automation.dojo.web.bugs;

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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Bug && this.id == ((Bug) obj).id;
    }
}
