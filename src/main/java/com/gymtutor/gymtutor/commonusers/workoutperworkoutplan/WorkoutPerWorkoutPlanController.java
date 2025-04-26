package com.gymtutor.gymtutor.commonusers.workoutperworkoutplan;

import com.gymtutor.gymtutor.commonusers.workout.WorkoutModel;
import com.gymtutor.gymtutor.commonusers.workout.WorkoutRepository;

import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanModel;
import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanService;
import com.gymtutor.gymtutor.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/workoutplan/{workoutPlanId}/linkworkout")
public class WorkoutPerWorkoutPlanController {


    @Autowired
    private WorkoutPlanService workoutPlanService;

    @Autowired
    private WorkoutRepository workoutRepository;
    @Autowired
    private WorkoutPerWorkoutPlanService workoutPerWorkoutPlanService;

    @GetMapping
    public String showWorkoutLinkForm(
            @PathVariable int workoutPlanId,
            Model model,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            RedirectAttributes redirectAttributes
            ) {
        return handleRequest(redirectAttributes, model, null, null, workoutPlanId, () -> {
            loadView(workoutPlanId, model);
            return "/fragments/layout";
        });
    }


    @PostMapping("/link")
    public String linkWorkout(
            @PathVariable int workoutPlanId,
            @ModelAttribute @Valid WorkoutPerWorkoutPlanFormDTO workoutPerWorkoutPlanFormDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errorMessage", "Dados inválidos para vincular treino.");
            return "redirect:/student/workoutplan/" + workoutPlanId + "/linkworkout";
        }
        return handleRequest(redirectAttributes, model, "/student/workoutplan/linkworkout", null, workoutPlanId, () ->{
            WorkoutPlanModel workoutPlan = workoutPlanService.findById(workoutPlanId);
            WorkoutModel workout = workoutRepository.findById(workoutPerWorkoutPlanFormDTO.getWorkoutId()).orElseThrow();
            workoutPerWorkoutPlanService.linkWorkoutToWorkoutPlan(workout, workoutPlan);
            redirectAttributes.addFlashAttribute("successMessage", "Treino vinculado com sucesso!");
            return "redirect:/student/workoutplan/" + workoutPlanId + "/linkworkout";
        });
    }


    @PostMapping("/unlink")
    public String unlinkWorkout(
            @PathVariable int workoutPlanId,
            @ModelAttribute WorkoutPerWorkoutPlanFormDTO workoutPerWorkoutPlanFormDTO,
            RedirectAttributes redirectAttributes,
            Model model
    ){
        return handleRequest(redirectAttributes, model, "/student/workoutplan/linkworkout", null, workoutPlanId, () ->{
            WorkoutPlanModel workoutPlan = workoutPlanService.findById(workoutPlanId);
            WorkoutModel workout = workoutRepository.findById(workoutPerWorkoutPlanFormDTO.getWorkoutId()).orElseThrow();
            workoutPerWorkoutPlanService.unlinkWorkoutFromWorkoutPlan(workout, workoutPlan);
            redirectAttributes.addFlashAttribute("successMessage", "Treino desvinculado com sucesso!");
            return "redirect:/student/workoutplan/" + workoutPlanId + "/linkworkout";
        });
    }


    private String handleValidationErrors(Model model, String view, WorkoutPerWorkoutPlanFormDTO workoutPerWorkoutPlanFormDTO, BindingResult bindingResult, Integer workoutPlanId){
        model.addAttribute("errorMessage", "Há erros no formulário!");
        model.addAttribute("org.springframework.validation.BindingResult.workoutPerWorkoutPlanFormDTO", bindingResult);
        model.addAttribute("formDTO", workoutPerWorkoutPlanFormDTO);
        if (workoutPlanId != null){
            model.addAttribute("workoutPlanId", workoutPlanId);
        }
        model.addAttribute("body", view);
        return "/fragments/layout";
    }

    private String handleRequest(RedirectAttributes redirectAttributes, Model model, String view, WorkoutPerWorkoutPlanModel workoutPerWorkoutPlanModel, int workoutPlanId, WorkoutPerWorkoutPlanController.RequestHandler block){
        try{
            return block.execute();
        }catch(Exception ex){
            ex.printStackTrace();
            return handleException(ex, model, workoutPerWorkoutPlanModel, view, redirectAttributes, workoutPlanId);
        }
    }

    @FunctionalInterface
    public interface RequestHandler{
        String execute();
    }

    public String handleException(Exception ex, Model model, WorkoutPerWorkoutPlanModel workoutPerWorkoutPlanModel, String view, RedirectAttributes redirectAttributes, int workoutPlanId){
        return switch (ex){
            case IllegalArgumentException illegalArgumentException ->
                    handleIllegalArgumentException(illegalArgumentException, model, workoutPerWorkoutPlanModel, view, workoutPlanId);
            case IllegalAccessException illegalAccessException ->
                    handleIllegalAccessException(illegalAccessException, redirectAttributes);
            case DataIntegrityViolationException ignored ->
                    handleDataIntegrityViolationException(model, workoutPerWorkoutPlanModel, view, workoutPlanId);
            case null, default -> handleUnexpectedException(model, workoutPerWorkoutPlanModel, view, workoutPlanId);
        };
    }


    private String handleIllegalArgumentException(IllegalArgumentException ex, Model model, WorkoutPerWorkoutPlanModel workoutPerWorkoutPlanModel, String view, int workoutPlanId){
        return handleError(ex.getMessage(), model, workoutPerWorkoutPlanModel, view, workoutPlanId);
    }

    private String handleIllegalAccessException(IllegalAccessException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/student/workoutplan";
    }

    private String handleDataIntegrityViolationException(Model model, WorkoutPerWorkoutPlanModel workoutPerWorkoutPlanModel, String view, int workoutPlanId){
        return handleError("Erro de integridade de dados.", model, workoutPerWorkoutPlanModel, view, workoutPlanId);
    }

    private String handleUnexpectedException(Model model, WorkoutPerWorkoutPlanModel workoutPerWorkoutPlanModel, String view, int workoutPlanId){
        return handleError("Erro inesperado. Tente novamente.", model, workoutPerWorkoutPlanModel, view, workoutPlanId);

    }

    private String handleError(String errorMessage, Model model, WorkoutPerWorkoutPlanModel workoutPerWorkoutPlanModel, String view, int workoutPlanId) {
        if (model != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("workoutPerWorkoutPlanModel", workoutPerWorkoutPlanModel);
            loadView(workoutPlanId, model);
        }
        if (view != null) {
            assert model != null;
            model.addAttribute("body", view);
            return "/fragments/layout";
        } else {
            return "redirect:/student/workoutplan";
        }
    }

    private void loadView(int workoutPlanId, Model model){
        WorkoutPlanModel workoutPlan = workoutPlanService.findById(workoutPlanId);
        List<WorkoutModel> allWorkouts = workoutRepository.findAll(); // todo: filtrar por usuario logado

        List<Integer> linkedWorkoutIds = workoutPlan.getWorkoutPerWorkoutPlans().stream()
                .map(link -> link.getWorkout().getWorkoutId())
                .collect(Collectors.toList());

        List<WorkoutModel> linkedWorkouts = workoutPlan.getWorkoutPerWorkoutPlans().stream()
                .sorted(Comparator.comparingInt(link -> link.getWorkout().getWorkoutId()))
                .map(WorkoutPerWorkoutPlanModel::getWorkout)
                .collect(Collectors.toList());

        model.addAttribute("workoutPlan", workoutPlan);
        model.addAttribute("workouts", allWorkouts);
        model.addAttribute("linkedWorkoutIds", linkedWorkoutIds);
        model.addAttribute("linkedWorkouts", linkedWorkouts);
        model.addAttribute("body", "student/workoutplan/linkworkout");
    }

}
