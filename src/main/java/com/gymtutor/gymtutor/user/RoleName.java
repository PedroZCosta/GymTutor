package com.gymtutor.gymtutor.user;

public enum RoleName {
    STUDENT,
    ADMIN,
    PERSONAL;

    public String getDisplayName() {
        return switch (this) {
            case STUDENT -> "Aluno";
            case ADMIN -> "Administrador";
            case PERSONAL -> "Personal Trainer";
        };
    }

}