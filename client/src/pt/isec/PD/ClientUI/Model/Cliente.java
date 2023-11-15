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
    private Socket socket;
    private String error;
    private String autenticar;
    private boolean admin = false;

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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean autenticar(String email, String password) {
        String message = null;
        try (
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
        ) {
            if (email.isEmpty() || password.isEmpty()) {
                error = "Preencha todos os campos!";
            } else {
                //TODO: Enviar para o servidor
                message = "AUTENTICAR";

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
        }
        return true;
    }

    public boolean registar( String nome, String cc, String email, String password) {
        String message = null;
        try (
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
        ) {
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

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ocorreu a excepcao {" + e + "} ao nível do socket TCP de leitura do cliente!");
            e.printStackTrace();
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
}
