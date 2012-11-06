package org.automation.dojo.web.scenario;

public interface Scenario<T> {
    void takeNextBug();

    String process(T request);

    void setNoBug();

    boolean activate(T request);
}
