package com.gymtutor.gymtutor.commonusers.profile;

import com.gymtutor.gymtutor.security.CustomUserDetails;
import com.gymtutor.gymtutor.user.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private PersonalService personalService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String showProfile(
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser
    ) {
        User user = loggedUser.getUser();
        model.addAttribute("user", user);

        Personal personal = personalService.findByUser(user);
        if (personal != null) {
            model.addAttribute("personal", personal);
        }


        model.addAttribute("body", "profile/profile");
        return "/fragments/layout";
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("userChangePasswordDTO", new UserChangePasswordDTO());
        model.addAttribute("body", "profile/change-password");
        return "/fragments/layout";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @Valid @ModelAttribute UserChangePasswordDTO userChangePasswordDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser
    ) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Há erros no formulário!");
            model.addAttribute("org.springframework.validation.BindingResult.userChangePasswordDTO", bindingResult);
            model.addAttribute("userChangePasswordDTO", userChangePasswordDTO);
            model.addAttribute("body", "profile/change-password");
            return "/fragments/layout";
        }
        //TODO: Adicionar a validação de erros ao salvar a senha
        User user = loggedUser.getUser();
        userService.changePassword(user, userChangePasswordDTO.getUserPassword());
        redirectAttributes.addFlashAttribute("successMessage", "Sua senha foi alterada com sucesso!");
        return "redirect:/profile";
    }

    @Transactional
    @PostMapping("/save-creef")
    public String saveCreef(
            @RequestParam("personalCREEF") String creef,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            RedirectAttributes redirectAttributes
    ) {
        User user = loggedUser.getUser();

        // Cria um novo Personal e vincula ao usuário
        Personal personal = new Personal();
        personal.setPersonalCREEF(creef);
        personal.setUser(user);
        personalService.save(personal);

        // Troca o papel para PERSONAL
        userService.changeRole(user, RoleName.PERSONAL);

        redirectAttributes.addFlashAttribute("successMessage", "CREEF cadastrado com sucesso!");
        return "redirect:/profile";
    }


    @PostMapping("/disable-account")
    public String disableAccount(
            @RequestParam("confirmPassword") String password,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser
    ) {
        User user = loggedUser.getUser();

        if (userService.checkPassword(user, password)) {
            userService.disableUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Sua conta foi desativada com sucesso!");
            return "redirect:/login"; 
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Senha incorreta. Conta não desativada.");
            return "redirect:/profile";
        }

    }


}
