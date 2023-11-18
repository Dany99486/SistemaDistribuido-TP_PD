package pt.isec.PD.ClientUI.Controllers;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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

    public void handleCriar() {
        String nome = nomeField.getText();
        String local = localField.getText();
        LocalDate dataInicio = dataInicioField.getValue();
        LocalDate dataFim = dataFimField.getValue();
        String horaInicio = horaInicioField.getText();
        String horaFim = horaFimField.getText();

        if (nome.isEmpty() || local.isEmpty() || dataInicio == null || dataFim == null || horaInicio.isEmpty() || horaFim.isEmpty()) {
            System.out.println("Preencha todos os campos!");
        } else {
            // Formatar as datas conforme desejado
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            String dataInicioFormatada = dataInicio.format(formatter);
            String dataFimFormatada = dataFim.format(formatter);
            System.out.println("nome: " + nome + "\nlocal: " + local + "\ndataInicio: " + dataInicioFormatada + "\ndataFim: " + dataFimFormatada + "\nhoraInicio: " + horaInicio + "\nhoraFim: " + horaFim);

            if(!controller.criarEvento(nome, local, dataInicioFormatada, dataFimFormatada, horaInicio, horaFim)) {
                System.out.println("Erro ao criar evento!");
            } else {
                System.out.println("Evento criado com sucesso!");
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