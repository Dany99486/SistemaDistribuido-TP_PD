package pt.isec.PD.ClientUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AutenticarController {
    public Label lblError;
    public Button loginButton;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;

    public void initialize(){
        loginButton = new Button();
    }

    @FXML
    public void handleLogin() throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        if(email.isEmpty() || password.isEmpty()){
            lblError.setText("Preencha todos os campos!");
        }

        //TODO: Enviar para o servidor
    }


}
