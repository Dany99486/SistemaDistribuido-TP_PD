package pt.isec.PD.ClientUI.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class ClienteAdminMenuController {
    public Button btnLogout;
    public Button btnPresencas;
    public Button btnEventos;
    public ClienteAdminMenuController() {
        this.btnLogout = new Button();
    }

    public void handlePresencas () throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/PresencasAdmin.fxml"));
        Parent root = loader.load();

        // Crie a cena usando a raiz carregada do FXML
        Scene scene = new Scene(root);

        // Obtenha o palco da aplicação do botão ou de outra maneira adequada
        Stage stage = (Stage) btnPresencas.getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Defina a nova cena no palco
        stage.setScene(scene);

        // Exiba o palco
        stage.show();
    }

    public void handleEventos () throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/EventosAdmin.fxml"));
        Parent root = loader.load();

        // Crie a cena usando a raiz carregada do FXML
        Scene scene = new Scene(root);

        // Obtenha o palco da aplicação do botão ou de outra maneira adequada
        Stage stage = (Stage) btnEventos.getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Defina a nova cena no palco
        stage.setScene(scene);

        // Exiba o palco
        stage.show();
    }

    public void logout() {
        if(controller.logout())
            System.exit(0);
    }
}
