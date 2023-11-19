package pt.isec.PD.ClientUI.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.PD.ClientUI.MainJFX;

import java.io.IOException;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class ClienteMenu {
    public Button btnLogout;
    public Button ObterCSV;
    public Button ConsultarPresenca;
    public Button SubmeterCodigo;
    public Button EditarRegisto;
    public Label lblOut;

    public ClienteMenu() {
        this.btnLogout = new Button();
        this.ObterCSV = new Button();
        this.ConsultarPresenca = new Button();
        this.SubmeterCodigo = new Button();
        this.EditarRegisto = new Button();
        this.lblOut = new Label();
    }

    public void logout() throws InterruptedException {
        if(controller.logout()) {
            lblOut.setText(controller.getLogout());
            Thread.sleep(500);
            System.exit(0);
        }else
            lblOut.setText("Não foi possivel realizar o logout: " + controller.getLogout());
    }

    public void editaregisto() throws IOException {
        if (controller.pedeDadosRegisto())
            handleToEditaDados();
        else
            lblOut.setText("Erro ao pedir dados");
    }

    @FXML
    public void SubmeteCodigo() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainJFX.class.getResource("/pt/isec/PD/ClientUI/Views/SubmissaoCodigo.fxml"));
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

    @FXML
    public void handleToEditaDados() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/EditaDados.fxml"));
        Parent root = loader.load();

        EditaDados editaDadosController = loader.getController();
        // Agora você pode acessar e manipular os campos do controlador diretamente
        editaDadosController.Nome.setText(controller.getNome());
        editaDadosController.Email.setText(controller.getEmail());

        // Crie a cena usando a raiz carregada do FXML
        Scene scene = new Scene(root);

        // Obtenha o palco da aplicação do botão ou de outra maneira adequada
        Stage stage = (Stage) lblOut.getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Defina a nova cena no palco
        stage.setScene(scene);

        // Exiba o palco
        stage.show();
    }


    @FXML
    public void ConsultarPresenca() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/ConsultaPresencas.fxml"));
        Parent root = loader.load();

        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Codigo");
        modalStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Definir o conteúdo da janela modal
        Scene scene = new Scene(root);
        modalStage.setScene(scene);

        // Mostrar a janela modal
        modalStage.showAndWait();
    }

    @FXML
    public void handleToCSV() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/CSVCliente.fxml"));
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

    public void obterCSVCliente() throws IOException {
        handleToCSV();
    }
}
