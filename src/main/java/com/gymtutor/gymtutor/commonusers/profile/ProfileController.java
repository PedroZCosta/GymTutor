package com.gymtutor.gymtutor.commonusers.profile;

import com.gymtutor.gymtutor.security.CustomUserDetails;
import com.gymtutor.gymtutor.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping
    public String showProfile(
            Model model,
            RedirectAttributes redirectAttributesm,
            @AuthenticationPrincipal CustomUserDetails loggedUser
    ) {
        User user = loggedUser.getUser();

        model.addAttribute("user", user);
        model.addAttribute("body", "profile/profile");
        return "/fragments/layout";
    }



}
