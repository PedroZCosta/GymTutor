package com.gymtutor.gymtutor.user;

import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanModel;
import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


public class UserWrapperDTO {

    @Valid
    @NotNull
    private List<UserDTO> users;

    public List<UserDTO> getUsers() {
        return users;
    }
    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
    @PostMapping("/{workoutPlanId}/linkusers")
    public String processLinkUsers(
            @PathVariable Integer workoutPlanId,
            @Valid @ModelAttribute UserWrapperDTO userWrapperDTO,
            BindingResult br,
            RedirectAttributes redirectAttributes,
            WorkoutPlanService workoutPlanService

    ) {
        if (br.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Selecione ao menos um usuário.");
            return "redirect:/student/workoutplan/" + workoutPlanId + "/linkusers";
        }
        workoutPlanService.findById(workoutPlanId);
        redirectAttributes.addFlashAttribute("successMessage", "Usuários vinculados com sucesso!");
        return "redirect:/student/workoutplan/" + workoutPlanId + "/linkusers";
    }



}
