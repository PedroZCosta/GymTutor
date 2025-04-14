package com.gymtutor.gymtutor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UploadProperties {

    @Value("${upload.dir.activities}")
    private String uploadDirActivities;

    @Value("${upload.dir.users}")
    private String uploadDirUsers;

    public String getUploadDirActivities() {
        return uploadDirActivities;
    }

    public String getUploadDirUsers() {
        return uploadDirUsers;
    }
}