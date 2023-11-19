package pt.isec.PD.ClientUI.Controllers;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class CriaEventoController {
    public TextField nomeField;
    public TextField localField;
    public DatePicker dataInicioField;
    public DatePicker dataFimField;
    public TextField horaInicioField;
    public TextField horaFimField;
    public Button btnCriar;
    public Label lblResult;

    public CriaEventoController() {
        this.nomeField = new TextField();
        this.localField = new TextField();
        this.dataInicioField = new DatePicker();
        this.dataFimField = new DatePicker();
        this.horaInicioField = new TextField();
        this.horaFimField = new TextField();
        this.btnCriar = new Button();
        this.lblResult = new Label();
    }

    public void handleCriar() {
        String nome = nomeField.getText();
        String local = localField.getText();
        LocalDate dataInicio = dataInicioField.getValue();
        LocalDate dataFim = dataFimField.getValue();
        String horaInicio = horaInicioField.getText();
        String horaFim = horaFimField.getText();

        if (nome.isEmpty() || local.isEmpty() || dataInicio == null || dataFim == null || horaInicio.isEmpty() || horaFim.isEmpty()
        || nome.isBlank() || local.isBlank() || horaInicio.isBlank() || horaFim.isBlank()) {
            System.out.println("Preencha todos os campos!");
            lblResult.setText("Preencha todos os campos!");
        } else {
            // Formatar as datas conforme desejado
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            String dataInicioFormatada = dataInicio.format(formatter);
            String dataFimFormatada = dataFim.format(formatter);
            // Verificar horas
            if (!horaInicio.contains(":") || !horaFim.contains(":") || horaInicio.length() != 5 || horaFim.length() != 5) {
                System.out.println("Hora inválida!");
                lblResult.setText("Hora inválida!");
            }
            System.out.println("nome: " + nome + "\nlocal: " + local + "\ndataInicio: " + dataInicioFormatada + "\ndataFim: " + dataFimFormatada + "\nhoraInicio: " + horaInicio + "\nhoraFim: " + horaFim);

            if(!controller.criarEvento(nome, local, dataInicioFormatada, dataFimFormatada, horaInicio, horaFim)) {
                System.out.println("Erro ao criar evento!");
                lblResult.setText("Erro ao criar evento!");
            } else {
                System.out.println("Evento criado com sucesso!");
                lblResult.setText("Evento criado com sucesso!");
                limparCampos();
            }

        }
    }

    private void limparCampos() {
        nomeField.clear();
        localField.clear();
        dataInicioField.setValue(null);
        dataFimField.setValue(null);
        horaInicioField.clear();
        horaFimField.clear();
    }
}