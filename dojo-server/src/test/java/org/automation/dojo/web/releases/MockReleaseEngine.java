package org.automation.dojo.web.releases;

import org.automation.dojo.ReleaseEngine;
import java.util.LinkedList;
import java.util.List;

public class MockReleaseEngine extends ReleaseEngine {

    private static List<String> history = new LinkedList<String>();

    public void nextMajorRelease() {
        history.add("nextMajorRelease");
    }

    public void nextMinorRelease() {
        history.add("nextMinorRelease");
    }

    public static String pullHistory() {
        String result = history.toString();
        history.clear();
        return result;
    }
}
