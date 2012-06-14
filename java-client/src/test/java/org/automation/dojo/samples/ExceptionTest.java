package org.automation.dojo.samples;

import org.automation.dojo.DojoTestRunner;
import org.automation.dojo.ReportTo;
import org.automation.dojo.Scenario;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author serhiy.zelenin
 */
@ReportTo(server = "http://localhost:1111", userName = "vasya")
@RunWith(DojoTestRunner.class)
public class ExceptionTest {
    @Test
    @Scenario(1)
    public void testException() {
        throw new NullPointerException();
    }
}
