package org.automation.dojo.web.servlet;

import javax.servlet.ServletException;
import java.io.IOException;

public class SearchController extends Controller {

    @Override
    public String doAction() throws ServletException, IOException {
        return new SearchScenario().process(new RequestWorker(super.request));
    }
}