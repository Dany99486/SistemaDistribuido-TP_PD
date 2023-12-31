package pt.isec.PD.ClientUI.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.isec.PD.ClientUI.Model.Evento;
import pt.isec.PD.ClientUI.Model.Presencas;

import java.util.List;
import java.util.stream.Collectors;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class ConsultaPresencasController {
    public TextField searchField;
    @FXML
    public TableView<Evento> tableView;
    public TableColumn<Evento, String> nomeColumn;
    public TableColumn<Evento, String> localColumn;
    public TableColumn<Evento, String> dataColumn;
    public TableColumn<Evento, String> horaInicioColumn;
    public TableColumn<Evento, String> horaFimColumn;


    public void initialize(){

        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        localColumn.setCellValueFactory(new PropertyValueFactory<>("local"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        horaInicioColumn.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        horaFimColumn.setCellValueFactory(new PropertyValueFactory<>("horaFim"));

        updateTable();
    }

    public void onSearch() {
        String nomeEvento = searchField.getText().toLowerCase();
        List<Evento> filtrados = controller.consultarEventos().stream()
                .filter(e -> e.getNome().toLowerCase().contains(nomeEvento))
                .collect(Collectors.toList());

        tableView.setItems(FXCollections.observableArrayList(filtrados));
    }


    public void updateTable() {
        List<Evento> presencas = controller.consultarEventos(); //No caso do utilizador ele recebe o evento dele
        tableView.getItems().clear();
        tableView.getItems().addAll(presencas);
    }
}
