package com.gymtutor.gymtutor.config;

import com.gymtutor.gymtutor.activities.MuscularGroup;
import com.gymtutor.gymtutor.activities.MuscularGroupModel;
import com.gymtutor.gymtutor.activities.MuscularGroupRepository;
import com.gymtutor.gymtutor.user.Role;
import com.gymtutor.gymtutor.user.RoleName;
import com.gymtutor.gymtutor.user.RoleRepository;
import com.gymtutor.gymtutor.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserService userService;
    private final MuscularGroupRepository muscularGroupRepository;

    // Injeção de dependências
    public DataLoader(RoleRepository roleRepository, UserService userService, MuscularGroupRepository muscularGroupRepository) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.muscularGroupRepository = muscularGroupRepository;
    }

    // Metodo executado ao iniciar a aplicação
    @Override
    public void run(String... args) throws Exception {
        // Verifica se as roles já existem no banco de dados
        // Se não existirem, cria as roles padrão ADMIN, STUDENT e PERSONAL
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(RoleName.ADMIN));    // Cria a role ADMIN
            roleRepository.save(new Role(RoleName.STUDENT));  // Cria a role STUDENT
            roleRepository.save(new Role(RoleName.PERSONAL)); // Cria a role PERSONAL
        }

        if (muscularGroupRepository.count() == 0) {
            muscularGroupRepository.save(new MuscularGroupModel(MuscularGroup.PEITORAL));
            muscularGroupRepository.save(new MuscularGroupModel(MuscularGroup.COSTAS));
            muscularGroupRepository.save(new MuscularGroupModel(MuscularGroup.TRICEPS));
            muscularGroupRepository.save(new MuscularGroupModel(MuscularGroup.BICEPS));
            muscularGroupRepository.save(new MuscularGroupModel(MuscularGroup.OMBROS));
            muscularGroupRepository.save(new MuscularGroupModel(MuscularGroup.PERNA));
            muscularGroupRepository.save(new MuscularGroupModel(MuscularGroup.ABDOMEN));
        }

        // Chama o Metodo criar administrador do UserService para criar o usuário administrador
        userService.createAdminUser();
    }
}