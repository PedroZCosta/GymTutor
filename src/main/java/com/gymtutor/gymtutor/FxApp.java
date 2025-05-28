package com.gymtutor.gymtutor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class FxApp extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        // Inicia o contexto Spring
        context = new SpringApplicationBuilder(GymtutorApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cria o FXMLLoader
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));

        // Define o controller para ser buscado no contexto Spring
        fxmlLoader.setControllerFactory(context::getBean);

        // Carrega a interface
        Parent root = fxmlLoader.load();

        // Cria a cena e mostra
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("GymTutorFX - Login");
        primaryStage.show();
    }

    @Override
    public void stop() {
        context.close();
    }
}