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
        request.getSession().setAttribute("errorMessage", "Credenciais inválidas. Tente novamente.");
        response.sendRedirect("/login?error=true");  // Redireciona para a página de login com um parâmetro de erro
    }
}