package pt.isec.PD.ClientUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.isec.PD.ClientUI.Model.Evento;

import java.util.List;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class ConsultaPresencasController {
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

    public void onSearch() {

    }


    public void updateTable() {
        List<Evento> eventos = controller.consultarPresencas();
        tableView.getItems().clear();
        tableView.getItems().addAll(eventos);
    }
}
