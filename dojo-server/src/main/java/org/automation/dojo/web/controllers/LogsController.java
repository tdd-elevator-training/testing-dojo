package org.automation.dojo.web.controllers;

import org.apache.commons.io.IOUtils;
import org.automation.dojo.*;
import org.automation.dojo.web.scenario.BasicScenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author serhiy.zelenin
 */
@Controller
public class LogsController {
    @Autowired
    private LogService logService;

    @Autowired
    private ReleaseEngine releaseEngine;
    @Autowired
    private ConfigurationService configurationService;
    
    @RequestMapping(value = "/logs/{playerName}/{lastN}", method = RequestMethod.GET)
    public String playerRecords(ModelMap model, @PathVariable String playerName, @PathVariable int lastN) {
        return addPageParams(model, playerName, lastN);
    }

    @RequestMapping(value = "/logs/{playerName}", method = RequestMethod.GET)
    public String playerRecords(ModelMap model, @PathVariable String playerName) {
        return addPageParams(model, playerName, -1);
    }

    private String addPageParams(ModelMap model, String playerName, int lastN) {
        List<ReleaseLogView> releaseLogs = logService.getLastReleaseLogsForPlayer(playerName, lastN);
        model.addAttribute("releaseLogs", releaseLogs);
        model.addAttribute("playerName", playerName);
        model.addAttribute("configuration", configurationService);
        model.addAttribute("majorNumber", releaseEngine.getMajorNumber() + 1);
        model.addAttribute("minorNumber", releaseEngine.getMinorNumber());
        return "logs";
    }

    @RequestMapping(value = "/scenarios", method = RequestMethod.GET)
    public String scenarios(ModelMap model) {
        ArrayList<String> scenariosHtml = new ArrayList<String>();
        List<BasicScenario> currentScenarios = releaseEngine.getCurrentScenarios();
        for (BasicScenario currentScenario : currentScenarios) {
            scenariosHtml.add(getDescription(currentScenario.getId()));
        }
        model.addAttribute("scenarios", scenariosHtml);
        return "scenarios";
    }

    private String getDescription(int scenarioId) {
        String scenarioHtml;
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = getClass().getResourceAsStream(
                    "/org/automation/dojo/scenario" + scenarioId + ".html");
            if (resourceAsStream == null) {
                scenarioHtml = "[No description yet. Ask trainer.]";
            } else {
                scenarioHtml = IOUtils.toString(resourceAsStream);
            }
        } catch (IOException e) {
            scenarioHtml = "[TBD]";
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(resourceAsStream);
        }
        return scenarioHtml;
    }

    @RequestMapping(value = "/scenario/{id}", method = RequestMethod.GET)
    public String scenarios(ModelMap model, @PathVariable int id) {
        releaseEngine.getScenario(id);//to make sure nobody sees non-implemented scenarios ;)
        model.addAttribute("scenarios",
                Collections.singletonList(getDescription(id)));
        return "scenarios";
    }
    
    
    
}
