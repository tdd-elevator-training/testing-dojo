package org.automation.dojo.web.servlet;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.ReleaseEngine;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.servlet.ServletException;
import java.io.IOException;

public class SearchController extends Controller {

    @Override
    public String doAction() throws ServletException, IOException {
        ApplicationContextLocator context = ApplicationContextLocator.getInstance();
        context.setApplicationContext(new FileSystemXmlApplicationContext("classpath:/org/automation/dojo/applicationContext.xml"));

        ReleaseEngine engine = context.getBean(ReleaseEngine.class);
        engine.init();

        return engine.getScenario(0).process(new RequestWorker(super.request));
    }
}