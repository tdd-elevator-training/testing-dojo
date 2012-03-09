import org.automation.dojo.DojoTestRunner;
import org.automation.dojo.ReportTo;
import org.automation.dojo.Scenario;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

@RunWith(DojoTestRunner.class)
@ReportTo(server = "http://127.0.0.1:8080", userName = "Sergey")
public class SampleAutomationTest {

    @Test
    @Scenario(1)
    public void searchForProduct() {
        //put test automation code here
//        fail();
    }

    @Test
    @Scenario(2)
    public void searchForProduct2() {
        //put test automation code here
        fail();
    }

    @Test
    @Scenario(3)
    public void searchForProduct3() {
        //put test automation code here
//        fail();
    }

    @Test
    @Scenario(4)
    public void searchForProduct4() {
        //put test automation code here
//        fail();
    }
}
