package pt.isec.PD.ClientUI.Controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class SubmeteCodigoCrontroller {
    public TextField txtCodigo;
    public Label lblMessage;
    public Button SubmeterCodigo;

    public SubmeteCodigoCrontroller() {
        txtCodigo = new TextField();
        lblMessage = new Label();
        SubmeterCodigo = new Button();
    }

    public void handleSubmeterCodigo() {
        System.out.println("Submeter Codigo");
        String code = txtCodigo.getText();
        String res = "Erro";

        if(code != null && !code.isEmpty())
            res = controller.submeteCodigo(code);

        lblMessage.setText(res);
        lblMessage.setVisible(true);
    }
}
