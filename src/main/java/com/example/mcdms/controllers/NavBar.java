package com.example.mcdms.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class NavBar {

    @GetMapping("etapes")
    public ModelAndView howToUse() {
        //model.addAttribute("name", name);
        ModelAndView mav = new ModelAndView("template");
        return mav;

    }
}
