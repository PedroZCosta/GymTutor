package com.gymtutor.gymtutor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.dir.activities}")
    private String uploadDirActivities;

    @Value("${upload.dir.users}")
    private String uploadDirUsers;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/activities/**")
                .addResourceLocations("file:" + new File(uploadDirActivities).getAbsolutePath() + "/");

        registry.addResourceHandler("/images/users/**")
                .addResourceLocations("file:" + new File(uploadDirUsers).getAbsolutePath() + "/");
    }

}