package pt.isec.PD.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private int TIMEOUT = 10000;
    private final String[] args;
    private File localDirectory;
    private String BDFileName = "serverdatabase.db";
    private String BDCanonicalFilePath = null;
    private String RMIService;
    private List<Socket> clients;
    int nClients = 0;
    private String hertbeat;

    public Server(String[] a) {
        this.args = a;
        this.clients = new ArrayList<>();
    }

    public String server() {
        String show;

        if (args.length != 4) {
            show = "Sintaxe: <Porto conexão do cliente> <Diretoria> <NomeRMI> <Porto registry>";
            return show;
        }

        show = checkBDFolder();
        if (show != null)
            return null;

        show = new BD().createBDIfNotExists(args, BDFileName); //TODO: Criar base de dados se nao existir

        RMIService = args[2];

        //TODO: Conexão com clientes via TCP

        ServerTCPConnectionSocket socketClient = new ServerTCPConnectionSocket(clients, nClients, TIMEOUT, args, BDFileName);


        show = socketClient.serverTCPConnection();

        //TODO: Fechar sockets dos clientes
        clients = socketClient.getClients();
        nClients = socketClient.getnClients();

        for (Socket s : clients) {
            try {
                s.close();
            } catch (IOException e) {
                show = "\nErro ao fechar sockets dos clientes.";
            } finally {
                clients.clear();
            }
        }
        return show;
    }

    private String checkBDFolder() {
        String show = null;

        localDirectory = new File(args[1].trim());

        if (!localDirectory.exists()) {
            show = "A diretoria " + localDirectory + " não existe";
            return show;
        }

        if (!localDirectory.isDirectory()) {
            show = "O caminho " + localDirectory + " não se refere a uma directoria!";
            return show;
        }

        if (!localDirectory.canRead()) {
            show = "Sem permissões de leitura na directoria " + localDirectory + "!";
            return show;
        }

        return show;
    }

    private String checkBDFile() throws IOException {
        String show = null;
        BDCanonicalFilePath = new File(localDirectory + File.separator + BDFileName).getCanonicalPath();

        if (!BDCanonicalFilePath.startsWith(localDirectory.getCanonicalPath() + File.separator)) {
            show = "Não é permitido aceder ao ficheiro " + BDCanonicalFilePath + "!";
            show += "A diretoria de base não corresponde a " + localDirectory.getCanonicalPath() + "!";
        }
        return show;
    }
}
