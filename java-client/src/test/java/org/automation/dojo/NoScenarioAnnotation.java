package org.automation.dojo;

import org.junit.Test;
import org.junit.runner.RunWith;

@ReportTo(server = "http://localhost:1111", userName = "vasya")
@RunWith(DojoTestRunner.class)
public class NoScenarioAnnotation {

    @Test
    public void notAnnotatedTest(){
    }
}
