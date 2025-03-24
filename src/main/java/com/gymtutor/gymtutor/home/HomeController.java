package com.gymtutor.gymtutor.home;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    // Rota para exibir a página Home
    @GetMapping("/home")
    public String home(
            Model model,
            @AuthenticationPrincipal UserDetails loggedUser,
            @RequestParam(required = false) String errorMessage
    ) {
        // Se houver uma mensagem de erro, adiciona ao modelo
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        // Adiciona o conteúdo da página 'home' ao modelo
        model.addAttribute("body", "home");

        // Retorna o layout com o conteúdo da página 'home'
        return "/fragments/layout";
    }

}
