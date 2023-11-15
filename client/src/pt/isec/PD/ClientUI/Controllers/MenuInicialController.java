package pt.isec.PD.ClientUI.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuInicialController {
    public Button btnAutenticar;
    public Button btnRegistar;
    public Button btnCreditos;

    @FXML
    public void handleToAutenticar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/Autenticar.fxml"));
        Parent root = loader.load();

        // Crie a cena usando a raiz carregada do FXML
        Scene scene = new Scene(root);

        // Obtenha o palco da aplicação do botão ou de outra maneira adequada
        Stage stage = (Stage) btnAutenticar.getScene().getWindow();

        // Defina a nova cena no palco
        stage.setScene(scene);

        // Exiba o palco
        stage.show();
    }
    @FXML
    public void handleToRegistar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/Registar.fxml"));
        Parent root = loader.load();

        // Crie a cena usando a raiz carregada do FXML
        Scene scene = new Scene(root);

        // Obtenha o palco da aplicação do botão ou de outra maneira adequada
        Stage stage = (Stage) btnAutenticar.getScene().getWindow();

        // Defina a nova cena no palco
        stage.setScene(scene);

        // Exiba o palco
        stage.show();
    }
    @FXML
    public void handleToCreditos() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/Creditos.fxml"));
        Parent root = loader.load();

        // Crie a cena usando a raiz carregada do FXML
        Scene scene = new Scene(root);

        // Obtenha o palco da aplicação do botão ou de outra maneira adequada
        Stage stage = (Stage) btnAutenticar.getScene().getWindow();

        // Defina a nova cena no palco
        stage.setScene(scene);

        // Exiba o palco
        stage.show();
    }
}
