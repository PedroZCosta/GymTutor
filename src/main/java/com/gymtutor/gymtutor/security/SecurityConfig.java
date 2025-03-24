package com.gymtutor.gymtutor.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Para desabilitar o CSRF, caso não seja necessário em seu contexto
                .authorizeRequests()
                    .antMatchers("/", "/cadastro", "/login", "/h2-console/**").permitAll() // Páginas públicas
                    .antMatchers("/admin/**").hasRole("ADMIN") // Apenas ADMIN pode acessar /admin
                    .antMatchers("/personal/**").hasRole("PERSONAL") // Apenas PERSONAL pode acessar /personal
                    .anyRequest().authenticated() // Todas as outras páginas exigem autenticação
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
        // Habilitar acesso ao H2 console em desenvolvimento
        http.headers().frameOptions().sameOrigin();
        return http.build();
    }
}