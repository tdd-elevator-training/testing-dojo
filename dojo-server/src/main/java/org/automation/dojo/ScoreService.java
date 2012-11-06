package org.automation.dojo;

import org.automation.dojo.web.scenario.Release;

import java.util.Collection;

public interface ScoreService {

    String SUPERMAN = "superman123";
    String LOOSER = "looser321";

    void nextRelease(Release previousRelease);

    void tick(long currentTime);

    Collection<PlayerRecord> suiteResult(TestSuiteResult suite);

    void reportSuperUsers(Release previousRelease);
}
