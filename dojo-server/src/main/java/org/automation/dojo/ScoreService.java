package org.automation.dojo;

import org.automation.dojo.web.scenario.Release;

import java.util.Collection;
import java.util.Map;

public interface ScoreService {

    void nextRelease(Release previousRelease);

    void tick(long currentTime);

    Collection<PlayerRecord> suiteResult(TestSuiteResult suite);
}
