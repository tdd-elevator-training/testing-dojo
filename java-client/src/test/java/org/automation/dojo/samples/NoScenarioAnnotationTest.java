package org.automation.dojo.samples;

import org.automation.dojo.DojoTestRunner;
import org.automation.dojo.ReportTo;
import org.junit.Test;
import org.junit.runner.RunWith;

@ReportTo(server = "http://localhost:1111", userName = "vasya")
@RunWith(DojoTestRunner.class)
public class NoScenarioAnnotationTest {

    @Test
    public void notAnnotatedTest(){
    }
}
