package com.gymtutor.gymtutor.activitiesvideos;


import com.gymtutor.gymtutor.activities.ActivitiesModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_activitiesvideo")
public class ActivitiesVideosModel {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int videoId;

    @NotBlank(message = "O nome do video nao pode esta vazio")
    @Size(min = 3, max = 100, message = "O nome do vídeo deve ter entre 3 e 100 caracteres.")
    private String videoName;

    @NotBlank(message = "O link do vídeo não pode estar vazio!")
    @Column(name = "video_link", length = 500)
    private String videoLink;

    @NotNull(message = "O vídeo deve estar associado a um exercício!")
    @ManyToOne
    @JoinColumn(name = "activity_id", referencedColumnName = "activitiesId")
    private ActivitiesModel activity;

    public ActivitiesVideosModel() {
    }


    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public ActivitiesModel getActivity() {
        return activity;
    }

    public void setActivity(ActivitiesModel activity) {
        this.activity = activity;
    }

}
