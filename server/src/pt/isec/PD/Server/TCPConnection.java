package pt.isec.PD.Server;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class TCPConnection extends Thread {
    private final String AUTENTICAR = "AUTENTICAR";
    private final String REGISTAR = "REGISTAR";
    private final String EDICAO = "EDICAO";
    private int TIMEOUT;
    private Socket toClientSocket;
    private String[] args;
    private String BDFileName;
    private List<Socket> clients;
    private int nClients;
    private String recebido, envia;
    private String msgShow;
    protected String cc=" ";
    private String role;
    private BD bd = new BD();

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
            do {
            recebido = (String) in.readObject();
            if (recebido == null)
                return; //EOF

            String[] aux = recebido.trim().split(" ");



                if (aux[0].equalsIgnoreCase(AUTENTICAR)) {
                    boolean verificaNaBDCliente = bd.checkClientIfExists(aux[1], aux[2], args, BDFileName);
                    cc= bd.getCC();
                    role=bd.getRole();
                    System.out.println("role:"+role);
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
                    int registo = bd.registClient(aux[1], aux[2],aux[3],aux[4], args, BDFileName);
                    if (registo == 1) {
                        cc = aux[3];
                        role="user";

                        envia = "Registado com sucesso";
                    }else if (registo == 0)
                        envia = "Erro: Não foi registado, o utilizador já existe!";
                    else if (registo == -1)
                        envia = "Erro: Não foi possivel registar, erro de conexão";
                    else if (registo == -2)
                        envia = "Erro: Não foi possivel registar, erro interno";
                    else
                        envia = "Erro: Não foi possivel registar";
                }
                if (aux[0].equalsIgnoreCase(EDICAO)) {
                    int registo = bd.EDITClient(aux[1], aux[2],cc, args, BDFileName);
                    if (registo == 1)
                        envia = "Editado com sucesso";
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
            }while (true);

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
