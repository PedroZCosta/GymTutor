package com.gymtutor.gymtutor.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonalRepository personalRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void criarUsuario(User user, boolean isPersonal, String creef) {
        // Criptografando a senha
        user.setSenha(passwordEncoder.encode(user.getSenha()));

        // Associando a role 'STUDENT' por padrão
        Role studentRole = roleRepository.findByNome("ROLE_STUDENT");
        user.getRoles().add(studentRole);

        // Se o usuário for um 'PERSONAL', associa a role 'PERSONAL' e o CREEF
        if (isPersonal) {
            Role personalRole = roleRepository.findByNome("ROLE_PERSONAL");
            user.getRoles().add(personalRole);

            // Criando e associando o Personal
            Personal personal = new Personal();
            personal.setUser(user);
            personal.setCreef(creef);
            personalRepository.save(personal);
        }

        // Salvando o usuário
        userRepository.save(user);
    }

    public void criarAdmin() {
        if (userRepository.findByEmail("admin@academia.com").isEmpty()) {
            User admin = new User();
            admin.setNome("Administrador");
            admin.setEmail("admin@academia.com");
            admin.setSenha(passwordEncoder.encode("admin123"));

            // Adicionando a role 'ADMIN' para o administrador
            Role adminRole = roleRepository.findByNome("ROLE_ADMIN");
            admin.getRoles().add(adminRole);

            userRepository.save(admin);
        }
    }
}
