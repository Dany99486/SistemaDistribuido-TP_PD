package pt.isec.PD.ClientUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        if (params.getRaw().size() != 2) {
            System.out.println("Sintaxe: java Cliente <serverAddress> <serverUdpPort>");
            System.exit(1);
        }
        controller.arrancaCliente(params.getRaw().toArray(new String[2]));

        FXMLLoader fxmlLoader = new FXMLLoader(MainJFX.class.getResource("/pt/isec/PD/ClientUI/Views/MenuInicialPD.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 400 , 400);
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        stage.setResizable(false);
        stage.show();
    }
}