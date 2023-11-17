package pt.isec.PD.ClientUI.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class AutenticarController extends MenuInicialController implements Initializable {
    public Label lblError;
    public Button loginButton;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    public Label lblAutenticar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton = new Button();
    }

    @FXML
    public void handleLogin() throws IOException, InterruptedException {
        if (emailField.getText().isBlank() || emailField.getText().isEmpty() || emailField == null)
            lblError.setText("Introduza um email"); //a alterar pra mudar a cor de azul para vermelho na filed
        else if (passwordField.getText().isBlank() || passwordField.getText().isEmpty() || passwordField == null)
            lblError.setText("Introduza uma password");

        if (!controller.autenticar(emailField.getText(), passwordField.getText())) {
            lblError.setText(controller.getError());
            lblError.setVisible(true);
        }else {
            lblAutenticar.setText(controller.getAutenticar());
            lblAutenticar.setVisible(true);
            Thread.sleep(1000);
            handleToClienteMenu();
        }
    }

    @FXML
    public void handleToClienteMenu() throws IOException {
        FXMLLoader loader;
        if (controller.isAdmin()) {
            loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/ClienteAdminMenu.fxml"));
        } else {
            loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/ClienteMenu.fxml"));
        }
        Parent root = loader.load();

        // Crie a cena usando a raiz carregada do FXML
        Scene scene = new Scene(root);

        // Obtenha o palco da aplicação do botão ou de outra maneira adequada
        Stage stage = (Stage) lblAutenticar.getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Defina a nova cena no palco
        stage.setScene(scene);

        // Exiba o palco
        stage.show();
    }
}
