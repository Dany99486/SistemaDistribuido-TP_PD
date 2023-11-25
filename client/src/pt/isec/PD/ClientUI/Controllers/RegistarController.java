package pt.isec.PD.ClientUI.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class RegistarController {
    public Label lblError;
    public Button loginButton;
    @FXML
    public TextField nomeField;
    @FXML
    public TextField CCField;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    public Label lblRegistar;
    public Button voltarButton;

    public RegistarController() {
        this.nomeField = new TextField();
        this.CCField = new TextField();
        this.emailField = new TextField();
        this.passwordField = new PasswordField();
        this.lblError = new Label();
        this.lblRegistar = new Label();
        this.loginButton = new Button();
        this.voltarButton = new Button();
    }

    @FXML
    public void handleRegistar() throws IOException, InterruptedException {
        if (controller.registar(nomeField.getText(), CCField.getText(), emailField.getText(), passwordField.getText())) {
            lblError.setVisible(false);
            lblRegistar.setText(controller.getAutenticar());
            Thread.sleep(500);
            handleToMenuInicial();
        } else if(controller.getError() != null) {
            lblError.setText(controller.getError());
            lblError.setVisible(true);
        }
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

    public void voltar() throws IOException {
        handleToMenuInicial();
    }
}
