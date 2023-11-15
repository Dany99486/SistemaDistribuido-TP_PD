module client {
    requires javafx.controls;
    requires javafx.fxml;

    exports pt.isec.PD.ClientUI.Controllers;
    exports pt.isec.PD.ClientUI.Model;
    exports pt.isec.PD.ClientUI;

    opens pt.isec.PD.ClientUI.Controllers to javafx.fxml;


}