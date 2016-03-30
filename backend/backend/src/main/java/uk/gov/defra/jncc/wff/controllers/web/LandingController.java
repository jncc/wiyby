/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Matt Debont
 */
@Controller
public class LandingController {

    @RequestMapping("/feedback")
    public String feedback(Model model) {
        return "feedback";
    }

    @RequestMapping("/help")
    public String help(Model model) {
        return "help";
    }

    @RequestMapping("/terms")
    public String terms(Model model) {
        return "terms";
    }

    @RequestMapping("/holding")
    public String holding(Model model) {
        return "holding";
    }
}
