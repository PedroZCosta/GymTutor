package com.gymtutor.gymtutor.security;

import com.gymtutor.gymtutor.user.User;
import com.gymtutor.gymtutor.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Procura o usuário pelo email
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Constrói e retorna o UserDetails
        UserBuilder userBuilder = org.springframework.security.core.userdetails.User.withUsername(user.getUserEmail());
        userBuilder.password(user.getUserPassword());

        // Adiciona o prefixo ROLE_ para cada role, se necessário
        userBuilder.roles(user.getRoles().stream()
                .map(role -> role.getRoleName().name())  // Prefixando com ROLE_
                .toArray(String[]::new));

        return userBuilder.build();
    }
}
