package pt.isec.PD.ClientUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.isec.PD.ClientUI.Model.Evento;

import java.time.LocalDate;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class EditEventoController {
    @FXML
    public Label lblResult;
    private ModalCallback callback;
    public void setModalCallback(ModalCallback callback) {
        this.callback = callback;
    }

    @FXML
    public TextField Nome;

    @FXML
    public TextField Data;

    @FXML
    public TextField Local;

    @FXML
    public TextField horaInicio;
    @FXML
    public TextField horaFim;
    @FXML
    public Button cancelarButton;
    @FXML
    public Button editarButton;

    private Evento evento;

    public EditEventoController() {
        this.Nome = new TextField();
        this.Data = new TextField();
        this.Local = new TextField();
        this.horaInicio = new TextField();
        this.horaFim = new TextField();
        this.cancelarButton = new Button();
        this.editarButton = new Button();
        this.lblResult = new Label();
    }
    @FXML
    public void onEditarButton() {
        String nomeCampoAlterado = "";
        String novaAlteracao = "";
        boolean invalido = false;

        if (!Nome.getText().equals(evento.getNome())) {
            nomeCampoAlterado = "nome";
            novaAlteracao = Nome.getText();
            evento.setNome(novaAlteracao);
        } else if (!Data.getText().equals(evento.getData())) {
            nomeCampoAlterado = "data";
            novaAlteracao = Data.getText();
            evento.setData(novaAlteracao);
        } else if (!Local.getText().equals(evento.getLocal())) {
            nomeCampoAlterado = "local";
            novaAlteracao = Local.getText();
            evento.setLocal(novaAlteracao);
        } else if (!horaInicio.getText().equals(evento.getHoraInicio())) {
            nomeCampoAlterado = "horaInicio";
            novaAlteracao = horaInicio.getText();

            if (!novaAlteracao.contains(":") || novaAlteracao.length() != 5) {
                lblResult.setText("Hora de Inicio inválida!");
                invalido = true;
            } else {
                evento.setHoraInicio(novaAlteracao);
            }
        } else if (!horaFim.getText().equals(evento.getHoraFim())) {
            nomeCampoAlterado = "horaFim";
            novaAlteracao = horaFim.getText();

            if (!novaAlteracao.contains(":") || novaAlteracao.length() != 5) {
                lblResult.setText("Hora de Fim inválida!");
                invalido = true;
            } else {
                evento.setHoraFim(novaAlteracao);
            }
        }

        if (novaAlteracao.isBlank() || invalido) {
            lblResult.setText("Nenhuma alteração foi feita");
        } else {
            if (!nomeCampoAlterado.isEmpty() && !novaAlteracao.isEmpty()) {
                // Chame o método do controlador com os parâmetros desejados
                if (controller.editarEvento(nomeCampoAlterado, novaAlteracao, evento.getNome())) {
                    //System.out.println("Evento editado com sucesso");
                    lblResult.setText("Evento editado com sucesso");
                } else {
                    //System.out.println("Erro ao editar evento");
                    lblResult.setText("Erro ao editar evento");
                }
            } else {
                //System.out.println("Nenhuma alteração foi feita");
                lblResult.setText("Nenhuma alteração foi feita");
            }
        }

        // Fechar o modal
        Stage stage = (Stage) editarButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onCancelarButton() {
        // Fechar o modal sem fazer nada
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }

    public void setEventoParaEdicao(Evento evento) {
        this.evento = evento;

        Nome.setText(evento.getNome());
        Data.setText(evento.getData());
        Local.setText(evento.getLocal());
        horaInicio.setText(evento.getHoraInicio());
        horaFim.setText(evento.getHoraFim());

    }
}
