package pt.isec.PD.Server;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final String[] args;
    private File localDirectory;
    private String BDFileName = "serverdatabase";
    private String BDCanonicalFilePath = null;
    private String RMIService;
    private OutputStream out;
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

        try {
            show = checkBDFile();
            if (show != null)
                return null;
            show = createBD();
        } catch (IOException ignored) {}

        RMIService = args[2];



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

        nClients = getClients(args[0], clients);

        return show;
    }

    private int getClients(String arg, List<Socket> clients) {
        
    }
}
