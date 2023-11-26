package pt.isec.PD.Model;

import pt.isec.PD.RMI.GetRemoteBDService;

import java.io.*;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private int TIMEOUT = 1000;
    private final String[] args;
    private File localDirectory;
    private String BDFileName = "serverdatabase.db";
    private String BDCanonicalFilePath = null;
    private String RMIServiceName,RMIPort;
    private List<Socket> clients;
    private BD bd;
    private Evento evento;
    int nClients = 0;
    private String hertbeat;
    private SharedDatabaseLock lock;
    private GetRemoteBDService fileService;
    private static int versaoBD = 0;

    public Server(String[] a) {
        this.args = a;
        this.clients = new ArrayList<>();
        this.lock = new SharedDatabaseLock();
        this.bd = new BD(lock);
        this.evento = new Evento(lock);
    }
    public class SharedDatabaseLock {
        // Um objeto de bloqueio compartilhado para sincronização
    }

    public String server() {
        String show;

        if (args.length != 4) {
            show = "Sintaxe: <Porto conexão do cliente> <Diretoria> <NomeRMI> <Porto registry>";
            return show;
        }

        RMIServiceName = args[2];
        RMIPort = args[3];

        show = checkBDFolder();
        if (show != null)
            return show;

        show = bd.createBDIfNotExists(args, BDFileName); //TODO: Criar base de dados se não existir
        //System.out.println(show);

        int v = bd.pesquisaUltimaVersaoBD(args, BDFileName);
        if (v >= 0)
            versaoBD = v;

        HeartbeatSender heartbeatReceiver = new HeartbeatSender(RMIServiceName, Integer.parseInt(args[3]), versaoBD);
        heartbeatReceiver.start();

        //TODO: RMI
        System.setProperty("java.rmi.server.hostname", "localhost");
        launchRMI();

        //TODO: Conexão com clientes via TCP
        ServerTCPConnectionSocket socketClient = new ServerTCPConnectionSocket(versaoBD, fileService, clients, nClients, TIMEOUT, bd, evento, args, BDFileName);

        show = socketClient.serverTCPConnection();

        //TODO: Fechar sockets dos clientes
        clients = socketClient.getClients();
        nClients = socketClient.getnClients();
        bd = socketClient.getBd();
        evento = socketClient.getEvento();
        versaoBD = socketClient.getVersaoBD();

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

    private void launchRMI(){
        try {

            try {

                System.out.println("Tentativa de lancamento do registry no porto " +
                        Registry.REGISTRY_PORT + "...");

                LocateRegistry.createRegistry(Integer.parseInt(RMIPort));

                System.out.println("Registry lancado!");

            } catch(RemoteException e) {
                System.out.println("Registry provavelmente ja' em execucao!");
            }

            /*
             * Cria o servico.
             */
            fileService = new GetRemoteBDService(localDirectory);




            //Envia o objeto para o TCPConnection


            System.out.println("Servico GetRemoteFile criado e em execucao ("+fileService.getRef().remoteToString()+"...");

            /*
             * Regista o servico no rmiregistry local para que os clientes possam localizá-lo, ou seja,
             * obter a sua referencia remota (endereco IP, porto de escuta, etc.).
             */

            Naming.bind("rmi://localhost:"+RMIPort+"/" + RMIServiceName, fileService);

            System.out.println("Servico " + RMIServiceName + " registado no registry...");

            /*
             * Para terminar um servico RMI do tipo UnicastRemoteObject:
             *
             *  UnicastRemoteObject.unexportObject(fileService, true).
             */

        } catch(RemoteException e) {
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        } catch(Exception e) {
            System.out.println("Erro - " + e);
            System.exit(1);
        }

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
