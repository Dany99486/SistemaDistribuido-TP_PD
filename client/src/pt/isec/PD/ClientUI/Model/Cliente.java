package pt.isec.PD.ClientUI.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cliente {
    private final String PRESENCAS = "PRESENCAS";
    private final String AUTENTICAR = "AUTENTICAR";
    private final String REGISTAR = "REGISTAR";
    private final String CONSULTA = "CONSULTA";
    private final String EVENTO = "EVENTO";
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
            socket.setSoTimeout(10000);
            try {
                this.in = new ObjectInputStream(socket.getInputStream());
                this.out = new ObjectOutputStream(socket.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean autenticar(String email, String password) {
        String message;
        try {
            if (!socket.isConnected())
                return false;
            if (email.isEmpty() || password.isEmpty()) {
                error = "Preencha todos os campos!";
                return false;
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
                return false;
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
            if (!socket.isConnected())
                return false;
            System.out.println("Dados");
            message = "DADOS";
            out.writeObject(message);
            out.flush();

            String response = (String) in.readObject();
            String[] dados = response.split(" ");

            if (response.contains("Erro"))
                return false;

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
                    aux = "pass " + password;

            if (aux == null)
                return false;

            message = "EDICAO " + aux;

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

    public String submeteCodigo(String code) {
        String message = "CODIGO " + code;
        try {
            out.writeObject(message);
            out.flush();

            String response = (String) in.readObject();
            System.out.println(response);

            return response;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ocorreu a excepcao {" + e + "} ao nível do socket TCP de leitura do cliente!");
            e.printStackTrace();
        }
        return null;
    }

    public List<Presencas> consultarPresencas() { //Admin
        try {
            String message = PRESENCAS + " sem_filtro";
            out.writeObject(message);
            out.flush();

            String response = (String) in.readObject();
            System.out.println(response);

            // Processar a string e criar uma lista de eventos
            return processarStringPresencas(response);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ocorreu a excepção {" + e + "} ao nível do socket TCP de leitura do cliente!");
            e.printStackTrace();
            return new ArrayList<>(); // ou lançar uma exceção, dependendo dos requisitos
        }
    }
    private List<Presencas> processarStringPresencas(String eventosString) { //Admin
        List<Presencas> presencas = new ArrayList<>();
        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile("ID: (.*?) Codigo: (.*?) idEvento: (.*?) CC: (.*?) Hora de inicio: (.*?) Hora de fim: (.*?)\n");
        matcher = pattern.matcher(eventosString);
        while (matcher.find()) {
            String id = matcher.group(1);
            String codigo = matcher.group(2);
            String idEvento = matcher.group(3);
            String CC = matcher.group(4);
            String horaInicio = matcher.group(5);
            String horaFim = matcher.group(6);
            // Adicionar o evento à lista
            presencas.add(new Presencas(id, codigo, idEvento, CC, horaInicio, horaFim));
        }

        return presencas;
    }




    public List<Evento> consultarEventos() { //Admin e User (Presencas)
        try {
            String message;
            if (admin)
                message = EVENTO + " " + CONSULTA + " campo sem_filtro";
            else
                message = CONSULTA + " sem_filtro"; //Para o user é para pesquisar eventos

            out.writeObject(message);
            out.flush();

            String response = (String) in.readObject();
            System.out.println(response);

            // Processar a string e criar uma lista de eventos
            return processarStringEventos(response);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ocorreu a excepção {" + e + "} ao nível do socket TCP de leitura do cliente!");
            e.printStackTrace();
            return new ArrayList<>(); // ou lançar uma exceção, dependendo dos requisitos
        }
    }

    private List<Evento> processarStringEventos(String eventosString) { //Admin e User (Presencas)
        List<Evento> eventos = new ArrayList<>();
        Pattern pattern;
        Matcher matcher;

        if (admin) {
            pattern = Pattern.compile("Nome: (.*?) Local: (.*?) Data: (.*?) Hora de inicio: (.*?) Hora de fim: (.*?) Codigo: (.*?) Validade: (.*?)\n");
            matcher = pattern.matcher(eventosString);
            while (matcher.find()) {
                String nome = matcher.group(1);
                String local = matcher.group(2);
                String data = matcher.group(3);
                String horaInicio = matcher.group(4);
                String horaFim = matcher.group(5);
                String codigo = matcher.group(6);
                String validade = matcher.group(7);
                // Adicionar o evento à lista
                eventos.add(new Evento(nome, local, data, horaInicio, horaFim, codigo, validade));
            }
        } else {
            pattern = Pattern.compile("Nome: (.*?) Local: (.*?) Data: (.*?) Hora de inicio: (.*?) Hora de fim: (.*?)\n");
            matcher = pattern.matcher(eventosString);
            while (matcher.find()) {
                String nome = matcher.group(1);
                String local = matcher.group(2);
                String data = matcher.group(3);
                String horaInicio = matcher.group(4);
                String horaFim = matcher.group(5);
                // Adicionar o evento à lista
                eventos.add(new Evento(nome, local, data, horaInicio, horaFim));
            }
        }

        return eventos;
    }

    public boolean criarEvento(String nome, String local, String dataInicioFormatada, String dataFimFormatada, String horaInicio, String horaFim) {
        String message = "EVENTO "+nome+" "+dataInicioFormatada+" "+dataFimFormatada+" "+local+" "+horaInicio+" "+horaFim;
        try {
            out.writeObject(message);
            out.flush();

            String response = (String) in.readObject();
            System.out.println(response);

            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ocorreu a excepção {" + e + "} ao nível do socket TCP de leitura do cliente!");
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminaEvento(String nome) {
        String message = "EVENTO APAGAR "+nome;
        try {
            out.writeObject(message);
            out.flush();

            String response = (String) in.readObject();
            System.out.println(response);

            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ocorreu a excepção {" + e + "} ao nível do socket TCP de leitura do cliente!");
            e.printStackTrace();
        }
        return false;
    }

    public boolean editarEvento(String nomeCampoAlterado, String novaAlteracao, String nome) {
        String message = "EVENTO EDICAO "+nomeCampoAlterado+" "+novaAlteracao+" "+nome;
        try {

            out.writeObject(message);
            out.flush();

            String response = (String) in.readObject();
            System.out.println(response);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ocorreu a excepção {" + e + "} ao nível do socket TCP de leitura do cliente!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean inserirPresencas(String nome, String email) {
        String message = "PRESENCAS "+nome+" "+email;
        try {
            out.writeObject(message);
            out.flush();

            String response = (String) in.readObject();
            System.out.println(response);

            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ocorreu a excepção {" + e + "} ao nível do socket TCP de leitura do cliente!");
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminaPresenca(String id) {
        String message = "PRESENCASUI APAGAR "+id;
        try {
            out.writeObject(message);
            out.flush();

            String response = (String) in.readObject();
            System.out.println(response);

            if (response.contains("Erro"))
                return false;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ocorreu a excepção {" + e + "} ao nível do socket TCP de leitura do cliente!");
            e.printStackTrace();
        }
        return true;
    }

    public String gerarCodigo(String nomeEvento, String tempo) {
        String message = "GERAR "+nomeEvento+" "+tempo;
        try {
            out.writeObject(message);
            out.flush();

            String response = (String) in.readObject();
            System.out.println(response);

            return response;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ocorreu a excepção {" + e + "} ao nível do socket TCP de leitura do cliente!");
            e.printStackTrace();
        }
        return null;
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
