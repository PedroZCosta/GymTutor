package com.gymtutor.gymtutor.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        // Adiciona a mensagem de erro na sessão
        String errorMessage = switch (exception) {
            case org.springframework.security.authentication.BadCredentialsException ignored ->
                    "Nome de usuário ou senha inválidos. Verifique suas credenciais e tente novamente.";
            case org.springframework.security.authentication.LockedException ignored ->
                    "A entidade associada à sua conta está bloqueada. Entre em contato com o administrador.";
            case org.springframework.security.authentication.DisabledException ignored ->
                    "Sua conta está desativada. Entre em contato com o administrador.";
            case null, default ->
                    "Erro de autenticação desconhecido. Tente novamente mais tarde ou entre em contato com o suporte.";
        };

        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect("/login?error=true");  // Redireciona para a página de login com um parâmetro de erro
    }
}