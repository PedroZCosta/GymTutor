package com.gymtutor.gymtutor.admin.activities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// A anotação @Repository indica que esta interface é um repositório Spring Data JPA,
// que irá gerenciar a entidade ClinicEntity, permitindo operações CRUD (criação, leitura, atualização, remoção) no banco de dados.
@Repository
public interface ActivitiesRepository extends JpaRepository<ActivitiesModel, Integer> {

    List<ActivitiesModel> findByActivityName(String activityName);

}
