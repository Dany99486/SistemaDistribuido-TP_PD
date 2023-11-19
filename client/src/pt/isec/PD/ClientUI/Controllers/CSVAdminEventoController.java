package pt.isec.PD.ClientUI.Controllers;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class CSVAdminEventoController {


    public Label lblResult;
    public TextField nomeField;

    public CSVAdminEventoController() {
        this.lblResult = new Label();
        this.nomeField = new TextField();
    }

    public void downloadFile() throws InterruptedException {
        lblResult.setText(null);
        if (nomeField.getText().isEmpty() || nomeField.getText().isBlank())
            lblResult.setText("Preencha o campo");
        else {
            lblResult.setText("A fazer download do ficheiro");
            Thread.sleep(300);
            if (controller.obterCSVAdminEvento(nomeField.getText()))
                lblResult.setText("Ficheiro obtido com sucesso");
            else
                lblResult.setText("Erro ao obter ficheiro");
        }
    }
}
