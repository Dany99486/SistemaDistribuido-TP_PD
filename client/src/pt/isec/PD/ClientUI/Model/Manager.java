package pt.isec.PD.ClientUI.Model;

import java.util.List;

public class Manager {
    private Cliente cliente;
    private String[] args;

    public void arrancaCliente(String[] a) {
        this.args = a;
        cliente = new Cliente(args);
    }

    public boolean parametrosSuficientes() {
        return cliente.clientLength();
    }

    public boolean socketParaCliente() {
        return cliente.socketClient();
    }

    public boolean autenticar(String email, String password) {
        return cliente.autenticar(email, password);
    }

    public boolean pedeDadosRegisto() {
        return cliente.pedeDadosParaRegisto();
    }

    public boolean registaDados(String n, String e, String p) {
        return cliente.editaRegistoConta(n, e, p);
    }

    public String getError() {
        return cliente.getError();
    }

    public String getAutenticar() {
        return cliente.getAutenticar();
    }

    public boolean isAdmin() {
        return cliente.isAdmin();
    }

    public String getNome() {
        return cliente.getNome();
    }

    public String getEmail() {
        return cliente.getEmail();
    }

    public boolean logout() {
        return cliente.logout();
    }

    public String getLogout() {
        return cliente.getLogout();
    }

    public void registar(String nomeFieldText, String cc, String emailFieldText, String passwordFieldText) {
        cliente.registar(nomeFieldText, cc, emailFieldText, passwordFieldText);
    }

    public String submeteCodigo(String code) {
        return cliente.submeteCodigo(code);
    }
    public List<Evento> consultarPresencas() {
        return cliente.consultarPresencas();
    }
}
