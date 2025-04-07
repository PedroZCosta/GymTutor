package com.gymtutor.gymtutor.activities;

import com.gymtutor.gymtutor.activitiesimages.ActivitiesImagesModel;
import com.gymtutor.gymtutor.activitiesvideos.ActivitiesVideosModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

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

    // Cria uma tabela relacionar entre o papel e o usuario
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "muscular_group_id") // Define a chave estrangeira para a tabela Role
    private MuscularGroupModel muscularGroup;


    // Relação com vídeos (um exercício para muitos vídeos)
    @OneToMany(mappedBy = "activityModel")
    private List<ActivitiesVideosModel> videos = new ArrayList<>();

    // Relação com imagens (um exercício para muitas imagens)
    @OneToMany(mappedBy = "activity")
    private List<ActivitiesImagesModel> images = new ArrayList<>();

    public ActivitiesModel() {}

    public List<ActivitiesVideosModel> getVideos() {
        return videos;
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

    public MuscularGroupModel getMuscularGroup() { return muscularGroup; }

    public void setMuscularGroup(MuscularGroupModel muscularGroup) { this.muscularGroup = muscularGroup;}




}
