package pt.isec.PD.ClientUI.Model;

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

    public String getError() {
        return cliente.getError();
    }

    public String getAutenticar() {
        return cliente.getAutenticar();
    }

    public boolean isAdmin() {
        return cliente.isAdmin();
    }

}
