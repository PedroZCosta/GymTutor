package com.gymtutor.gymtutor.commonusers.workoutactivities;

import com.gymtutor.gymtutor.activities.ActivitiesRepository;
import com.gymtutor.gymtutor.commonusers.workout.WorkoutService;
import com.gymtutor.gymtutor.security.CustomUserDetails;
import com.gymtutor.gymtutor.activities.ActivitiesModel;
import com.gymtutor.gymtutor.commonusers.workout.WorkoutModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
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

    private static final Logger logger = LoggerFactory.getLogger(WorkoutActivitiesController.class);

    @GetMapping
    public String showActivitiesLinkForm(
            @PathVariable int workoutId,
            Model model,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            RedirectAttributes redirectAttributes) {
        return handleRequest(redirectAttributes, model, null, null, () ->{
            logger.info("Entrou no GET de linkActivities com workoutId: {}", workoutId);
            WorkoutModel workout = workoutService.findById(workoutId);
            List<ActivitiesModel> allActivities = activitiesRepository.findAll();

            List<WorkoutActivitiesModel> currentLinks = workout.getWorkoutActivities(); // Assumindo que há um relacionamento mapeado
            List<Integer> linkedActivitiesIds = currentLinks.stream()
                    .map(link -> link.getActivity().getActivitiesId())
                    .collect(Collectors.toList());

            List<WorkoutActivityFormDTO> dtos = new ArrayList<>();
            for (ActivitiesModel activity : allActivities) {
                WorkoutActivityFormDTO dto = new WorkoutActivityFormDTO();
                dto.setActivitiesId(activity.getActivitiesId());
                dto.setSelected(linkedActivitiesIds.contains(activity.getActivitiesId())); // checkbox
                dto.setSequences((byte) 1); // valor padrão
                dto.setReps("");     // valor padrão
                dtos.add(dto);
            }

            WorkoutActivitiesFormWrapperDTO wrapper = new WorkoutActivitiesFormWrapperDTO();
            wrapper.setWorkoutActivities(dtos);

            model.addAttribute("workout", workout);
            model.addAttribute("activities", allActivities);
            model.addAttribute("linkedActivitiesIds", linkedActivitiesIds);
            model.addAttribute("workoutActivities", wrapper); // ESSENCIAL pro th:object
            model.addAttribute("body", "student/workout/linkactivities");

            return "/fragments/layout";
        });

    }

    // print dentro do post dos objetos do dto
    // utilizar apenas um array list com as dtos contendo as informacoes de vinculacao
    //

    @PostMapping
    public String linkOrUnlinkActivities(
            @PathVariable int workoutId,
            @ModelAttribute("workoutActivities") WorkoutActivitiesFormWrapperDTO formWrapperDTO, // wrapper com a lista de atividades
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            Model model
    ) {
        if (bindingResult.hasErrors()){
            return handleValidationErrors(model, "/student/workout/" + workoutId + "/linkactivities", null, bindingResult, workoutId);
        }

        return handleRequest(redirectAttributes, model, "/student/workout/" + workoutId + "/linkactivities", null, () -> {

            // DEBUG: Visualizando o que chegou do formulário
            for (WorkoutActivityFormDTO dto : formWrapperDTO.getWorkoutActivities()) {
                System.out.println("DTO - ID: " + dto.getActivitiesId() + " | selected: " + dto.isSelected());
            }

            logger.info("Entrou no POST de linkActivities com workoutId: {}", workoutId);
            logger.info("Atividades recebidas: {}", formWrapperDTO.getWorkoutActivities());
            System.out.println("Recebido: " + formWrapperDTO.getWorkoutActivities());
            List<WorkoutActivityFormDTO> dtoList = formWrapperDTO.getWorkoutActivities();
            List<Integer> submittedActivityIds = dtoList.stream()
                    .map(WorkoutActivityFormDTO::getActivitiesId)
                    .toList();

            WorkoutModel workout = workoutService.findById(workoutId);
            List<WorkoutActivitiesModel> currentLinks = workout.getWorkoutActivities();
            List<Integer> linkedActivitiesIds = currentLinks.stream()
                    .map(link -> link.getActivity().getActivitiesId())
                    .toList();

            // Pega só os IDs realmente selecionados
            List<Integer> selectedActivityIds = dtoList.stream()
                    .filter(WorkoutActivityFormDTO::isSelected)
                    .map(WorkoutActivityFormDTO::getActivitiesId)
                    .toList();

        // Vincular atividades novas
            for (WorkoutActivityFormDTO dto : dtoList) {
                if (dto.isSelected()) {
                    ActivitiesModel activity = activitiesRepository.findById(dto.getActivitiesId()).orElse(null);
                    if (activity != null) {
                        workoutActivitiesService.linkWorkoutActivityToWorkout(
                                workout,
                                activity,
                                dto.getSequences(),
                                dto.getReps()
                        );
                    }
                }
            }

            // Desvincular atividades que foram removidas (ou seja, não estão mais selecionadas)
            for (Integer linkedId : linkedActivitiesIds) {
                if (!selectedActivityIds.contains(linkedId)) {
                    ActivitiesModel activity = activitiesRepository.findById(linkedId).orElse(null);
                    if (activity != null) {
                        workoutActivitiesService.unlinkWorkoutActivityFromWorkout(workout, activity);
                    }
                }
            }

            for(WorkoutActivityFormDTO dto : dtoList){
                System.out.println("id: " +dto.getActivitiesId());
                System.out.println("selecionado: " + dto.isSelected());
                System.out.println("repeticoes: " +dto.getReps());
                System.out.println("sequencias: " +dto.getSequences());

            }
            redirectAttributes.addFlashAttribute("successMessage", "Vínculos atualizados com sucesso!");
            return "redirect:/student/workout/" + workoutId + "/linkactivities";
        });
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

    private String handleRequest(RedirectAttributes redirectAttributes, Model model, String view, WorkoutActivitiesModel workoutActivitiesModel, WorkoutActivitiesController.RequestHandler block){
        try{
            return block.execute();
        }catch(Exception ex){
            return handleException(ex, model, workoutActivitiesModel, view, redirectAttributes);
        }
    }

    @FunctionalInterface
    public interface RequestHandler{
        String execute();
    }

    public String handleException(Exception ex, Model model, WorkoutActivitiesModel workoutActivitiesModel, String view, RedirectAttributes redirectAttributes){
        return switch (ex){
            case IllegalArgumentException illegalArgumentException ->
                    handleIllegalArgumentException(illegalArgumentException, model, workoutActivitiesModel, view);
            case IllegalAccessException illegalAccessException ->
                    handleIllegalAccessException(illegalAccessException, redirectAttributes);
            case DataIntegrityViolationException ignored ->
                    handleDataIntegrityViolationException(model, workoutActivitiesModel, view);
            case null, default -> handleUnexpectedException(model, workoutActivitiesModel, view);
        };
    }

    private String handleIllegalArgumentException(IllegalArgumentException ex, Model model, WorkoutActivitiesModel workoutActivitiesModel, String view){
        return handleError(ex.getMessage(), model, workoutActivitiesModel, view);
    }

    private String handleIllegalAccessException(IllegalAccessException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/student/workout";
    }

    private String handleDataIntegrityViolationException(Model model, WorkoutActivitiesModel workoutActivitiesModel, String view){
        return handleError("Erro de integridade de dados.", model, workoutActivitiesModel, view);
    }

    private String handleUnexpectedException(Model model, WorkoutActivitiesModel workoutActivitiesModel, String view){
        return handleError("Erro inesperado. Tente novamente.", model, workoutActivitiesModel, view);

    }

    private String handleError(String errorMessage, Model model, WorkoutActivitiesModel workoutActivitiesModel, String view){
        if (model != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("workoutActivitiesModel", workoutActivitiesModel);
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

