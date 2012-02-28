package org.automation.dojo;

import org.junit.Test;

import static junit.framework.Assert.fail;

@ReportTo(server = "http://localhost:8888", userName = "petya")
public class OneFailedTest {

    @Test
    @Scenario(33)
    public void shouldWhen() {
        fail();
    }
}
