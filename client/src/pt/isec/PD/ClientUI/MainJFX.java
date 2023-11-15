package pt.isec.PD.ClientUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class MainJFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(MainJFX.class.getResource("/pt/isec/PD/ClientUI/Views/MenuInicialPD.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 400 , 400);
        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();
    }

    }