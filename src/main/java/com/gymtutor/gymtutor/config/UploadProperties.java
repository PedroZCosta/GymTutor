package com.gymtutor.gymtutor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UploadProperties {

    @Value("${upload.dir.activities}")
    private String uploadDirActivities;

    public String getUploadDirActivities() {
        return uploadDirActivities;
    }
}