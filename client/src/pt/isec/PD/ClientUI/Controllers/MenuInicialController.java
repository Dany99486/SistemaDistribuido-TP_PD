package pt.isec.PD.ClientUI.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class MenuInicialController {
    public Button btnAutenticar;
    public Button btnRegistar;
    public Button btnCreditos;
    @FXML
    private Label lblError;
    private boolean avanca;

    public MenuInicialController() {
        this.btnRegistar = new Button();
        this.btnAutenticar = new Button();
        this.btnCreditos = new Button();
        this.lblError = new Label();

        if (!controller.parametrosSuficientes()) {
            btnAutenticar.setDisable(true);
            btnRegistar.setDisable(true);
            lblError.setText("Sintaxe: java Cliente serverAddress serverUdpPort");
            try {
                Thread.sleep(1000); //tempo para ler a mensagem de erro
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
        avanca = controller.socketParaCliente();
    }

    private void errorServer() {
        lblError.setText("Erro ao conectar ao servidor");
        System.err.println("Erro ao conectar ao servidor");
        try {
            Thread.sleep(1000); //tempo para ler a mensagem de erro
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @FXML
    public void handleToAutenticar() throws IOException {
        if (!avanca)
            errorServer();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/Autenticar.fxml"));
        Parent root = loader.load();

        // Crie a cena usando a raiz carregada do FXML
        Scene scene = new Scene(root);

        // Obtenha o palco da aplicação do botão ou de outra maneira adequada
        Stage stage = (Stage) btnAutenticar.getScene().getWindow();

        // Defina a nova cena no palco
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Exiba o palco
        stage.show();
    }
    @FXML
    public void handleToRegistar() throws IOException {
        if (!avanca)
            errorServer();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/Registar.fxml"));
        Parent root = loader.load();

        // Crie a cena usando a raiz carregada do FXML
        Scene scene = new Scene(root);

        // Obtenha o palco da aplicação do botão ou de outra maneira adequada
        Stage stage = (Stage) btnAutenticar.getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Defina a nova cena no palco
        stage.setScene(scene);

        // Exiba o palco
        stage.show();
    }
    @FXML
    public void handleToCreditos() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/Creditos.fxml"));
        Parent root = loader.load();

        // Crie a cena usando a raiz carregada do FXML
        Scene scene = new Scene(root);

        // Obtenha o palco da aplicação do botão ou de outra maneira adequada
        Stage stage = (Stage) btnAutenticar.getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Defina a nova cena no palco
        stage.setScene(scene);

        // Exiba o palco
        stage.show();
    }
}
