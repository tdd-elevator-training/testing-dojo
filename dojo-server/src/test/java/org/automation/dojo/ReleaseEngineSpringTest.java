package org.automation.dojo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

/**
 * @author serhiy.zelenin
 */
@ContextConfiguration(locations = {"classpath:/org/automation/dojo/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ReleaseEngineSpringTest {
    @Autowired private ReleaseEngine releaseEngine;

    @Test
    public void testScenariosCreated() {
        assertEquals(1, releaseEngine.getScenario(1).getId());
    }

}
