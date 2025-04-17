package com.gymtutor.gymtutor.commonusers.workoutactivities;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class WorkoutActivityFormDTO {

    @NotNull(message = "O ID da atividade não pode ser nulo.")
    private Integer activitiesId;

    @NotNull(message = "A sequência não pode ser nula.")
    @Min(value = 1, message = "A sequência deve ser maior ou igual a 1.")
    private Byte sequences;

    @NotBlank(message = "As repetições são obrigatórias.")
    private String reps;

    private boolean selected; // Novo campo para controlar o estado do checkbox


    // Getters e setters obrigatórios
    public Integer getActivitiesId() {
        return activitiesId;
    }

    public void setActivitiesId(Integer activitiesId) {
        this.activitiesId = activitiesId;
    }

    public Byte getSequences() {
        return sequences;
    }

    public void setSequences(Byte sequences) {
        this.sequences = sequences;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
