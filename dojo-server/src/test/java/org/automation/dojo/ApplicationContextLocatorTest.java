package org.automation.dojo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

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