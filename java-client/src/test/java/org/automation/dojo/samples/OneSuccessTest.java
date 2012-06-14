package org.automation.dojo.samples;

import org.automation.dojo.ReportTo;
import org.automation.dojo.Scenario;
import org.junit.Test;

@ReportTo(server = "http://localhost:1111", userName = "Sergey")
public class OneSuccessTest {

    @Test
    @Scenario(1)
    public void shouldPass(){
    }

}
