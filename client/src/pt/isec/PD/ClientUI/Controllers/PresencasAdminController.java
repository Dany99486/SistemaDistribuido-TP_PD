package pt.isec.PD.ClientUI.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.PD.ClientUI.MainJFX;
import pt.isec.PD.ClientUI.Model.Evento;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class PresencasAdminController {
    public TextField searchField;
    public Button btnCriar;
    public Button btnEditar;
    public Button btnEliminar;
    @FXML
    public TableView<Evento> tableView;
    public TableColumn<Evento, String> nomeColumn;
    public TableColumn<Evento, String> localColumn;
    public TableColumn<Evento, String> dataColumn;
    public TableColumn<Evento, String> horaInicioColumn;
    public TableColumn<Evento, String> horaFimColumn;

    public void initialize(){
        /*
        tableView = new TableView<>();
        nomeColumn = new TableColumn<>();
        localColumn = new TableColumn<>();
        dataColumn = new TableColumn<>();
        horaInicioColumn = new TableColumn<>();
        horaFimColumn = new TableColumn<>();*/

        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        localColumn.setCellValueFactory(new PropertyValueFactory<>("local"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        horaInicioColumn.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        horaFimColumn.setCellValueFactory(new PropertyValueFactory<>("horaFim"));

        updateTable();
    }

    public void handleCriar() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainJFX.class.getResource("/pt/isec/PD/ClientUI/Views/InserirPresencas.fxml"));
        Parent root = loader.load();

        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Criar Presenças");

        // Definir o conteúdo da janela modal
        Scene scene = new Scene(root);
        modalStage.setScene(scene);

        // Mostrar a janela modal
        modalStage.showAndWait();
    }
    public void handleEditar() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainJFX.class.getResource("/pt/isec/PD/ClientUI/Views/EditarPresencas.fxml"));
        Parent root = loader.load();

        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Editar Presenças");

        // Definir o conteúdo da janela modal
        Scene scene = new Scene(root);
        modalStage.setScene(scene);

        // Mostrar a janela modal
        modalStage.showAndWait();
    }
    public void handleEliminar(){
        return;
    }

    public void onSearch(){
        String nome = searchField.getText().toLowerCase();
        List<Evento> funcionariosFiltrados = controller.consultarPresencas().stream()
                .filter(evento -> evento.getNome().toLowerCase().contains(nome))
                .collect(Collectors.toList());

        tableView.setItems(FXCollections.observableArrayList(funcionariosFiltrados));
    }

    public void voltar() throws IOException {
        handleToClienteMenu();
    }

    @FXML
    public void handleToClienteMenu() throws IOException {
        FXMLLoader loader;
        if (controller.isAdmin()) {
            loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/ClienteAdminMenu.fxml"));
        } else {
            loader = new FXMLLoader(getClass().getResource("/pt/isec/PD/ClientUI/Views/ClienteMenu.fxml"));
        }
        Parent root = loader.load();

        // Crie a cena usando a raiz carregada do FXML
        Scene scene = new Scene(root);

        // Obtenha o palco da aplicação do botão ou de outra maneira adequada
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pt/isec/PD/ClientUI/Img/logo.png"))));

        // Defina a nova cena no palco
        stage.setScene(scene);

        // Exiba o palco
        stage.show();
    }
    public void updateTable() {
        List<Evento> eventos = controller.consultarPresencas();
        tableView.getItems().clear();
        tableView.getItems().addAll(eventos);
    }
}
