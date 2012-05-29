package org.automation.dojo.web.controllers;

import org.automation.dojo.BoardRecord;
import org.automation.dojo.ConfigurationService;
import org.automation.dojo.LogService;
import org.automation.dojo.ReleaseEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author serhiy.zelenin
 */
@Controller
@RequestMapping("/board")
public class LeaderBoard {
    @Autowired
    private LogService logService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ReleaseEngine releaseEngine;

    public LeaderBoard(LogService logService) {
        this.logService = logService;
    }

    public LeaderBoard() {
    }

    @RequestMapping(method = RequestMethod.GET)
    public String board(ModelMap model) {
        model.addAttribute("records", logService.getBoardRecords());
        model.addAttribute("configuration", configurationService);
        model.addAttribute("majorNumber", releaseEngine.getMajorNumber() + 1); // TODO please check Admin and ReleaseLogView controllers - there are "getMajorNumber() + 1" duplicate
        model.addAttribute("minorNumber", releaseEngine.getMinorNumber());
        return "board";
    }
}
