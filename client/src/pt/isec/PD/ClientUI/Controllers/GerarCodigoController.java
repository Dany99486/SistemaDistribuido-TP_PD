package pt.isec.PD.ClientUI.Controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class GerarCodigoController {
    public TextField codigoField;
    public TextField tempoField;
    public TextField nomeEventoField;
    public Button btnGerar;
    public Label lblMsg;

    public void hangleGerarCodigo() {
        String tempo = tempoField.getText();
        String nomeEvento = nomeEventoField.getText();

        if (tempo.isEmpty() || nomeEvento.isEmpty()) {
            lblMsg.setText("Preencha todos os campos!");
        } else {
            String resposta = controller.gerarCodigo(nomeEvento, tempo);

            if (resposta.startsWith("Codigo de registo de presenças: ")) {
                String registo = resposta.substring("Codigo de registo de presenças: ".length());

                codigoField.setText(registo);

                lblMsg.setText("");
            } else {
                lblMsg.setText(resposta);

                codigoField.setText("");
            }
        }
    }
}
