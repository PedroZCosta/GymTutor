package com.gymtutor.gymtutor.commonusers.workoutactivities;

import com.gymtutor.gymtutor.activities.ActivitiesRepository;
import com.gymtutor.gymtutor.commonusers.workout.WorkoutService;
import com.gymtutor.gymtutor.security.CustomUserDetails;
import com.gymtutor.gymtutor.activities.ActivitiesModel;
import com.gymtutor.gymtutor.commonusers.workout.WorkoutModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/student/workout/{workoutId}/linkactivities")
public class WorkoutActivitiesController {

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private ActivitiesRepository activitiesRepository;

    @Autowired
    private WorkoutActivitiesService workoutActivitiesService;

    @GetMapping
    public String showActivitiesLinkForm(
            @PathVariable int workoutId,
            Model model,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            RedirectAttributes redirectAttributes) {
        return handleRequest(redirectAttributes, model, null, null, workoutId, () -> {
            loadView(workoutId, model);
            return "/fragments/layout";
        });
    }

    @PostMapping("/link")
    public String linkActivity(
            @PathVariable int workoutId,
            @ModelAttribute @Valid WorkoutActivityFormDTO formDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Dados inválidos para vincular atividade.");
            return "redirect:/student/workout/" + workoutId + "/linkactivities";
        }

        return handleRequest(redirectAttributes, model, "/student/workout/linkactivities", null, workoutId, () -> {
            WorkoutModel workout = workoutService.findById(workoutId);
            ActivitiesModel activity = activitiesRepository.findById(formDTO.getActivitiesId()).orElseThrow();
            workoutActivitiesService.linkWorkoutActivityToWorkout(workout, activity, formDTO.getReps());
            redirectAttributes.addFlashAttribute("successMessage", "Atividade vinculada com sucesso!");
            return "redirect:/student/workout/" + workoutId + "/linkactivities";
        });
    }

    @PostMapping("/unlink")
    public String unlinkActivity(
            @PathVariable int workoutId,
            @ModelAttribute WorkoutActivityFormDTO formDTO,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        return handleRequest(redirectAttributes, model, "/student/workout/linkactivities", null, workoutId, () -> {
            WorkoutModel workout = workoutService.findById(workoutId);
            ActivitiesModel activity = activitiesRepository.findById(formDTO.getActivitiesId()).orElseThrow();
            workoutActivitiesService.unlinkWorkoutActivityFromWorkout(workout, activity);

            redirectAttributes.addFlashAttribute("successMessage", "Atividade desvinculada com sucesso!");
            return "redirect:/student/workout/" + workoutId + "/linkactivities";
        });
    }

    @PostMapping("/reorder")
    public ResponseEntity<Void> reorderActivities(
            @PathVariable int workoutId,
            @RequestBody List<ReorderRequest> reorderList) {

        System.out.println("Recebido reorder para workout " + workoutId);
        for (ReorderRequest req : reorderList) {
            System.out.println("Activity: " + req.getActivitiesId() + " -> Sequence: " + req.getSequence());
            workoutActivitiesService.updateSequence(workoutId, req.getActivitiesId(), req.getSequence());
        }

        return ResponseEntity.ok().build();
    }

    private String handleValidationErrors(Model model, String view, WorkoutActivityFormDTO formDTO, BindingResult bindingResult, Integer workoutId){
        model.addAttribute("errorMessage", "Há erros no formulário!");
        model.addAttribute("org.springframework.validation.BindingResult.formDTO", bindingResult);
        model.addAttribute("formDTO", formDTO);
        if (workoutId != null){
            model.addAttribute("workoutId", workoutId);
        }
        model.addAttribute("body", view);
        return "/fragments/layout";
    }

    private String handleRequest(RedirectAttributes redirectAttributes, Model model, String view, WorkoutActivitiesModel workoutActivitiesModel, int workoutId, WorkoutActivitiesController.RequestHandler block){
        try{
            return block.execute();
        }catch(Exception ex){
            return handleException(ex, model, workoutActivitiesModel, view, redirectAttributes, workoutId);
        }
    }

    @FunctionalInterface
    public interface RequestHandler{
        String execute();
    }

    public String handleException(Exception ex, Model model, WorkoutActivitiesModel workoutActivitiesModel, String view, RedirectAttributes redirectAttributes, int workoutId){
        return switch (ex){
            case IllegalArgumentException illegalArgumentException ->
                    handleIllegalArgumentException(illegalArgumentException, model, workoutActivitiesModel, view, workoutId);
            case IllegalAccessException illegalAccessException ->
                    handleIllegalAccessException(illegalAccessException, redirectAttributes);
            case DataIntegrityViolationException ignored ->
                    handleDataIntegrityViolationException(model, workoutActivitiesModel, view, workoutId);
            case null, default -> handleUnexpectedException(model, workoutActivitiesModel, view, workoutId);
        };
    }

    private String handleIllegalArgumentException(IllegalArgumentException ex, Model model, WorkoutActivitiesModel workoutActivitiesModel, String view, int workoutId){
        return handleError(ex.getMessage(), model, workoutActivitiesModel, view, workoutId);
    }

    private String handleIllegalAccessException(IllegalAccessException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/student/workout";
    }

    private String handleDataIntegrityViolationException(Model model, WorkoutActivitiesModel workoutActivitiesModel, String view, int workoutId){
        return handleError("Erro de integridade de dados.", model, workoutActivitiesModel, view, workoutId);
    }

    private String handleUnexpectedException(Model model, WorkoutActivitiesModel workoutActivitiesModel, String view, int workoutId){
        return handleError("Erro inesperado. Tente novamente.", model, workoutActivitiesModel, view, workoutId);

    }

    private String handleError(String errorMessage, Model model, WorkoutActivitiesModel workoutActivitiesModel, String view, int workoutId){
        if (model != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("workoutActivitiesModel", workoutActivitiesModel);
            loadView(workoutId , model);
        }
        if(view != null){
            assert model != null;
            model.addAttribute("body", view);
            return "/fragments/layout";
        }else{
            return "redirect:/student/workout";
        }
    }

    private void loadView(int workoutId, Model model){
        WorkoutModel workout = workoutService.findById(workoutId);
        List<ActivitiesModel> allActivities = activitiesRepository.findAll();

        List<Integer> linkedActivitiesIds = workout.getWorkoutActivities().stream()
                .map(link -> link.getActivity().getActivitiesId())
                .collect(Collectors.toList());

        List<WorkoutActivitiesModel> linkedActivities = workout.getWorkoutActivities()
                .stream()
                .sorted(Comparator.comparingInt(link -> link.getSequence()))
                .collect(Collectors.toList());


        model.addAttribute("workout", workout);
        model.addAttribute("activities", allActivities);
        model.addAttribute("linkedActivitiesIds", linkedActivitiesIds);
        model.addAttribute("linkedActivities", linkedActivities);
        model.addAttribute("body", "/student/workout/linkactivities");
    }


}

