package org.automation.dojo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name="resultServlet", urlPatterns={"/result"})
public class GetResultServlet extends HttpServlet {
    private ScenarioService scenarioService;
    private Pattern pattern = Pattern.compile("scenario(\\d*)", Pattern.CASE_INSENSITIVE);

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        String name = request.getParameter("name");
        String remoteAddr = request.getRemoteAddr();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            Matcher matcher = pattern.matcher(parameterName);
            if (matcher.find()) {
                boolean result = scenarioService.testResult(name, remoteAddr, Integer.parseInt(matcher.group(1)),
                        parseResult(request.getParameter(parameterName)));
                resp.getWriter().println(parameterName + "=" + (result ? "passed" : "failed"));
            }
        }
        resp.flushBuffer();
    }

    private boolean parseResult(String value) {
        return value.equalsIgnoreCase("passed") || value.equalsIgnoreCase("true");
    }

    public void setScenarioService(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }
}
