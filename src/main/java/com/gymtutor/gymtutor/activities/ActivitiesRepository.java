package com.gymtutor.gymtutor.activities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// A anotação @Repository indica que esta interface é um repositório Spring Data JPA,
// que irá gerenciar a entidade ClinicEntity, permitindo operações CRUD (criação, leitura, atualização, remoção) no banco de dados.
@Repository
public interface ActivitiesRepository extends JpaRepository<ActivitiesModel, Integer> {
}
