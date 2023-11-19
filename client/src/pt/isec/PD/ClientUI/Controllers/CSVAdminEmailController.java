package pt.isec.PD.ClientUI.Controllers;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class CSVAdminEmailController {
    public TextField emailField;
    public Label lblResult;

    public CSVAdminEmailController() {
        this.lblResult = new Label();
        this.emailField = new TextField();
    }

    public void downloadFile() throws InterruptedException {
        lblResult.setText(null);
        if (emailField.getText().isEmpty() || emailField.getText().isBlank())
            lblResult.setText("Preencha o campo");
        else {
            lblResult.setText("A fazer download do ficheiro");
            Thread.sleep(300);
            if (controller.obterCSVAdminEmail(emailField.getText()))
                lblResult.setText("Ficheiro obtido com sucesso");
            else
                lblResult.setText("Erro ao obter ficheiro");
        }
    }
}
