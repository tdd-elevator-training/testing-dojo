package org.automation.dojo;

import org.junit.Test;

@ReportTo(server = "http://localhost:1111", userName = "Sergey")
public class OneSuccessTest {

    @Test
    @Scenario(1)
    public void shouldPass(){
    }

}
