package pt.isec.PD.Server;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class TCPConnection extends Thread {
    private final String AUTENTICAR = "AUTENTICAR";
    private final String REGISTAR = "REGISTAR";
    private final String EDICAO = "EDICAO";
    private final String APAGAR = "APAGAR";
    private final String CONSULTA = "CONSULTA";
    private final String EVENTO = "EVENTO";
    private final String ADMIN = "admin";
    private final String USER = "user";
    private int TIMEOUT;
    private Socket toClientSocket;
    private String[] args;
    private String BDFileName;
    private List<Socket> clients;
    private int nClients;
    private String recebido, envia;
    private String msgShow;
    protected String cc = null;
    private String role;
    private BD bd = new BD();
    private Evento evento = new Evento();

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
                    cc = bd.getCC();
                    role = bd.getRole();
                    //System.out.println("role: " + role);
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
                        role = "user";
                    }
                    defaultRegistoReturn(registo);
                }
                if (aux[0].equalsIgnoreCase(EDICAO)) {
                    int registo = bd.editClient(aux[1], aux[2],cc, args, BDFileName);
                    defaultRegistoReturn(registo);
                }
                if (role.equalsIgnoreCase(ADMIN)) {
                    if (aux[0].equalsIgnoreCase(EVENTO)) {
                        if (aux.length != 4)
                            defaultRegistoReturn(-3);
                        else {
                            int hi = 0, hf = 0;
                            boolean atualiza = true;
                            try {
                                hi = Integer.parseInt(aux[3]);
                                hf = Integer.parseInt(aux[4]);
                                if (hi < 0 || hf < 0) {
                                    atualiza = false;
                                    throw new Exception("Valores negativos");
                                }
                            } catch (Exception e) {
                                msgShow = "\nErro de conversão de valores";
                            }
                            if (atualiza) {
                                int registo = evento.criaEvento(aux[1], aux[2], hi, hf, args, BDFileName);
                                defaultRegistoReturn(registo);
                            } else
                                defaultRegistoReturn(-3);
                        }
                    }
                    if (aux[0].equalsIgnoreCase(EVENTO) && aux[1].equalsIgnoreCase(EDICAO)) {
                        if (aux.length != 5)
                            defaultRegistoReturn(-3);
                        else {
                            boolean atualiza = true;
                            try {
                                int x = Integer.parseInt(aux[3]);
                                if (x < 0) {
                                    atualiza = false;
                                    throw new Exception("Valores negativos");
                                }
                            } catch (Exception e) {/*Não precisa dizer nada*/}
                            if (atualiza) {
                                //Evento - coluna, alteracao, nome do evento a atualizar
                                int registo = evento.editaEvento(aux[2], aux[3], aux[4], args, BDFileName);
                                defaultRegistoReturn(registo);
                            } else
                                defaultRegistoReturn(-3);
                        }
                    }
                    if (aux[0].equalsIgnoreCase(EVENTO) && aux[1].equalsIgnoreCase(APAGAR)) {
                        if (aux.length != 3)
                            defaultRegistoReturn(-3);
                        else {
                            int registo = evento.eliminaEvento(aux[2], args, BDFileName);
                            defaultRegistoReturn(registo);
                        }
                    }
                    if (aux[0].equalsIgnoreCase(EVENTO) && aux[1].equalsIgnoreCase(CONSULTA)) {
                        if (aux.length != 3)
                            defaultRegistoReturn(-3);
                        else
                            envia = evento.consultaEvento(aux[2], args, BDFileName);
                    }
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

    private void defaultRegistoReturn(int registo) {
        if (registo == 1)
            envia = "Editado com sucesso";
        else if (registo == 0)
            envia = "Erro: Não foi registado, o utilizador já existe!";
        else if (registo == -1)
            envia = "Erro: Não foi possivel registar, erro de conexão";
        else if (registo == -2)
            envia = "Erro: Não foi possivel registar, erro interno";
        else if (registo == -3)
            envia = "Erro: Infomação inválida";
        else
            envia = "Erro: Não foi possivel registar";
    }
    public List<Socket> getClients() {
        return clients;
    }

    public int getnClients() {
        return nClients;
    }
}
