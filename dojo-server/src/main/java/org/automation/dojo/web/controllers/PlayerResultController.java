package org.automation.dojo.web.controllers;

import org.automation.dojo.ScoreService;
import org.automation.dojo.TestStatus;
import org.automation.dojo.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/result")
public class PlayerResultController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private TimeService timeService;
    private Pattern pattern = Pattern.compile("scenario(\\d*)", Pattern.CASE_INSENSITIVE);

    public PlayerResultController(ScoreService service, TimeService timeService) {
        this.scoreService = service;
        this.timeService = timeService;
    }

    public PlayerResultController() {
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET})
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            Matcher matcher = pattern.matcher(parameterName);
            if (matcher.find()) {
                try {
                    boolean result = scoreService.testResult(name, Integer.parseInt(matcher.group(1)), parseResult(
                            request.getParameterValues(parameterName)), timeService.now().getTime());
                    response.getWriter().println(parameterName + "=" + (result ? "passed" : "failed"));
                } catch (IllegalArgumentException e) {
                    //Scenario not found so skip it
                }
            }
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
