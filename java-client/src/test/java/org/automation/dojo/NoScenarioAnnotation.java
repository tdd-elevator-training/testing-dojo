package org.automation.dojo;

import org.junit.Test;
import org.junit.runner.RunWith;

@ReportTo(server = "http://localhost:8888", userName = "vasya")
@RunWith(DojoTestRunner.class)
public class NoScenarioAnnotation {

    @Test
    public void notAnnotatedTest(){
    }
}
