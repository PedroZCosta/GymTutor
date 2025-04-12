package com.gymtutor.gymtutor.commonusers.workoutplanperuser;

import org.springframework.ui.Model;
import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanService;
import com.gymtutor.gymtutor.security.CustomUserDetails;
import com.gymtutor.gymtutor.user.UserRepository;
import com.gymtutor.gymtutor.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            @AuthenticationPrincipal CustomUserDetails loggedUser
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

        try {
            // Vincular novos usuários
            for (Integer userId : userIds) {
                if (!linkedUserIds.contains(userId)) {
                    var user = userRepository.findById(userId).orElse(null);
                    if (user != null) {
                        workoutPlanPerUserService.linkWorkoutPlanToUser(workoutPlan, user);
                    }
                }
            }

            // Desvincular usuários que foram desmarcados
            for (Integer linkedUserId : linkedUserIds) {
                if (!userIds.contains(linkedUserId)) {
                    var user = userRepository.findById(linkedUserId).orElse(null);
                    if (user != null) {
                        workoutPlanPerUserService.unlinkWorkoutPlanPerUser(workoutPlan, user);
                    }
                }
            }

            redirectAttributes.addFlashAttribute("successMessage", "Vínculos atualizados com sucesso!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocorreu um erro ao atualizar os vínculos.");
        }

        return "redirect:/student/workoutplan/{workoutPlanId}/linkusers";
    }

}
