package pt.isec.PD.ClientUI.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.isec.PD.ClientUI.Model.Evento;

import java.util.List;
import java.util.stream.Collectors;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class ConsultaEventosController {
    public TextField searchField;
    @FXML
    public TableView<Evento> tableView;
    public TableColumn<Evento, String> nomeColumn;
    public TableColumn<Evento, String> localColumn;
    public TableColumn<Evento, String> dataColumn;
    public TableColumn<Evento, String> horaInicioColumn;
    public TableColumn<Evento, String> horaFimColumn;
    public TableColumn<Evento, String> codeColumn;
    public TableColumn<Evento, String> validadeColumn;


    public void initialize(){
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        localColumn.setCellValueFactory(new PropertyValueFactory<>("local"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        horaInicioColumn.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        horaFimColumn.setCellValueFactory(new PropertyValueFactory<>("horaFim"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        validadeColumn.setCellValueFactory(new PropertyValueFactory<>("validade"));

        updateTable();
    }

    public void onSearch() {
        String nome = searchField.getText().toLowerCase();
        List<Evento> filtrados = controller.consultarEventos().stream()
                .filter(evento -> evento.getNome().toLowerCase().contains(nome))
                .collect(Collectors.toList());

        tableView.setItems(FXCollections.observableArrayList(filtrados));
    }


    public void updateTable() {
        List<Evento> eventos = controller.consultarEventos();
        tableView.getItems().clear();
        tableView.getItems().addAll(eventos);
    }
}
