import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: serhiy.zelenin
 * Date: 6/10/12
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String[] testResults = new String[]{"PASSED", "FAILED"};
        String[] reportedBugInPrevs = new String[]{"Yes", "No"};
        String[] reportedLiarInPrevs = new String[]{"Yes", "No"};
        String[] suiteResults = new String[]{"PASSED", "FAILED"};
        String[] hasBugs = new String[]{"true", "false"};
        String[] reportedBugInSuites = new String[]{"Yes", "No"};
        String[] reportedLiarInSuites = new String[]{"Yes", "No"};
        File file = new File("D:\\workspace\\projects\\testing-dojo\\all.csv");
        List<String> strings = new ArrayList<String>();
        strings.add("Test Result, Reported in prev, Suite result, has bug, Reported in suite, Reported Liar in prev, Reported Liar in suite");
        for (String testResult : testResults) {
            for (String reportedInPrev : reportedBugInPrevs) {
                for (String suiteResult : suiteResults) {
                    for (String hasBug : hasBugs) {
                        for (String reportedInSuite : reportedBugInSuites) {
                            for (String reportedLiarInSuite : reportedLiarInSuites) {
                                for (String reportedLiarInPrev : reportedLiarInPrevs) {
                                    strings.add(testResult + ", " + reportedInPrev + "," + suiteResult + "," + hasBug + "," + reportedInSuite + ","+reportedLiarInPrev+","+reportedLiarInSuite);
                                }
                            }
                        }
                    }
                }
            }
        }
        FileUtils.writeLines(file, strings);
    }
}
