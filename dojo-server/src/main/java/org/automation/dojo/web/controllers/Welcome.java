package org.automation.dojo.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author serhiy.zelenin
 */
@Controller
@RequestMapping("/")
public class Welcome {
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET})
    public String homePage() {
        return "redirect:/search";
    }
}
