package pt.isec.PD.ClientUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

    public RegistarController() {
        this.nomeField = new TextField();
        this.CCField = new TextField();
        this.emailField = new TextField();
        this.passwordField = new PasswordField();
        this.lblError = new Label();
        this.lblRegistar = new Label();
        this.loginButton = new Button();
    }

    @FXML
    public void handleRegistar() throws IOException {
        controller.registar(nomeField.getText(), CCField.getText(), emailField.getText(), passwordField.getText());
        if(controller.getError() != null) {
            lblError.setText(controller.getError());
            lblError.setVisible(true);
        }
        lblRegistar.setText(controller.getAutenticar());
    }
}
