package com.gymtutor.gymtutor.activities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin/activities")
public class ActivitiesController {

    @Autowired
    private ActivitiesService activitiesService;



    @GetMapping
    public String listActivities(Model model){
        var activitiesList = activitiesService.findAll();
        model.addAttribute("activities", activitiesList);
        model.addAttribute("body", "/admin/activities/list");
        return "/fragments/layout";
    }

    @GetMapping("/new")
    public String newActivityForm(Model model){
        model.addAttribute("ActivitiesModel", new ActivitiesModel());
        model.addAttribute("body", "/admin/activities/new");
        return "/fragments/layout";
    }

    @PostMapping
    public String createActivity(
            @Valid @ModelAttribute ActivitiesModel activitiesModel,
            BindingResult bindingResult ,
            Model model,
            RedirectAttributes redirectAttributes
    ){
        activitiesService.createActivity(activitiesModel);
        redirectAttributes.addFlashAttribute("successMessage", "atividade criada com sucesso!!!");
        return "redirect:/admin/activities";

    }
    @GetMapping("/{activitiesId}/edit")
    public String editActivityForm(
            @PathVariable int activitiesId,
            Model model,
            RedirectAttributes redirectAttribute
    ){
        ActivitiesModel activitiesModel = activitiesService.findById(activitiesId);
        model.addAttribute("ActivitiesModel", activitiesModel);
        model.addAttribute("body", "/admin/activities/edit");
        return "/fragments/layout";
    }

    @PostMapping("/{activitiesId}/edit")
    public String updateActivity(
            @PathVariable int activitiesId,
            @Valid @ModelAttribute ActivitiesModel activitiesModel,
            BindingResult bindingResult ,
            Model model,
            RedirectAttributes redirectAttributes
    ){
        activitiesService.updateActivity(activitiesModel, activitiesId);
        redirectAttributes.addFlashAttribute("successMessage", "atividade Alterada com sucesso!!!");
        return "redirect:/admin/activities";

    }

    @PostMapping("/delete/{activitiesId}")
    public String deleteActivities(
            @PathVariable int activitiesId,
            RedirectAttributes redirectAttributes
    ){
        activitiesService.deleteActivities(activitiesId);
        redirectAttributes.addFlashAttribute("successMessage", "DELETADO!");
        return "redirect:/admin/activities";
    }

}
