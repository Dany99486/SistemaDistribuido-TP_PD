package pt.isec.PD.ClientUI.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Cliente {
    private final String AUTENTICAR = "AUTENTICAR";
    private final String REGISTAR = "REGISTAR";
    private String[] args;
    private static Socket socket;
    private String error;
    private String autenticar;
    private String editado;

    private String logout;
    private String nome, email, password; //Usar para os campos de edicao de registo
    private boolean admin = false;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Cliente(String[] args) {
        this.args = args;
    }

    public boolean clientLength() {
        return args.length == 2;
    }

    public boolean socketClient() {
        if (!clientLength()) {
            return false;
        }
        try {
            socket = new Socket(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
            try {
                this.in = new ObjectInputStream(socket.getInputStream());
                this.out = new ObjectOutputStream(socket.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean autenticar(String email, String password) {
        String message = null;
        try {
            if (email.isEmpty() || password.isEmpty()) {
                error = "Preencha todos os campos!";
            } else {
                //TODO: Enviar para o servidor
                message = AUTENTICAR;

                autenticar = "A autenticar...";
                out.writeObject(message);
                out.flush();

                message = email + " " + password;
            }
            out.writeObject(message);
            out.flush();

            String response = (String) in.readObject();
            System.out.println(response);

            if (response.contains("Admin bem-vindo")) {
                admin = true;
                autenticar = response;
            } else if (response.contains("Logado com sucesso")) {
                admin = false;
                autenticar = response;
            } else {
                error = "Tente novamente";
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ocorreu a excepcao {" + e + "} ao nível do socket TCP de leitura do cliente!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean registar(String nome, String cc, String email, String password) {
        String message = null;
        try {
            if (nome.isEmpty() || cc.isEmpty() || email.isEmpty() || password.isEmpty()) {
                error = "Preencha todos os campos!";
            } else {
                //TODO: Enviar para o servidor

                autenticar = "A registar...";

                message = REGISTAR + " " + email + " " + password + " " + cc + " " + nome;
            }
            out.writeObject(message);
            out.flush();

            String response = (String) in.readObject();
            System.out.println(response);

            autenticar = response;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ocorreu a excepcao {" + e + "} ao nível do socket TCP de leitura do cliente!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean pedeDadosParaRegisto() {
        String message;
        try {
            message = "DADOS";
            out.writeObject(message);
            out.flush();

            String response = (String) in.readObject();
            String[] dados = response.split(" ");

            nome = dados[0];
            email = dados[1];
            password = dados[2];
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ocorreu a excepcao {" + e + "} ao nível do socket TCP de leitura do cliente!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean editaRegistoConta(String nome, String email, String password) {
        String message;
        try {
            //Pedir dados primeiro
            //Depois enviar os dados
            String aux = null;
            if (!nome.isEmpty() || !nome.isBlank())
                if (!nome.equals(this.nome))
                    aux = "nome " + nome;
            if (!email.isEmpty() || !email.isBlank())
                if (!email.equals(this.email))
                    aux = "email " + email;
            if (!password.isEmpty() || !password.isBlank())
                if (!password.equals(this.password))
                    aux = "password " + password;

            if (aux == null)
                return false;

            message = "EDICAO  " + aux;

            out.writeObject(message);
            out.flush();

            String response = (String) in.readObject();
            System.out.println(response);

            editado = response;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ocorreu a excepcao {" + e + "} ao nível do socket TCP de leitura do cliente!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean logout() {
        try {
            out.writeObject("LOGOUT");
            out.flush();

            logout = (String) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ocorreu a excepcao {" + e + "} ao nível do socket TCP de leitura do cliente!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getError() {
        return error;
    }

    public String getAutenticar() {
        return autenticar;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getLogout() {
        return logout;
    }
}
