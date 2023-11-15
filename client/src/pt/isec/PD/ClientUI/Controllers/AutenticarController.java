package pt.isec.PD.ClientUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class AutenticarController extends MenuInicialController {
    public Label lblError;
    public Button loginButton;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    public Label lblAutenticar;

    public void initialize() {
        loginButton = new Button();
    }

    @FXML
    public void handleLogin() throws IOException {
        controller.autenticar(emailField.toString(), passwordField.toString());
        lblError.setText(controller.getError());
        lblAutenticar.setText(controller.getAutenticar());
    }
}
