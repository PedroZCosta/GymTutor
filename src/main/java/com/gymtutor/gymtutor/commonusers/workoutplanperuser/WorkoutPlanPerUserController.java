package com.gymtutor.gymtutor.commonusers.workoutplanperuser;

import com.gymtutor.gymtutor.activities.ActivitiesController;
import com.gymtutor.gymtutor.activities.ActivitiesModel;
import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanModel;
import com.gymtutor.gymtutor.user.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanService;
import com.gymtutor.gymtutor.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

//todo : handle validation erros no posts


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

    @Autowired
    private WorkoutPlanPerUserRepository workoutPlanPerUserRepository;

    public void WorkoutPlanUserLinkController(
            WorkoutPlanService workoutPlanService,
            UserService userService,
            WorkoutPlanPerUserService workoutPlanPerUserService
    ) {
        this.workoutPlanService = workoutPlanService;
        this.userService = userService;
        this.workoutPlanPerUserService = workoutPlanPerUserService;
    }

    @GetMapping
    public String showUserLinkForm(
            @PathVariable int workoutPlanId,
            Model model,
            @AuthenticationPrincipal CustomUserDetails loggedUser
    ) {
        // Recupera a ficha de treino
        var workoutPlan = workoutPlanService.findById(workoutPlanId);

        // Recupera todos os usuários (ou filtre conforme necessário)
        var allUsers = userRepository.findAll();

        // Recupera os vínculos de usuários com essa ficha de treino
        var workoutPlanPerUsers = workoutPlanPerUserService.findAllByWorkoutPlanId(workoutPlanId);

        // Lista de IDs de usuários já vinculados à ficha
        var linkedUserIds = workoutPlanPerUsers.stream()
                .map(link -> link.getUser().getUserId())
                .toList();

        // Mapeia o ID do usuário para o objeto WorkoutPlanPerUserId
        var workoutPlanUserMap = workoutPlanPerUsers.stream()
                .collect(Collectors.toMap(
                        link -> link.getUser().getUserId(),
                        WorkoutPlanPerUserModel::getWorkoutPlanPerUserId
                ));

        model.addAttribute("workoutPlan", workoutPlan);
        model.addAttribute("users", allUsers);
        model.addAttribute("linkedUserIds", linkedUserIds);
        model.addAttribute("workoutPlanUserMap", workoutPlanUserMap);
        model.addAttribute("body", "student/workoutplan/linkusers");

        return "/fragments/layout";
    }

    @PostMapping
    public String linkOrUnlinkUsers(
            @PathVariable int workoutPlanId,
            @RequestParam(value = "userIds", required = false) List<Integer> userIds,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            Model model
    ) {
        // Se nada foi selecionado, tratamos como lista vazia
        if (userIds == null) {
            userIds = List.of();
        }

        var workoutPlan = workoutPlanService.findById(workoutPlanId);
        var currentLinks = workoutPlanPerUserService.findAllByWorkoutPlanId(workoutPlanId);
        var linkedUserIds = currentLinks.stream()
                .map(link -> link.getUser().getUserId())
                .toList();
        // Usando seu tratamento genérico
        List<Integer> finalUserIds = userIds;
        return handleRequest(redirectAttributes, model, "student/workoutplan/linkusers", null, () -> {
            // Vincular novos usuários
            for (Integer userId : finalUserIds) {
                if (!linkedUserIds.contains(userId)) {
                    var user = userRepository.findById(userId).orElse(null);
                    if (user != null) {
                        workoutPlanPerUserService.linkWorkoutPlanToUser(workoutPlan, user);
                    }
                }
            }

            // Desvincular usuários que foram desmarcados
            for (Integer linkedUserId : linkedUserIds) {
                if (!finalUserIds.contains(linkedUserId)) {
                    var user = userRepository.findById(linkedUserId).orElse(null);
                    if (user != null) {
                        workoutPlanPerUserService.unlinkWorkoutPlanPerUser(workoutPlan, user);
                    }
                }
            }

            redirectAttributes.addFlashAttribute("successMessage", "Vínculos atualizados com sucesso!");
            return "redirect:/student/workoutplan/" + workoutPlanId + "/linkusers";
        });
    }


//
//    public String showUserLinkForm(
//            @PathVariable Integer workoutPlanId,
//            Model model,
//            RedirectAttributes redirectAttributes,
//            @AuthenticationPrincipal CustomUserDetails userDetails
//    ) {
//        return handleRequest(redirectAttributes, model, null, null, () -> {
//            WorkoutPlanModel workoutPlan = workoutPlanService.findById(workoutPlanId);
//            List<User> allUsers     = userService.findAll();
//            List<User> linkedUsers  = workoutPlanService.findUsersLinkedToWorkoutPlan(workoutPlanId);
//
//            // **mapeia para seus DTOs**
//            List<UserDTO> userDTOs = linkedUsers.stream()
//                    .map(u -> {
//                        UserDTO dto = new UserDTO();
//                        dto.setUserId(u.getUserId());
//                        dto.setWorkoutPlanId(workoutPlanId);
//                        return dto;
//                    })
//                    .collect(Collectors.toList());
//
//            UserWrapperDTO wrapper = new UserWrapperDTO();
//            wrapper.setUsers(userDTOs);
//
//            model.addAttribute("workoutPlan", workoutPlan);
//            model.addAttribute("users", allUsers);
//            model.addAttribute("userWrapperDTO", wrapper);
//            model.addAttribute("body", "student/workoutplan/linkusers");
//            return "/fragments/layout";
//        });
//    }

    // --- NOVO: vincular um único usuário (modal) ---
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




//    // --- NOVO: desvincular um único usuário ---
//    @PostMapping("/{workoutPlanId}/linkusers/unlink")
//    public String unlinkUser(
//            @PathVariable Integer workoutPlanId,
//            @RequestParam Integer userId,
//            RedirectAttributes redirectAttributes,
//            Model model
//    ) {
//        return handleRequest(redirectAttributes, model,
//                "/student/workoutplan/" + workoutPlanId + "/linkusers",
//                null, () -> {
//                    workoutPlanService.unlinkUserFromPlan(plan, user);
//                    redirectAttributes.addFlashAttribute("successMessage", "Usuário desvinculado com sucesso!");
//                    return "redirect:/student/workoutplan/" + workoutPlanId + "/linkusers";
//
//                });
//    }

    private String handleValidationErrors(Model model, String view, WorkoutPlanPerUserModel workoutPlanPerUserModel, BindingResult bindingResult, Integer workoutPerWorkoutPlanId){
        model.addAttribute("errorMessage", "Há erros no formulário!");
        model.addAttribute("org.springframework.validation.BindingResult.WorkoutPlanPerUserModel", bindingResult);
        model.addAttribute("workoutPlanPerUserModel", workoutPlanPerUserModel);

        if(workoutPerWorkoutPlanId != null){
            model.addAttribute("workoutPerWorkoutPlanId", workoutPerWorkoutPlanId);
        }
        model.addAttribute("body", view);

        return "/fragments/layout";
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
