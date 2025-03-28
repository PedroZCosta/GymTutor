package com.gymtutor.gymtutor.activities;

import jakarta.persistence.*;

// Entidade JPA que mapeia os dados da clínica no banco de dados. A tabela gerada será 'tb_activities'.
@Entity
@Table(
        name = "tb_activities"
)
public class ActivitiesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int activitiesId;

    private String activityName;

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
