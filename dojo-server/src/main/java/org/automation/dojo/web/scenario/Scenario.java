package org.automation.dojo.web.scenario;

import org.automation.dojo.web.servlet.RequestWorker;

public interface Scenario<T> {
    void takeNextBug();

    String process(T request);

    void setNoBug();

    boolean activate(T request);
}
