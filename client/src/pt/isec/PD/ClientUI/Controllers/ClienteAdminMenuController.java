package pt.isec.PD.ClientUI.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.PD.ClientUI.MainJFX;

import java.io.IOException;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class ClienteAdminMenuController {
    public Button btnLogout;
    public Button btnPresencas;
    public Button btnEventos;
    public ClienteAdminMenuController() {
        this.btnPresencas = new Button();
        this.btnEventos = new Button();
        this.btnLogout = new Button();
    }

    public void handlePresencas() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/PresencasAdmin.fxml"));
        Parent root = loader.load();

        // Crie a cena usando a raiz carregada do FXML
        Scene scene = new Scene(root);

        // Obtenha o palco da aplicação do botão ou de outra maneira adequada
        Stage stage = (Stage) btnPresencas.getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Defina a nova cena no palco
        stage.setScene(scene);

        // Exiba o palco
        stage.show();
    }

    public void handleEventos() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/EventosAdmin.fxml"));
        Parent root = loader.load();

        // Crie a cena usando a raiz carregada do FXML
        Scene scene = new Scene(root);

        // Obtenha o palco da aplicação do botão ou de outra maneira adequada
        Stage stage = (Stage) btnEventos.getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Defina a nova cena no palco
        stage.setScene(scene);

        // Exiba o palco
        stage.show();
    }

    public void handleGerarCodigo() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainJFX.class.getResource("/pt/isec/PD/ClientUI/Views/GeracaoCodigo.fxml"));
        Parent root = loader.load();

        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Código");
        modalStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Definir o conteúdo da janela modal
        Scene scene = new Scene(root);
        modalStage.setScene(scene);

        // Mostrar a janela modal
        modalStage.showAndWait();
    }

    public void logout() {
        if(controller.logout())
            System.exit(0);
    }

    public void obterCSVNomeEvento() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainJFX.class.getResource("/pt/isec/PD/ClientUI/Views/CSVAdminEmail.fxml"));
        Parent root = loader.load();

        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Evento");
        modalStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Definir o conteúdo da janela modal
        Scene scene = new Scene(root);
        modalStage.setScene(scene);

        // Mostrar a janela modal
        modalStage.showAndWait();
    }

    public void obterCSVEmail() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainJFX.class.getResource("/pt/isec/PD/ClientUI/Views/CSVAdminEvento.fxml"));
        Parent root = loader.load();

        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Utilizador");
        modalStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Definir o conteúdo da janela modal
        Scene scene = new Scene(root);
        modalStage.setScene(scene);

        // Mostrar a janela modal
        modalStage.showAndWait();
    }
}
