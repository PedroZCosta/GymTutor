package com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student/workoutplan")
public class WorkoutExecutionRecordController {

    @Autowired
    private WorkoutExecutionRecordPerUserService workoutExecutionRecordPerUserService;

    @PostMapping("/workoutCheck")
    public String changeWorkoutExecutionRecord(
            @RequestParam Integer receiverId,
            @RequestParam Integer workoutPlanId,
            @RequestParam Integer workoutId,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        return handleRequest(redirectAttributes, model, null, () -> {
            workoutExecutionRecordPerUserService.workoutCheck(receiverId, receiverId, workoutPlanId, workoutId);
            redirectAttributes.addFlashAttribute("successMessage", "Treino registrado com sucesso!");
        });
    }

    private String handleRequest(RedirectAttributes redirectAttributes, Model model, String view, Runnable block) {
        try {
            block.run();
            return "redirect:/student/workoutplan";
        } catch (Exception ex) {
            ex.printStackTrace();
            return handleException(ex, model, view, redirectAttributes);
        }
    }

    private String handleException(Exception ex, Model model, String view, RedirectAttributes redirectAttributes) {
        String errorMessage;

        if (ex instanceof IllegalArgumentException) {
            errorMessage = ex.getMessage();
        } else if (ex instanceof IllegalAccessException) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/student/workoutplan";
        } else if (ex instanceof DataIntegrityViolationException) {
            errorMessage = "Erro de integridade de dados.";
        } else {
            errorMessage = "Erro inesperado. Tente novamente.";
        }

        if (model != null && view != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("body", view);
            return "/fragments/layout";
        }

        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        return "redirect:/student/workoutplan";
    }

}
