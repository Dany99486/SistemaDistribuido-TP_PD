package pt.isec.PD.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private int TIMEOUT = 1000;
    private final String[] args;
    private File localDirectory;
    private String BDFileName = "serverdatabase.db";
    private String BDCanonicalFilePath = null;
    private String RMIService;
    private ObjectOutputStream output;
    private ObjectInputStream input;
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
        /*
        try {
            show = checkBDFile();
            if (show != null)
                return null;
            //show = createBD();
        } catch (IOException ignored) {}*/

        RMIService = args[2];

        show = serverTCPConnection();

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

    private String createBD() {
        String show;

        //A realizar
        return show = null;
    }

    private String serverTCPConnection() {
        String show;

        show = socketServer();

        //nClients = getClients(args[0], clients); //A realizar

        return show;
    }

    private String socketServer() {
        String show = null;

        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]))) {
            try (Socket socket = serverSocket.accept()) {
                socket.setSoTimeout(TIMEOUT * 10);
                //A realizar
            } catch (SocketTimeoutException e) {
                show = "Timeout " + e;
            } catch (IOException e) {
                show += "Problema de I/O " + e;
            }
        } catch (NumberFormatException e) {
            show += "O porto de escuta deve ser um inteiro positivo:\n\t" + e;
        } catch (SocketException e) {
            show += "Ocorreu uma excepcao ao nivel do socket UDP:\n\t" + e;
        } catch (IOException e) {
            show += "Ocorreu a excepcao de E/S: \n\t" + e;
        }
        return show;
    }

    private int getClients(String arg, List<Socket> clients) {
        return 0;
    }
}
