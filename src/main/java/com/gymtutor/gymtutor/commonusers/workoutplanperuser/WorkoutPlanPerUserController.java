package com.gymtutor.gymtutor.commonusers.workoutplanperuser;

import com.gymtutor.gymtutor.activities.ActivitiesController;
import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanModel;
import com.gymtutor.gymtutor.user.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanService;
import com.gymtutor.gymtutor.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Controlador para gerenciar as operações relacionadas a WorkoutPlanPerUser.
@Controller
@RequestMapping("/student/workoutplan/{workoutPlanId}/linkusers")
public class WorkoutPlanPerUserController {

    @Autowired
    private WorkoutPlanService workoutPlanService;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkoutPlanPerUserService workoutPlanPerUserService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showUserLinkForm(
            @PathVariable int workoutPlanId,
            Model model,
            @AuthenticationPrincipal CustomUserDetails loggedUser
    ) {
        // Recupera a ficha de treino
        WorkoutPlanModel workoutPlan = workoutPlanService.findById(workoutPlanId);

        // Recupera todos os usuários (ou filtre conforme necessário)
        List<User> allUsers = userRepository.findAll();

        // Recupera os vínculos de usuários com essa ficha de treino
        var workoutPlanPerUsers = workoutPlanPerUserService.findAllByWorkoutPlanId(workoutPlanId);

        // IDs dos usuários vinculados
        List<Integer> linkedUserIds = workoutPlanPerUsers.stream()
                .map(link -> link.getUser().getUserId())
                .collect(Collectors.toList());

        // Objetos User completos, ordenados pelo ID
        List<User> linkedUsers = workoutPlanPerUsers.stream()
                .sorted(Comparator.comparingInt(link -> link.getUser().getUserId()))
                .map(WorkoutPlanPerUserModel::getUser)
                .collect(Collectors.toList());


        model.addAttribute("workoutPlan", workoutPlan);
        model.addAttribute("users", allUsers);
        model.addAttribute("linkedUserIds", linkedUserIds);
        model.addAttribute("linkedUsers", linkedUsers);
        model.addAttribute("body", "student/workoutplan/linkusers");

        return "/fragments/layout";
    }

    @PostMapping("/link")
    public String linkUser(
            @PathVariable Integer workoutPlanId,
            @RequestParam Integer userId,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        return handleRequest(redirectAttributes, model,
                "/student/workoutplan/linkusers",
                null, () ->{
                    workoutPlanPerUserService.linkUserToPlan(workoutPlanId, userId);
                    redirectAttributes.addFlashAttribute("successMessage", "Usuário vinculado com sucesso!");
                    return "redirect:/student/workoutplan/" + workoutPlanId + "/linkusers";
                });
    }



    @PostMapping("/unlink")
    public String unlinkUser(
            @PathVariable Integer workoutPlanId,
            @ModelAttribute WorkoutPlanPerUserFormDTO workoutPlanPerUserFormDTO,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        return handleRequest(redirectAttributes, model,
                "/student/workoutplan/" + workoutPlanId + "/linkusers",
                null, () -> {
                    workoutPlanPerUserService.unlinkWorkoutPlanPerUser(workoutPlanId, workoutPlanPerUserFormDTO.getUserId());
                    redirectAttributes.addFlashAttribute("successMessage", "Usuário desvinculado com sucesso!");
                    return "redirect:/student/workoutplan/" + workoutPlanId + "/linkusers";

                });
    }

    private String handleRequest(RedirectAttributes redirectAttributes, Model model, String view, WorkoutPlanPerUserModel workoutPlanPerUserModel, ActivitiesController.RequestHandler block
    ){
        try{
            return block.execute();
        }catch (Exception ex) {
            return handleException(ex, model, workoutPlanPerUserModel, view, redirectAttributes);
        }
    }
    private String handleException(Exception ex, Model model, WorkoutPlanPerUserModel workoutPlanPerUserModel, String view, RedirectAttributes redirectAttributes){
        return switch (ex) {
            case IllegalArgumentException illegalArgumentException ->
                    handleIllegalArgumentException(illegalArgumentException, model, workoutPlanPerUserModel, view);
            case IllegalAccessException illegalAccessException ->
                    handleIllegalAccessException(illegalAccessException, redirectAttributes);
            case DataIntegrityViolationException ignored ->
                    handleDataIntegrityViolationException(model, workoutPlanPerUserModel, view);
            case null, default -> handleUnexpectedException(model, workoutPlanPerUserModel, view);
        };
    }
    private String handleIllegalArgumentException(IllegalArgumentException ex, Model model, WorkoutPlanPerUserModel workoutPlanPerUserModel , String view){
        return handleError(ex.getMessage(), model, workoutPlanPerUserModel, view);
    }
    private String handleIllegalAccessException(IllegalAccessException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/student/workoutplan/linkusers";
    }
    private String handleDataIntegrityViolationException(Model model, WorkoutPlanPerUserModel workoutPlanPerUserModel, String view){
        return handleError("Erro de integridade de dados.", model, workoutPlanPerUserModel, view);
    }

    private String handleUnexpectedException(Model model, WorkoutPlanPerUserModel workoutPlanPerUserModel, String view){
        return handleError("Erro inesperado. Tente novamente.", model, workoutPlanPerUserModel, view);

    }

    private String handleError(String errorMessage, Model model, WorkoutPlanPerUserModel workoutPlanPerUserModel, String view){
        if (model != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("workoutPlanPerUserModel", workoutPlanPerUserModel);
        }
        if(view != null){
            assert model != null;
            model.addAttribute("body", view);
            return "/fragments/layout";
        }else{
            return "redirect:/student/workoutplan/linkusers";
        }
    }
}
