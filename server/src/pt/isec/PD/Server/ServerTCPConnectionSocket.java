package pt.isec.PD.Server;

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

    public ServerTCPConnectionSocket(List<Socket> cs, int nc, int TIMEOUT, String[] args, String BDFileName) {
        this.TIMEOUT = TIMEOUT;
        this.args = args;
        this.BDFileName = BDFileName;
        this.clients = cs;
        this.nClients = nc;
    }
    public String serverTCPConnection() {
        String show = null;

        try (ServerSocket socket = new ServerSocket(Integer.parseInt(args[0]))) {
            show += "TCP Server iniciado no porto " + socket.getLocalPort();
            while (true) {
                //TODO: Aceita clientes
                System.out.println("f");

                Socket toClientSocket = socket.accept();
                TCPConnection tcp = new TCPConnection(clients, nClients, toClientSocket, TIMEOUT, args, BDFileName);
                tcp.start();
                clients = tcp.getClients();
                nClients = tcp.getnClients();
            }
        } catch (NumberFormatException e) {
            show += "\nO porto deve ser um inteiro positivo";
        } catch (IOException e) {
            show += "\nOcorreu um erro ao nivel do socket de escuta: " + e;
        }
        return show;
    }

    public List<Socket> getClients() {
        return clients;
    }

    public int getnClients() {
        return nClients;
    }
}
