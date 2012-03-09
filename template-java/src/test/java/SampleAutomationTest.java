import org.automation.dojo.DojoTestRunner;
import org.automation.dojo.ReportTo;
import org.automation.dojo.Scenario;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DojoTestRunner.class)
@ReportTo(server = "http://127.0.0.1:8080", userName = "JohnDoe")
public class SampleAutomationTest {

    @Test
    @Scenario(1)
    public void searchForProduct(){
        //put test automation code here
    }
}
