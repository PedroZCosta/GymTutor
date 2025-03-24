package com.gymtutor.gymtutor.config;




import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addUserInfoToModel(
            Model model,
            @AuthenticationPrincipal UserDetails loggedUser
    ){
        if(loggedUser != null){
            // Obtendo a lista de authorities do usuário
            String roles = loggedUser.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)  // Pega o nome da authority
                    .reduce((role1, role2) -> role1 + ", " + role2)  // Junta as roles em uma string separada por vírgulas
                    .orElse(""); // Caso não haja nenhuma role, retorna uma string vazia

            // Adicionando ao modelo o nome de usuário e as permissões
            model.addAttribute("LoggedUserRole", roles);
            model.addAttribute("LoggedUserName", loggedUser.getUsername()); // Exibe o nome de usuário

        }

    }

}
