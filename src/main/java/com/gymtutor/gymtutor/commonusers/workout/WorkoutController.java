package com.gymtutor.gymtutor.commonusers.workout;

import com.gymtutor.gymtutor.commonusers.workoutactivities.WorkoutActivitiesModel;
import com.gymtutor.gymtutor.security.CustomUserDetails;
import com.gymtutor.gymtutor.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Comparator;


@Controller
@RequestMapping("/student/workout")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;


    @GetMapping
    public String listWorkout(
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        return handleRequest(redirectAttributes, model, null, null, () -> {
            int userId = userDetails.getUser().getUserId();
            var workoutList = workoutService.findAllByReceiverUserId(userId); // todo: tenho que realizar novo filtro pois agora aprece as atividades clonadas tambem, ou seja talvez seja legal ter um campo chamado "clonado" como boolean
            //Função para colocar as atividades vinculadas em ordem sequencial
            for (WorkoutModel workout : workoutList) {
                workout.getWorkoutActivities().sort(Comparator.comparingInt(WorkoutActivitiesModel::getSequence));
            }
            model.addAttribute("workout", workoutList);
            model.addAttribute("body", "student/workout/list");
            return "/fragments/layout";
        });
    }

    @GetMapping("/new")
    public String newWorkoutForm(Model model){
        model.addAttribute("workoutModel", new WorkoutModel());
        model.addAttribute("body", "student/workout/new");
        return "/fragments/layout";
    }

    @PostMapping
    public String createWorkout(
            @Valid @ModelAttribute WorkoutModel workoutModel,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser

    ){
        if(bindingResult.hasErrors()){
            return handleValidationErrors(model, "/student/workout/new", workoutModel, bindingResult, null);
        }
        return handleRequest(redirectAttributes, model, "student/workout/new", workoutModel, () ->{
            workoutService.createWorkout(workoutModel, loggedUser);
            redirectAttributes.addFlashAttribute("successMessage", "Treino criado com sucesso!");
            return "redirect:/student/workout";
        });}

    @GetMapping("/{workoutId}/edit")
    public String editWorkoutForm(
            @PathVariable int workoutId,
            Model model,
            RedirectAttributes redirectAttributes
    ){
        return handleRequest(redirectAttributes, model, null, null, () ->{
            WorkoutModel workoutModel = workoutService.findById(workoutId);
            model.addAttribute("workoutModel", workoutModel);
            model.addAttribute("body", "/student/workout/edit");
            return "/fragments/layout";
        });
    }

    @PostMapping("/{workoutId}/edit")
    public String updateWorkout(
            @PathVariable int workoutId,
            @Valid @ModelAttribute WorkoutModel workoutModel,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetailsService loggedUser
            ){
        if (bindingResult.hasErrors()){
            return handleValidationErrors(model, "/student/workoutId/edit", workoutModel, bindingResult, workoutId);

        }return handleRequest(redirectAttributes, model, "student/workout/edit", workoutModel, () -> {
            workoutService.updateWorkout(workoutModel, workoutId);
            redirectAttributes.addFlashAttribute("successMessage", "Treino alterado com sucesso!");
            return "redirect:/student/workout";
        });
    }

    @PostMapping("/delete/{workoutId}")
    public String deleteWorkout(
            @PathVariable int workoutId,
            RedirectAttributes redirectAttributes
    ){
        try{
            workoutService.deleteWorkout(workoutId);
            redirectAttributes.addFlashAttribute("successMessage", "Treino deletado com sucesso!");
            return "redirect:/student/workout";
        }catch (DataIntegrityViolationException ex){
            // Se não for possível excluir devido a dependências de outros registros, exibe um erro
            redirectAttributes.addFlashAttribute("errorMessage", "Não é possivel excluir o Treino, pois há registros dependentes.");
            return "redirect:/student/workout";
        }catch (Exception ex){
            // Para outros erros, exibe a mensagem de erro
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/student/workout";
        }
    }



    private String handleValidationErrors(Model model, String view, WorkoutModel workoutModel, BindingResult bindingResult, Integer workoutId) {
        model.addAttribute("errorMessage", "Há erros no formulário!");
        model.addAttribute("org.springframework.validation.BindingResult.WorkoutModel", bindingResult);
        model.addAttribute("WorkoutModel", workoutModel);
        if (workoutId != null) {
            model.addAttribute("workoutId", workoutId);
        }
        model.addAttribute("body", view);
        return "/fragments/layout";
    }

    private String handleRequest(RedirectAttributes redirectAttributes, Model model, String view, WorkoutModel workoutModel, WorkoutController.RequestHandler block){
        try{
            return block.execute();
        }catch (Exception ex){
            return handleException(ex, model, workoutModel, view,  redirectAttributes);
        }
    }

    @FunctionalInterface
    public interface RequestHandler{
        String execute();
    }

    public String handleException(Exception ex, Model model, WorkoutModel workoutModel, String view, RedirectAttributes redirectAttributes){
        if(ex instanceof IllegalArgumentException){
            return handleIllegalArgumentException((IllegalArgumentException) ex, model, workoutModel, view);
        } else if (ex instanceof IllegalAccessException) {
            return handleIllegalAccessException((IllegalAccessException) ex, redirectAttributes);
        } else if (ex instanceof DataIntegrityViolationException) {
            return handleDataIntegrityViolationException(model, workoutModel, view);
        } else {
            return handleUnexpectedException(model, workoutModel, view);
        }
    }

    private String handleIllegalArgumentException(IllegalArgumentException ex, Model model, WorkoutModel workoutModel, String view){
        return handleError(ex.getMessage(), model, workoutModel, view);
    }

    private String handleIllegalAccessException(IllegalAccessException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/student/workout";
    }

    private String handleDataIntegrityViolationException(Model model, WorkoutModel workoutModel, String view){
        return handleError("Erro de integridade de dados.", model, workoutModel, view);
    }

    private String handleUnexpectedException(Model model, WorkoutModel workoutModel, String view){
        return handleError("Erro inesperado. Tente novamente.", model, workoutModel, view);

    }

    private String handleError(String errorMessage, Model model, WorkoutModel workoutModel, String view){
        if (model != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("workoutModel", workoutModel);
        }
        if(view != null){
            assert model != null;
            model.addAttribute("body", view);
            return "/fragments/layout";
        }else{
            return "redirect:/student/workout";
        }
    }
}
