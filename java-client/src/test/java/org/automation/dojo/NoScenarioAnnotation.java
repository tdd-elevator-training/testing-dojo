package org.automation.dojo;

import org.junit.Test;

@ReportTo(server = "http://localhost:8888", userName = "vasya")
public class NoScenarioAnnotation {

    @Test
    @Scenario(1)
    public void shouldWhen(){
    }

    @Test
    public void notAnnotatedTest(){
    }
}
