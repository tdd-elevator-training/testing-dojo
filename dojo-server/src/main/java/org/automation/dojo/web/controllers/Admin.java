package org.automation.dojo.web.controllers;

import org.automation.dojo.ConfigurationService;
import org.automation.dojo.LogService;
import org.automation.dojo.ReleaseEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author serhiy.zelenin
 */
@Controller
@RequestMapping("/admin31415")
public class Admin {
    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private LogService logService;

    @Autowired
    private ReleaseEngine releaseEngine;
    
    @RequestMapping(method = RequestMethod.GET)
    public String settingsForm(Model model) {
        model.addAttribute("configuration", configurationService);
        model.addAttribute("players", logService.getRegisteredPlayers());
        model.addAttribute("majorNumber", releaseEngine.getMajorNumber() + 1);
        model.addAttribute("minorNumber", releaseEngine.getMinorNumber());
        model.addAttribute("minorInfo", releaseEngine.getMinorInfo());
        model.addAttribute("majorInfo", releaseEngine.getMajorInfo());
        model.addAttribute("release", releaseEngine.getCurrentRelease());
        return "admin";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submitSettingsForm(ConfigurationService configuration, BindingResult result) {
        if (configuration.getMinorReleaseFrequency() < 1) {
            result.reject("minorReleaseFrequency", "Minor release frequency is too low");
            return "redirect:/admin";
        }
        if (configuration.getPenaltyTimeOut() < 0) {
            result.reject("penaltyTimeOut", "Time out can not be less than 0");
            return "redirect:/admin";
        }
        if (configuration.getPenaltyValue() < 0) {
            result.reject("penaltyTimeOut", "Penalty can not be negative");
            return "redirect:/admin";
        }
        configurationService.setMinorReleaseFrequency(configuration.getMinorReleaseFrequency());
        configurationService.setPenaltyValue(configuration.getPenaltyValue());
        configurationService.setPenaltyTimeOut(configuration.getPenaltyTimeOut());
        configurationService.setManualReleaseTriggering(configuration.isManualReleaseTriggering());
        configurationService.setLiarWeight(configuration.getLiarWeight());
        configurationService.setExceptionWeight(configuration.getExceptionWeight());
        configurationService.adjustChanges();
        return "redirect:/admin";
    }

    @RequestMapping("/nextMajor")
    public String nextMajor() {
        releaseEngine.nextMajorRelease();
        return "redirect:/admin";
    }

    @RequestMapping("/nextMinor")
    public String nextMinor() {
        releaseEngine.nextMinorRelease();
        return "redirect:/admin";
    }

    @RequestMapping("/pause")
    public String pause() {
        configurationService.setPaused(true);
        configurationService.adjustChanges();
        return "redirect:/admin";
    }

    @RequestMapping("/resume")
    public String resume() {
        configurationService.setPaused(false);
        configurationService.adjustChanges();
        return "redirect:/admin";
    }
}
