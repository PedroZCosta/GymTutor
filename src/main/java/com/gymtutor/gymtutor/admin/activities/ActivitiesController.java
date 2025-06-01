package com.gymtutor.gymtutor.admin.activities;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


// Controlador para gerenciar as operações relacionadas a activities.
@Controller
@RequestMapping("/admin/activities")
public class ActivitiesController {

    @Autowired
    private ActivitiesService activitiesService;

    @Autowired
    private MuscularGroupRepository muscularGroupRepository;

    @GetMapping
    public String listActivities(Model model, RedirectAttributes redirectAttributes){
        return handleRequest(redirectAttributes, model, null, null, () -> {
            var activitiesList = activitiesService.findAll();
            model.addAttribute("activities", activitiesList);
            model.addAttribute("body", "/admin/activities/list");
            return "/fragments/layout";
        });
    }

    @GetMapping("/new")
    public String newActivityForm(Model model){
        model.addAttribute("activitiesModel", new ActivitiesModel());
        model.addAttribute("body", "/admin/activities/new");
        model.addAttribute("muscularGroups", muscularGroupRepository.findAll());
        return "/fragments/layout";
    }

    @PostMapping
    public String createActivity(
            @Valid @ModelAttribute ActivitiesModel activitiesModel,
            BindingResult bindingResult ,
            Model model,
            RedirectAttributes redirectAttributes
    ){
        if(bindingResult.hasErrors()){
            return handleValidationErrors(model, "/admin/activities/new", activitiesModel, bindingResult, null);
        }
        return handleRequest(redirectAttributes, model, "/admin/activities/new", activitiesModel, () -> {
            activitiesService.createActivity(activitiesModel);
            redirectAttributes.addFlashAttribute("successMessage", "Atividade criada com sucesso!!!");
            return "redirect:/admin/activities";
        });
    }

    @GetMapping("/{activitiesId}/edit")
    public String editActivityForm(
            @PathVariable int activitiesId,
            Model model,
            RedirectAttributes redirectAttributes
    ){
        return handleRequest(redirectAttributes, model, null, null, () -> {
            ActivitiesModel activitiesModel = activitiesService.findById(activitiesId);
            model.addAttribute("activitiesModel", activitiesModel);
            model.addAttribute("body", "/admin/activities/edit");
            model.addAttribute("muscularGroups", muscularGroupRepository.findAll());
            return "/fragments/layout";
        });
    }

    @PostMapping("/{activitiesId}/edit")
    public String updateActivity(
            @PathVariable int activitiesId,
            @Valid @ModelAttribute ActivitiesModel activitiesModel,
            BindingResult bindingResult ,
            Model model,
            RedirectAttributes redirectAttributes
    ){
        if(bindingResult.hasErrors()){
            return handleValidationErrors(model, "/admin/activities/edit", activitiesModel, bindingResult, activitiesId);

        }return handleRequest(redirectAttributes, model, "/admin/activities/edit", activitiesModel, () -> {
            activitiesService.updateActivity(activitiesModel, activitiesId);
            redirectAttributes.addFlashAttribute("successMessage", "Atividade alterada com sucesso!!!");
            return "redirect:/admin/activities";
        });
    }

    @PostMapping("/delete/{activitiesId}")
    public String deleteActivities(
            @PathVariable int activitiesId,
            RedirectAttributes redirectAttributes
    ){
        try{
        activitiesService.deleteActivities(activitiesId);
        redirectAttributes.addFlashAttribute("successMessage", "Item deletado com sucesso!");
        return "redirect:/admin/activities";
        } catch (DataIntegrityViolationException ex) {
            // Se não for possível excluir devido a dependências de outros registros, exibe um erro
            redirectAttributes.addFlashAttribute("errorMessage", "Não é possível excluir o execício, pois há registros dependentes.");
            return "redirect:/admin/activities";
        } catch (Exception ex) {
            // Para outros erros, exibe a mensagem de erro
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/admin/activities";
        }
    }


    private String handleValidationErrors(Model model, String view, ActivitiesModel activitiesModel, BindingResult bindingResult, Integer activitiesId){
        model.addAttribute("errorMessage", "Há erros no formulário!");
        model.addAttribute("org.springframework.validation.BindingResult.activitiesModel", bindingResult);
        model.addAttribute("ActivitiesModel", activitiesModel);
        model.addAttribute("muscularGroups", muscularGroupRepository.findAll());
        if(activitiesId != null){
            model.addAttribute("activitiesId", activitiesId);
        }
        model.addAttribute("body", view);

        return "/fragments/layout";
    }

    private String handleRequest(RedirectAttributes redirectAttributes, Model model, String view, ActivitiesModel activitiesModel, RequestHandler block
    ){
        try{
            return block.execute();
        }catch (Exception ex) {
            return handleException(ex, model, activitiesModel, view, redirectAttributes);
        }
    }

    @FunctionalInterface
    public interface RequestHandler{
        String execute();
    }

    private String handleException(Exception ex, Model model, ActivitiesModel activitiesModel, String view, RedirectAttributes redirectAttributes){
//        System.out.println(ex.getMessage());
//        System.out.println(ex.getCause());
        return switch (ex) {
            case IllegalArgumentException illegalArgumentException ->
                    handleIllegalArgumentException(illegalArgumentException, model, activitiesModel, view);
            case IllegalAccessException illegalAccessException ->
                    handleIllegalAccessException(illegalAccessException, redirectAttributes);
            case DataIntegrityViolationException ignored ->
                    handleDataIntegrityViolationException(model, activitiesModel, view);
            case null, default -> handleUnexpectedException(model, activitiesModel, view);
        };
    }

    private String handleIllegalArgumentException(IllegalArgumentException ex, Model model, ActivitiesModel activitiesModel, String view){
        return handleError(ex.getMessage(), model, activitiesModel, view);
    }

    private String handleIllegalAccessException(IllegalAccessException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/admin/activities";
    }

    private String handleDataIntegrityViolationException(Model model, ActivitiesModel activitiesModel, String view){
        return handleError("Erro de integridade de dados.", model, activitiesModel, view);
    }

    private String handleUnexpectedException(Model model, ActivitiesModel activitiesModel, String view){
        return handleError("Erro inesperado. Tente novamente.", model, activitiesModel, view);

    }

    private String handleError(String errorMessage, Model model, ActivitiesModel activitiesModel, String view){
        if (model != null) {
//            System.err.println(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("ActivitiesModel", activitiesModel);
            model.addAttribute("muscularGroups", muscularGroupRepository.findAll());
        }
        if(view != null){
            assert model != null;
            model.addAttribute("body", view);
            return "/fragments/layout";
        }else{
            return "redirect:/admin/activities";
        }
    }

}
