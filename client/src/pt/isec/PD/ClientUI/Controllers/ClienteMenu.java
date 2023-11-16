package pt.isec.PD.ClientUI.Controllers;

import javafx.scene.control.Button;

import java.io.IOException;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class ClienteMenu extends MenuInicialController {
    public Button btnLogout;
    public Button ObterCSV;
    public Button ConsultarPresenca;
    public Button SubmeterCodigo;
    public Button EditarRegisto;

    public void initialize() {
        btnLogout = new Button();
        ObterCSV = new Button();
        ConsultarPresenca = new Button();
        SubmeterCodigo = new Button();
        EditarRegisto = new Button();
    }

    public void handleClienteMenu() {
    }

    public void logout() {
        if(controller.logout())
            System.exit(0);
    }

    public void editaregisto() throws IOException {
        if (controller.pedeDadosRegisto())
            handleToEditaDados();
    }
}
