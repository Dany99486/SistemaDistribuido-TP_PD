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

    public boolean registar(String nomeFieldText, String cc, String emailFieldText, String passwordFieldText) {
        return cliente.registar(nomeFieldText, cc, emailFieldText, passwordFieldText);
    }

    public String submeteCodigo(String code) {
        return cliente.submeteCodigo(code);
    }
    public List<Evento> consultarEventos() {
        return cliente.consultarEventos();
    }
    public List<Presencas> consultarPresencas() {
        return cliente.consultarPresencas();
    }

    public boolean criarEvento(String nome, String local, String dataInicioFormatada, String dataFimFormatada, String horaInicio, String horaFim) {
        return cliente.criarEvento(nome, local, dataInicioFormatada, dataFimFormatada, horaInicio, horaFim);
    }

    public boolean eliminaEvento(String nome) {
        return cliente.eliminaEvento(nome);
    }

    public boolean editarEvento(String nomeCampoAlterado, String novaAlteracao, String nome) {
        return cliente.editarEvento(nomeCampoAlterado, novaAlteracao, nome);
    }

    public boolean eliminaPresenca(String id) {
        return cliente.eliminaPresenca(id);
    }

    public boolean inserirPresencas(String nome, String email) {
        return cliente.inserirPresencas(nome, email);
    }

    public String gerarCodigo(String nomeEvento, String tempo) {
        return cliente.gerarCodigo(nomeEvento, tempo);
    }

    public boolean obterCSVCliente(String campo, String param) {
        return cliente.obterCSVCliente(campo, param);
    }

    public boolean obterCSVAdminEmail(String n) {
        return cliente.obterCSVAdminEmail(n);
    }

    public boolean obterCSVAdminEvento(String n) {
        return cliente.obterCSVAdminEvento(n);
    }
}
