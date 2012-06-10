package org.automation.dojo;

import org.automation.dojo.web.scenario.Release;

import java.util.Map;

public interface ScoreService {

    void nextRelease(Release previousRelease);

    void tick(long currentTime);

    Map<Integer, Boolean> suiteResult(TestSuiteResult suite);
}
