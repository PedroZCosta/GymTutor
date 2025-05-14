package com.gymtutor.gymtutor.security;

import com.gymtutor.gymtutor.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    public SecurityConfig(UserRepository userRepository, CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.userRepository = userRepository;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    // Define a configuração de segurança da aplicação
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/registration", "/password-recovery", "/login").permitAll() // Páginas públicas
                        .requestMatchers("/images/activities/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Permitir acesso a arquivos estáticos
                        .requestMatchers("/profile/**").hasAnyRole("STUDENT", "PERSONAL", "ADMIN")
                        .requestMatchers("/student/**").hasAnyRole("STUDENT", "PERSONAL", "ADMIN") // STUDENT e PERSONAL podem acessar /student
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Apenas ADMIN pode acessar /admin
                        .requestMatchers("/personal/**").hasAnyRole("PERSONAL", "ADMIN") // Apenas PERSONAL e ADMIN pode acessar /personal
                        .anyRequest().authenticated() // Qualquer outra requisição requer autenticação
                )
                .formLogin(form -> form
                        .loginPage("/login") // Configura a URL da página de login
                        .permitAll() // Permite que qualquer usuário acesse a página de login
                        .defaultSuccessUrl("/home", true) // Redireciona para /home após o login bem-sucedido
                        .failureUrl("/login?error=true") // Redireciona em caso de falha no login
                        .failureHandler(customAuthenticationFailureHandler) // Usando o handler customizado
                )
                .logout(LogoutConfigurer::permitAll // Permite que qualquer usuário faça logout
                );

        return http.build();
    }

    // Bean para codificar senhas com BCrypt
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para fornecer o serviço de detalhes do usuário, será carregado na session
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository); // Usando o repositório para carregar o usuário
    }

    // Bean para configurar o provedor de autenticação
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); // Usando o UserDetailsService customizado
        authProvider.setPasswordEncoder(passwordEncoder()); // Usando o BCryptPasswordEncoder para codificar senhas
        return authProvider;
    }
}