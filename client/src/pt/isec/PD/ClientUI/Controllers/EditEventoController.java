package pt.isec.PD.ClientUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.isec.PD.ClientUI.Model.Evento;

import java.time.LocalDate;

public class EditEventoController {
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
    public Button cancelarButton;
    @FXML
    public Button editarButton;

    private Evento evento;
    @FXML
    public void onEditarButton() {

        // Lógica para criar um novo funcionário com os dados inseridos
        String nome = Nome.getText();
        String data = Data.getText();
        String local = Local.getText();
        String horaInicio = this.horaInicio.getText();


        evento.setNome(nome);
        evento.setLocal(local);
        evento.setData(data);
        evento.setHoraInicio(horaInicio);

            //if (callback != null) {
            //    callback.onFuncionarioEditado(evento);
           // }

            // Fechar o modal
            Stage stage = (Stage) editarButton.getScene().getWindow();
            stage.close();



    }

    @FXML
    public void handleCancelarButton() {
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

    }
}
