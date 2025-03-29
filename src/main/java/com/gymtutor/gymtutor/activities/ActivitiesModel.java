package com.gymtutor.gymtutor.activities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Entidade JPA que mapeia os dados da clínica no banco de dados. A tabela gerada será 'tb_activities'.
@Entity
@Table(
        name = "tb_activities"
)
public class ActivitiesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int activitiesId;


    @NotBlank(message = "Nome não pode estar vazio!")
    @Size(min = 3, max=100 , message = "Nome deve ter entre 3 e 100 caracteres.")
    private String activityName;

    @NotBlank(message = "Descrição não pode estar vazio!")
    @Size(min = 10, max=200 , message = "Nome deve ter entre 10 e 200 caracteres.")
    private String activityDescription;

    public ActivitiesModel() {

    }

    public ActivitiesModel(int activitiesId) {
        this.activitiesId = activitiesId;
    }

    public ActivitiesModel(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public int getActivitiesId() {
        return activitiesId;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
