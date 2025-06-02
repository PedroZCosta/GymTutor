package com.gymtutor.gymtutor.personal.clientgraphs;

import com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser.WorkoutExecutionRecordPerUserService;
import com.gymtutor.gymtutor.personal.clientperuser.ClientPerUserController;
import com.gymtutor.gymtutor.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/personal/clientgraphs")
public class ClientGraphsController {

    @Autowired
    private ClientGraphsService clientGraphsService;

    @Autowired
    private WorkoutExecutionRecordPerUserService workoutExecutionRecordPerUserService;

    @GetMapping
    public String listGraphs(
            Model model,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            RedirectAttributes redirectAttributes
    ){
        return handleRequest(redirectAttributes, model, "/clientgraphs/listclients", null, loggedUser, () -> {
            List<ClientGraphDTO> clients;
            clients= clientGraphsService.findAllClientsGraphs(loggedUser.getUser().getUserId());
            model.addAttribute("clients", clients);
            model.addAttribute("body", "/personal/clientgraphs/list");
            return "/fragments/layout";
        });

    }

    @PostMapping("/restart-client-workout")
    public String restartClientWorkout(
            Model model,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            @RequestParam("workoutPlanId") int workoutPlanId,  // recebe o userId do cliente no form
            RedirectAttributes redirectAttributes
    ){
        return handleRequest(redirectAttributes, model, "/personal/clientgraphs", null, loggedUser, () -> {

            workoutExecutionRecordPerUserService.resetAllExecutions(workoutPlanId);
            redirectAttributes.addFlashAttribute("successMessage", "Ficha de treino reiniciada com sucesso!");
            return "redirect:/personal/clientgraphs";
        });
    }

    private String handleRequest(RedirectAttributes redirectAttributes, Model model, String view, Object ClientGraphsDTO, Object loggedUser, RequestHandler block) {
        try {
            return block.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
            return handleException(ex, model, view, redirectAttributes, loggedUser);
        }
    }

    @FunctionalInterface
    private interface RequestHandler {
        String execute();
    }

    private String handleException(Exception ex, Model model, String view, RedirectAttributes redirectAttributes, Object loggedUser
    ) {
        return switch (ex) {
            case IllegalArgumentException illegalArgumentException ->
                    handleIllegalArgumentException(illegalArgumentException, model, view, loggedUser);
            case IllegalAccessException illegalAccessException ->
                    handleIllegalAccessException(illegalAccessException, redirectAttributes);
            case DataIntegrityViolationException ignored ->
                    handleDataIntegrityViolationException(model, view, loggedUser);
            case null, default -> handleUnexpectedException(model, view, loggedUser);
        };
    }

    private String handleIllegalArgumentException(IllegalArgumentException ex, Model model, String view, Object loggedUser) {
        return handleError(ex.getMessage(), model, view, loggedUser);
    }

    private String handleIllegalAccessException(IllegalAccessException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/personal/linkclients";
    }

    private String handleDataIntegrityViolationException(Model model, String view, Object loggedUser) {
        return handleError("Erro de integridade de dados.", model, view, loggedUser);
    }

    private String handleUnexpectedException(Model model, String view, Object loggedUser) {
        return handleError("Erro inesperado. Tente novamente.", model, view, loggedUser);
    }

    private String handleError(String errorMessage, Model model, String view, Object loggedUser) {
        if (model != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("body", view);
            return "/fragments/layout";
        } else {
            return "redirect:/personal/linkclients";
        }
    }
}
