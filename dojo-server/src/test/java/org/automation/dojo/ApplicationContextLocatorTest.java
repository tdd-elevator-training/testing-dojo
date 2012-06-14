package org.automation.dojo;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.ReleaseEngine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

public class ApplicationContextLocatorTest {

    @Autowired
    private ApplicationContext context;

    private ApplicationContextLocator instance;

    @Test
    public void shouldLoadReleaseEngineByClass(){
        assertNotNull(instance.getBean(ReleaseEngine.class));
    }

    @Test
    public void shouldLoadReleaseEngineById(){
        assertNotNull(instance.getBean("releaseEngine"));
    }

}