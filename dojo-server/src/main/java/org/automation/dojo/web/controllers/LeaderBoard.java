package org.automation.dojo.web.controllers;

import org.automation.dojo.BoardRecord;
import org.automation.dojo.LogService;
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

    public LeaderBoard(LogService logService) {
        this.logService = logService;
    }

    public LeaderBoard() {
    }

    @RequestMapping(method = RequestMethod.GET)
    public String board(ModelMap model) {
        model.addAttribute("records", logService.getBoardRecords());
        return "board";
    }
}
