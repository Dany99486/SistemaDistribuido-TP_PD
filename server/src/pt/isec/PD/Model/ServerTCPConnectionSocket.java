package pt.isec.PD.Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerTCPConnectionSocket extends Thread {
    private int TIMEOUT;
    private String BDFileName;
    private String[] args;
    private List<Socket> clients;
    private int nClients;
    private BD bd;
    private Evento evento;

    public ServerTCPConnectionSocket(List<Socket> cs, int nc, int TIMEOUT, BD bd, Evento evento, String[] args, String BDFileName) {
        this.TIMEOUT = TIMEOUT;
        this.args = args;
        this.BDFileName = BDFileName;
        this.clients = cs;
        this.nClients = nc;
        this.bd = bd;
        this.evento = evento;
    }

    public String serverTCPConnection() {
        String show = null;

        try (ServerSocket socket = new ServerSocket(Integer.parseInt(args[0]))) {
            show += "TCP Server iniciado no porto " + socket.getLocalPort();
            while (true) {
                //TODO: Aceita clientes
                System.out.println("Thread");

                Socket toClientSocket = socket.accept();
                TCPConnection tcp = new TCPConnection(clients, nClients, toClientSocket, TIMEOUT, bd, evento, args, BDFileName);
                tcp.start();
                clients = tcp.getClients();
                nClients = tcp.getnClients();
            }
        } catch (NumberFormatException e) {
            show += "\nO porto deve ser um inteiro positivo";
        } catch (IOException e) {
            show += "\nOcorreu um erro ao nivel do socket de escuta: " + e;
        }
        System.out.println("saiuuuuuuuuuuuuuuuuuuuuuuuuuuu");

        return show;
    }

    public List<Socket> getClients() {
        return clients;
    }

    public int getnClients() {
        return nClients;
    }

    public BD getBd() {
        return bd;
    }

    public Evento getEvento() {
        return evento;
    }
}
