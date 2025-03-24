package com.gymtutor.gymtutor.activities;

import jakarta.persistence.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin/activities")
public class ActivitiesController {

    @GetMapping
    public String listActivities(Model model){
        model.addAttribute("body", "/admin/activities/list");
        return "/fragments/layout";
    }



}
