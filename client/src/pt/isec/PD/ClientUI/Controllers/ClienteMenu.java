package pt.isec.PD.ClientUI.Controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class ClienteMenu extends MenuInicialController {
    public Button btnLogout;
    public Button ObterCSV;
    public Button ConsultarPresenca;
    public Button SubmeterCodigo;
    public Button EditarRegisto;
    public Label lblOut;

    public void initialize() {
        btnLogout = new Button();
        ObterCSV = new Button();
        ConsultarPresenca = new Button();
        SubmeterCodigo = new Button();
        EditarRegisto = new Button();
    }

    public void handleClienteMenu() {
    }

    public void logout() throws InterruptedException {
        if(controller.logout()) {
            lblOut.setText(controller.getLogout());
            Thread.sleep(500);
            System.exit(0);
        }
    }

    public void editaregisto() throws IOException {
        if (controller.pedeDadosRegisto())
            handleToEditaDados();
    }
}
