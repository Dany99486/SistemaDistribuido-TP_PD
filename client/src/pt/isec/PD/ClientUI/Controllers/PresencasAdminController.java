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
import pt.isec.PD.ClientUI.Model.Presencas;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class PresencasAdminController {
    public TextField searchField;
    public Button btnCriar;
    public Button btnEliminar;
    @FXML
    public TableView<Presencas> tableView;

    public TableColumn<Presencas, String> id;

    public TableColumn<Presencas, String> codigo;
    public TableColumn<Presencas, String> idEvento;
    public TableColumn<Presencas, String> cartaoCidadao;
    public TableColumn<Presencas, String> horaInicio;
    public TableColumn<Presencas, String> horaFim;

    public void initialize(){

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        idEvento.setCellValueFactory(new PropertyValueFactory<>("idEvento"));
        cartaoCidadao.setCellValueFactory(new PropertyValueFactory<>("cartaoCidadao"));
        horaInicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        horaFim.setCellValueFactory(new PropertyValueFactory<>("horaFim"));

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
    public void handleEliminar(){
        Presencas presencaSelecionado = tableView.getSelectionModel().getSelectedItem();
        if (presencaSelecionado != null) {

            if(!controller.eliminaPresenca(presencaSelecionado.getId())) {
                System.out.println("Não foi possível eliminar o evento");
            } else {
                System.out.println("Evento eliminado com sucesso");
            }

            // Atualiza a tabela
            updateTable();
        }
    }

    public void onSearch(){
        String nome = searchField.getText().toLowerCase();
        List<Presencas> filtrados = controller.consultarPresencas().stream()
                .filter(p -> p.getCartaoCidadao().toLowerCase().contains(nome))
                .collect(Collectors.toList());

        tableView.setItems(FXCollections.observableArrayList(filtrados));
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
        List<Presencas> presencas = controller.consultarPresencas();
        tableView.getItems().clear();
        tableView.getItems().addAll(presencas);
    }
}
