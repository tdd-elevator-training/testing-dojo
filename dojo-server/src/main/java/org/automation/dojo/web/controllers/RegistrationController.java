package org.automation.dojo.web.controllers;

import org.automation.dojo.LogService;
import org.automation.dojo.Player;
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
@RequestMapping("/register")
public class RegistrationController {
    
    @Autowired
    private LogService logService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String openRegistrationForm(Model model) {
        model.addAttribute("player", new Player());
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submitRegistrationForm(Player player, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        if (logService.getRegisteredPlayers().contains(player.getName())) {
            result.rejectValue("name", "playerExist", "Player with this name already registered");
            return "register";
        }
        logService.registerPlayer(player.getName());
        return "redirect:/logs/" + player.getName()+"/20";
    }
}
