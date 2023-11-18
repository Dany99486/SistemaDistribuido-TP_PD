package pt.isec.PD.ClientUI.Controllers;

import pt.isec.PD.ClientUI.Model.Evento;

public interface ModalCallback {
    void onEventoCriado(Evento evento);
    void onEventoEditado(Evento evento);
}
