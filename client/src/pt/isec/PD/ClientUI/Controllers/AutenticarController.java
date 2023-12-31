package pt.isec.PD.ClientUI.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.stage.Stage;

import java.io.IOException;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class AutenticarController {
    public Label lblError;
    public Button loginButton;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    public Label lblAutenticar;
    public Button voltarButton;

    public AutenticarController() {
        this.emailField = new TextField();
        this.passwordField = new PasswordField();
        this.lblError = new Label();
        this.lblAutenticar = new Label();
        this.loginButton = new Button();
        this.voltarButton = new Button();
    }

    @FXML
    public void handleLogin() throws IOException, InterruptedException {
        if (emailField.getText().isBlank() || emailField.getText().isEmpty() || emailField == null)
            emailField.setBorder(new Border(new BorderStroke(javafx.scene.paint.Color.RED, BorderStrokeStyle.SOLID, null, null)));
        else
            emailField.setBorder(null);
        if (passwordField.getText().isBlank() || passwordField.getText().isEmpty() || passwordField == null)
            passwordField.setBorder(new Border(new BorderStroke(javafx.scene.paint.Color.RED, BorderStrokeStyle.SOLID, null, null)));
        else
            passwordField.setBorder(null);

        if (!controller.autenticar(emailField.getText(), passwordField.getText())) {
            lblError.setText(controller.getError());
            lblError.setVisible(true);
            voltar();
        } else {
            lblAutenticar.setText(controller.getAutenticar());
            lblAutenticar.setVisible(true);
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

    public void voltar() throws IOException {
        handleToMenuInicial();
    }

    @FXML
    public void handleToMenuInicial() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/MenuInicialPD.fxml"));
        Parent root = loader.load();

        // Crie a cena usando a raiz carregada do FXML
        Scene scene = new Scene(root);

        // Obtenha o palco da aplicação do botão ou de outra maneira adequada
        Stage stage = (Stage) loginButton.getScene().getWindow();

        // Defina a nova cena no palco
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Exiba o palco
        stage.show();
    }
}
