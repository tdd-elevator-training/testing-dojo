package org.automation.dojo.web.controllers;

import org.automation.dojo.DojoScoreService;
import org.automation.dojo.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
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
    private Pattern pattern = Pattern.compile("scenario(\\d*)", Pattern.CASE_INSENSITIVE);

    public PlayerResultController(ScoreService service) {
        this.scoreService = service;
    }

    public PlayerResultController() {
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET})
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String remoteAddr = request.getRemoteAddr();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            Matcher matcher = pattern.matcher(parameterName);
            if (matcher.find()) {
                boolean result = scoreService.testResult(name, remoteAddr, Integer.parseInt(matcher.group(1)),
                        parseResult(request.getParameter(parameterName)));
                response.getWriter().println(parameterName + "=" + (result ? "passed" : "failed"));
            }
        }
        response.flushBuffer();
    }

    private boolean parseResult(String value) {
        return value.equalsIgnoreCase("passed") || value.equalsIgnoreCase("true");
    }

    public void setScoreService(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

}
