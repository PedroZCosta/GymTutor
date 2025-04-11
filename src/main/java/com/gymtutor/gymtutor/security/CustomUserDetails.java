package com.gymtutor.gymtutor.security;

import com.gymtutor.gymtutor.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public int getUserId() {
        return user.getUserId();
    }

    public String getUserName() {
        return user.getUserName();
    }

    public String getEmail() {
        return user.getUserEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Pega a lista de papéis (roles) do usuário e transforma em uma lista de "autoridades" do Spring Security
        return user.getRoles().stream()
                // Para cada role, cria uma implementação de GrantedAuthority (com lambda)
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role.getRoleName().name())
                // Coleta todos os GrantedAuthority em uma lista
                .collect(Collectors.toList());
    }


    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isLocked(); // usuário está bloqueado? então não está "non-locked"
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive(); // usa o campo isActive da entidade
    }

}