package com.gymtutor.gymtutor.security;

import com.gymtutor.gymtutor.user.User;
import com.gymtutor.gymtutor.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {

        return "login";
    }


    //TODO: IMPLEMENTAR MÉTODOS DE CADASTRO DOS USUÁRIOS
    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("user", new User());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String criarConta(
            @ModelAttribute User user,
            @RequestParam(required = false) boolean isPersonal,
            @RequestParam(required = false) String creef
    ) {
        userService.createUser(user, isPersonal, creef);
        return "redirect:/login";
    }

}