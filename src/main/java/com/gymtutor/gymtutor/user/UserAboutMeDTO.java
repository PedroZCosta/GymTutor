package com.gymtutor.gymtutor.user;

import jakarta.validation.constraints.Size;

public class UserAboutMeDTO {

    @Size(min = 10, max=1200 , message = "Este campo deve ter entre 10 e 1200 caracteres.")
    private String aboutMe;

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
}
