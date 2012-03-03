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

@WebServlet(name="resultServlet", urlPatterns={"/result"}, asyncSupported = false)
public class GetResultServlet extends HttpServlet {
    private ScoreService scoreService = new DojoScoreService();
    private Pattern pattern = Pattern.compile("scenario(\\d*)", Pattern.CASE_INSENSITIVE);

    @Override
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req, resp);
    }

    private boolean parseResult(String value) {
        return value.equalsIgnoreCase("passed") || value.equalsIgnoreCase("true");
    }

    public void setScoreService(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

}
