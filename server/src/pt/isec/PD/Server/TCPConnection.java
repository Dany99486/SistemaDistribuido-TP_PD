package pt.isec.PD.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class TCPConnection extends Thread {
    private int TIMEOUT;
    private Socket toClientSocket;
    private String[] args;
    private String BDFileName;
    private List<Socket> clients;
    private int nClients;
    private String recebido, envia;
    private String msgShow;

    public TCPConnection(List<Socket> cs, int nc, Socket toClientSocket, int TIMEOUT, String[] args, String BDFileName) {
        this.toClientSocket = toClientSocket;
        this.TIMEOUT = TIMEOUT;
        this.args = args;
        this.BDFileName= BDFileName;
        this.clients = cs;
        this.nClients = nc;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(toClientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(toClientSocket.getInputStream()))) {

            toClientSocket.setSoTimeout(TIMEOUT);
            recebido = in.readLine();

            if (recebido == null)
                return; //EOF

            //Print temporaria
            System.out.println("["+"+Thread."+currentThread().getName()+"]"
                    +" Recebido \"" + recebido + "\" de " +
                    toClientSocket.getInetAddress().getHostAddress() + ":" +
                    toClientSocket.getPort());

            String[] aux = recebido.trim().split(" ");
            boolean verificaNaBDCliente = new BD().checkClientIfExists(aux[0], aux[1], args, BDFileName);

            if (verificaNaBDCliente) {
                clients.add(toClientSocket);
                nClients++;
            }

            out.println(envia);
            out.flush();
        } catch (IOException e) {
            msgShow = "\nErro na comunicação com o cliente atual: " + e;
        }
    }

    public List<Socket> getClients() {
        return clients;
    }

    public int getnClients() {
        return nClients;
    }
}
