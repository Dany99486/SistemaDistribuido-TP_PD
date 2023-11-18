package pt.isec.PD.ClientUI.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class EditaDados {
    public TextField Nome;
    public TextField Email;
    public TextField Password;
    public Label lblError;

    public EditaDados() {
        Nome = new TextField();
        Email = new TextField();
        Password = new TextField();
        lblError = new Label();
    }

    public void edita() throws InterruptedException, IOException {
        if (!controller.registaDados(Nome.getText(), Email.getText(), Password.getText()))
            lblError.setText("Campos inválidos");
        else {
            lblError.setText("Dados guardados");
            Thread.sleep(1000);
            handleToClienteMenu();
        }
    }

    public void voltar() throws IOException {
        handleToClienteMenu();
    }

    @FXML
    public void handleToClienteMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/ClienteMenu.fxml"));
        Parent root = loader.load();

        // Crie a cena usando a raiz carregada do FXML
        Scene scene = new Scene(root);

        // Obtenha o palco da aplicação do botão ou de outra maneira adequada
        Stage stage = (Stage) lblError.getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Defina a nova cena no palco
        stage.setScene(scene);

        // Exiba o palco
        stage.show();
    }
}
