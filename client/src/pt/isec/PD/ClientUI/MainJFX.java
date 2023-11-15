package pt.isec.PD.ClientUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.PD.ClientUI.Model.Manager;

public class MainJFX extends Application {
    public static Manager controller;
    static {
        controller = new Manager();
    }

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parameters params = getParameters();
        controller.arrancaCliente(params.getRaw().toArray(new String[2]));

        FXMLLoader fxmlLoader = new FXMLLoader(MainJFX.class.getResource("/pt/isec/PD/ClientUI/Views/MenuInicialPD.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 400 , 400);
        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();
    }
}