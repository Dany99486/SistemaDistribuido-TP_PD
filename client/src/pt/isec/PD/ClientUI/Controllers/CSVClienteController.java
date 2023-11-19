package pt.isec.PD.ClientUI.Controllers;


import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class CSVClienteController {
    public TextField campoField;
    public TextField paramField;
    public Button btnInserir;
    public Label lblResult;

    public CSVClienteController() {
        this.btnInserir = new Button();
        this.campoField = new TextField();
        this.paramField = new TextField();
        this.lblResult = new Label();
    }

    public void downloadFile() throws InterruptedException {
        if (campoField.getText().isEmpty() || paramField.getText().isEmpty())
            lblResult.setText("Preencha todos os campos");
        if (campoField.getText().isBlank() || paramField.getText().isBlank())
            lblResult.setText("Preencha todos os campos");
        else {
            lblResult.setText("A fazer download do ficheiro");
            Thread.sleep(300);
            if (controller.obterCSVCliente(campoField.getText(), paramField.getText()))
                lblResult.setText("Ficheiro obtido com sucesso");
            else
                lblResult.setText("Erro ao obter ficheiro");
        }
    }
}
