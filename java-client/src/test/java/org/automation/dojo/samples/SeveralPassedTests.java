package org.automation.dojo.samples;

import org.automation.dojo.DojoTestRunner;
import org.automation.dojo.ReportTo;
import org.automation.dojo.Scenario;
import org.junit.Test;
import org.junit.runner.RunWith;

@ReportTo(server = "http://localhost:1111/", userName = "vasya")
@RunWith(DojoTestRunner.class)
public class SeveralPassedTests {

    @Test
    @Scenario(1)
    public void shouldPass1(){
    }

    @Test
    @Scenario(2)
    public void shouldPass2(){
    }
}
