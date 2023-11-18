package pt.isec.PD.ClientUI.Controllers;

import javafx.scene.control.Button;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class ClienteAdminMenuController {
    public Button btnLogout;
    public ClienteAdminMenuController() {
        this.btnLogout = new Button();
    }

    public void logout() {
        if(controller.logout())
            System.exit(0);
    }
}
