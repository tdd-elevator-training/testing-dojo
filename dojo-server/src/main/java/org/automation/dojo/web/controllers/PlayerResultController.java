package org.automation.dojo.web.controllers;

import org.automation.dojo.*;
import org.automation.dojo.web.scenario.BasicScenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/result")
public class PlayerResultController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private ReleaseEngine releaseEngine;

    @Autowired
    private TimeService timeService;
    private Pattern pattern = Pattern.compile("scenario(\\d*)", Pattern.CASE_INSENSITIVE);

    public PlayerResultController(ScoreService service, TimeService timeService, ReleaseEngine releaseEngine) {
        this.scoreService = service;
        this.releaseEngine = releaseEngine;
        this.timeService = timeService;
    }

    public PlayerResultController() {
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET})
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        TestSuiteResult suite = new TestSuiteResult(name, timeService.now().getTime());
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            Matcher matcher = pattern.matcher(parameterName);
            if (matcher.find()) {
                try {
                    int scenarioId = Integer.parseInt(matcher.group(1));
                    BasicScenario scenario = releaseEngine.getScenario(scenarioId);
                    suite.addTestResult(scenarioId,
                            parseResult(request.getParameterValues(parameterName)));
                } catch (IllegalArgumentException e) {
                    // Scenario not found so skip it
                    response.getWriter().println(e.getMessage());
                }
            }
        }
        try {
            Collection<PlayerRecord> records = scoreService.suiteResult(suite);
            for (PlayerRecord playerRecord : records) {
                response.getWriter().println(playerRecord);
            }
        } catch (IllegalArgumentException e) {
            // User not found
            response.getWriter().println(e.getMessage());
        }
        response.flushBuffer();
    }

    private TestStatus parseResult(String ... values) {
        boolean failed = false;
        for (String value : values) {
            if (value.equalsIgnoreCase("exception")) {
                return TestStatus.EXCEPTION;
            }
            failed |= value.equalsIgnoreCase("failed") || value.equalsIgnoreCase("false") || value.equalsIgnoreCase("FAIL");

        }
        return failed ? TestStatus.FAILED : TestStatus.PASSED;
    }

    public void setScoreService(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

}
