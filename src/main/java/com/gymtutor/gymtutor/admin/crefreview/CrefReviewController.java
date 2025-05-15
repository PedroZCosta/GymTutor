package com.gymtutor.gymtutor.admin.crefreview;

import com.gymtutor.gymtutor.admin.activities.ActivitiesController;
import com.gymtutor.gymtutor.admin.activities.ActivitiesModel;
import com.gymtutor.gymtutor.user.Personal;
import com.gymtutor.gymtutor.user.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/cref_review")
public class CrefReviewController {

    @Autowired
    private PersonalService personalService;

    @GetMapping
    public String listCrefRequests(Model model, RedirectAttributes redirectAttributes){
        return handleRequest(redirectAttributes, model, null, null, () -> {
            var crefRequestList = personalService.findAllCrefReviewRequests();
            model.addAttribute("crefRequestList", crefRequestList);
            model.addAttribute("body", "/admin/crefreview/list");
            return "/fragments/layout";
        });
    }

    @PostMapping("/approve")
    public String approveCref(
            @RequestParam int personalId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Personal personal = personalService.findById(personalId);
            if (personal == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Personal não encontrado.");
                return "redirect:/admin/cref_review";
            }

            personalService.approvePersonal(personal);
            redirectAttributes.addFlashAttribute("successMessage", "CREF aprovado com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao aprovar CREF.");
        }

        return "redirect:/admin/cref_review";
    }

    @PostMapping("/reject")
    public String rejectCref(
            @RequestParam int personalId,
            @RequestParam String reason,
            RedirectAttributes redirectAttributes
    ) {
        try {
            if (reason.length() < 5 || reason.length() > 50) {
                redirectAttributes.addFlashAttribute("errorMessage", "O motivo da recusa deve ter entre 5 e 50 caracteres.");
                return "redirect:/admin/cref_review";
            }

            Personal personal = personalService.findById(personalId);
            if (personal == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Personal não encontrado.");
                return "redirect:/admin/cref_review";
            }

            personalService.rejectPersonal(personal, reason);
            redirectAttributes.addFlashAttribute("successMessage", "CREF rejeitado com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao rejeitar CREF.");
        }

        return "redirect:/admin/cref_review";
    }

    private String handleRequest(RedirectAttributes redirectAttributes, Model model, String view, ActivitiesModel activitiesModel, ActivitiesController.RequestHandler block
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
        return "redirect:/home";
    }

    private String handleDataIntegrityViolationException(Model model, ActivitiesModel activitiesModel, String view){
        return handleError("Erro de integridade de dados.", model, activitiesModel, view);
    }

    private String handleUnexpectedException(Model model, ActivitiesModel activitiesModel, String view){
        return handleError("Erro inesperado. Tente novamente.", model, activitiesModel, view);

    }

    private String handleError(String errorMessage, Model model, ActivitiesModel activitiesModel, String view){
        if (model != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        if(view != null){
            assert model != null;
            model.addAttribute("body", view);
            return "/fragments/layout";
        }else{
            return "redirect:/home";
        }
    }
}
