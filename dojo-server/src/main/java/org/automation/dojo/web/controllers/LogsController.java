package org.automation.dojo.web.controllers;

import org.automation.dojo.LogService;
import org.automation.dojo.PlayerRecord;
import org.automation.dojo.ReleaseEngine;
import org.automation.dojo.ReleaseLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    
    @RequestMapping(value = "/logs/{playerName}", method = RequestMethod.GET)
    public String playerRecords(ModelMap model, @PathVariable String playerName) {
        List<ReleaseLog> logsForHost = logService.getReleaseLogs();
        List<List<PlayerRecord>> releaseLogs = new ArrayList<List<PlayerRecord>>();
        for (ReleaseLog releaseLog : logsForHost) {
            releaseLogs.add(releaseLog.getRecordsForPlayer(playerName));
        }
        model.addAttribute("releaseLogs", releaseLogs);
        model.addAttribute("playerName", playerName);
        return "logs";
    }
}
