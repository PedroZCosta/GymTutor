package com.gymtutor.gymtutor.user;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PersonalRepository personalRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    //Construtor
    public UserService(UserRepository userRepository, PersonalRepository personalRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.personalRepository = personalRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(User user, boolean isPersonal, String CREEF) {
        // Criptografando a senha
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));

        // Associando a role 'STUDENT' por padrão
        Role studentRole = roleRepository.findByRoleName(RoleName.STUDENT);
        user.getRoles().add(studentRole);

        // Se o usuário for um 'PERSONAL', associa a role 'PERSONAL' e o CREEF
        if (isPersonal) {
            Role personalRole = roleRepository.findByRoleName(RoleName.PERSONAL);
            user.getRoles().add(personalRole);

            // Criando e associando o Personal
            Personal personal = new Personal();
            personal.setUser(user);
            personal.setPersonalCREEF(CREEF);
            personalRepository.save(personal);
        }

        // Salvando o usuário
        userRepository.save(user);
    }

    public void createAdminUser() {
        if (userRepository.findByUserEmail("admin@academia.com").isEmpty()) {
            User admin = new User();
            admin.setUserName("Administrador");
            admin.setUserEmail("admin@academia.com");
            admin.setUserPassword(passwordEncoder.encode("admin123"));

            // Adicionando a role 'ADMIN' para o administrador
            Role adminRole = roleRepository.findByRoleName(RoleName.ADMIN);
            admin.getRoles().add(adminRole);

            userRepository.save(admin);
        }
    }
}
