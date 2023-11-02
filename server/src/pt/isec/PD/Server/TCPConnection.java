package pt.isec.PD.Server;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class TCPConnection extends Thread {
    private final String AUTENTICAR = "AUTENTICAR";
    private final String REGISTAR = "REGISTAR";
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
        try (/*PrintWriter out = new PrintWriter(toClientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(toClientSocket.getInputStream()))*/
                ObjectOutputStream out = new ObjectOutputStream(toClientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(toClientSocket.getInputStream())
        ) {
            toClientSocket.setSoTimeout(TIMEOUT);
            //recebido = in.readLine();
            recebido = (String) in.readObject();

            if (recebido == null)
                return; //EOF

            String[] aux = recebido.trim().split(" ");

            //Print temporaria
            System.out.println("[" + "+Thread." + currentThread().getName() + "]"
                    + " Recebido do utilizador \"" + aux[1] + "\" de " +
                    toClientSocket.getInetAddress().getHostAddress() + ":" +
                    toClientSocket.getPort());

            if (aux[0].equalsIgnoreCase(AUTENTICAR)) {
                boolean verificaNaBDCliente = new BD().checkClientIfExists(aux[1], aux[2], args, BDFileName);

                if (verificaNaBDCliente) {
                    clients.add(toClientSocket);
                    nClients++;
                    envia = "Logado com sucesso";
                }
                else
                    envia = "Erro: Não foi possivel logar!";
                //out.println(envia);
            }
            if (aux[0].equalsIgnoreCase(REGISTAR)) {
                int registo = new BD().registClient(aux[1], aux[2], args, BDFileName);
                if (registo == 1)
                    envia = "Registado com sucesso";
                else if (registo == 0)
                    envia = "Erro: Não foi registado, o utilizador já existe!";
                else if (registo == -1)
                    envia = "Erro: Não foi possivel registar, erro de conexão";
                else if (registo == -2)
                    envia = "Erro: Não foi possivel registar, erro interno";
                else
                    envia = "Erro: Não foi possivel registar";
            }
            out.writeObject(envia);
            out.flush();
        } catch (IOException e) {
            msgShow = "\nErro na comunicação com o cliente atual: " + e;
        } catch (ClassNotFoundException e) {
            msgShow = "\nErro, dados passados não são reconhecidos " + e;
        }
    }

    public List<Socket> getClients() {
        return clients;
    }

    public int getnClients() {
        return nClients;
    }
}
