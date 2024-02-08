package it.unicam.cs.mpmgc.robotswarm.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class RobotSwarmApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/RobotSwarmApp.fxml")));
        primaryStage.setTitle("GOL App");
        primaryStage.setScene(new Scene(root, RobotSwarmController.WIDTH, RobotSwarmController.HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
