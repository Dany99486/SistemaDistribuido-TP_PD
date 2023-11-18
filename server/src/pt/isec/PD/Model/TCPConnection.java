package pt.isec.PD.Model;

import pt.isec.PD.RMI.GetRemoteBDService;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class TCPConnection extends Thread {
    private final String DADOS = "DADOS";
    private final String AUTENTICAR = "AUTENTICAR";
    private final String LOGOUT = "LOGOUT";
    private final String REGISTAR = "REGISTAR";
    private final String CODIGO = "CODIGO";
    private final String EDICAO = "EDICAO";
    private final String APAGAR = "APAGAR";
    private final String EMAIL = "EMAIL";
    private final String CONSULTA = "CONSULTA";
    private final String EVENTO = "EVENTO";
    private final String PRESENCAS = "PRESENCAS";
    private final String GERAR = "GERAR";
    private final String CSV2 = "CSV2";
    private final String CSV = "CSV";
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
    private String cc;
    private String role;
    private BD bd;
    private Evento evento;
    private boolean open = true;
    private GetRemoteBDService fileService;

    public TCPConnection(GetRemoteBDService fileService, List<Socket> cs, int nc, Socket toClientSocket, int TIMEOUT, BD bd, Evento evento, String[] args, String BDFileName) {
        this.toClientSocket = toClientSocket;
        this.TIMEOUT = TIMEOUT;
        this.args = args;
        this.bd = bd;
        this.evento = evento;
        this.BDFileName= BDFileName;
        this.clients = cs;
        this.nClients = nc;
        this.fileService = fileService;
    }

    @Override
    public void run() {
        try (/*PrintWriter out = new PrintWriter(toClientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(toClientSocket.getInputStream()))*/
                ObjectOutputStream out = new ObjectOutputStream(toClientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(toClientSocket.getInputStream())
        ) {
            String[] queryArray = new String[1];
            do {
                recebido = (String) in.readObject();
                if (recebido == null) {
                    System.out.println("Recebido: [null]");
                    return; //EOF
                }

                String[] aux = recebido.trim().split(" ");

                if (aux[0].equalsIgnoreCase(AUTENTICAR)) {
                    //Antes de realizar o login
                    Verifica10s verifica10s = new Verifica10s(TIMEOUT);
                    verifica10s.start();

                    //Recebe o login
                    recebido = (String) in.readObject();
                    String[] aux2 = recebido.trim().split(" ");

                    if (verifica10s.isTimeout()) {
                        envia = "Erro: Timeout";
                        out.writeObject(envia);
                        out.flush();
                        toClientSocket.close();
                        verificaConectados();
                        open = false;
                        return;
                    }

                    //Realizar
                    boolean verificaNaBDCliente = bd.checkClientIfExists(aux2[0], aux2[1], args, BDFileName);
                    //System.out.println("role: " + role);

                    if (verificaNaBDCliente) {
                        cc = bd.getCC();
                        role = bd.getRole();

                        if (cc == null || role == null) {
                            envia = "Nao foi possivel logar";
                        } else {
                            clients.add(toClientSocket);
                            nClients++;
                            if (role.equalsIgnoreCase(ADMIN))
                                envia = "Admin bem-vindo";
                            else
                                envia = "Logado com sucesso";
                        }
                    } else {
                        envia = "Nao foi possivel logar";
                        out.writeObject(envia);
                        out.flush();
                    }
                    //out.println(envia);
                }
                if (aux[0].equalsIgnoreCase(REGISTAR)) {
                    int registo = bd.registClient(aux[1], aux[2],aux[3],aux[4], args, BDFileName,queryArray);
                    if (registo == 1) {
                        cc = aux[3];
                        role = "user";
                    }
                    System.out.println(Arrays.toString(queryArray));
                    notifyObservers(queryArray);
                    defaultRegistoReturn(registo);
                }
                if (aux[0].equalsIgnoreCase(EDICAO)) {
                    int registo = bd.editClient(aux[1], aux[2], cc, args, BDFileName,queryArray);

                    System.out.println(Arrays.toString(queryArray));
                    defaultRegistoReturn(registo);
                    notifyObservers(queryArray);
                }
                if (aux[0].equalsIgnoreCase(LOGOUT)) {
                    boolean registo = clients.remove(toClientSocket);
                    if (registo) {
                        nClients--;
                        envia = "Deixou a sessão";
                    } else
                        envia = "A sessão não foi terminada";
                }
                if (aux[0].equalsIgnoreCase(DADOS)) {
                    if (cc == null || role == null) {
                        envia = "Erro: Não está registado";
                        out.writeObject(envia);
                        out.flush();
                    } else {
                        envia = bd.devolveDados(cc, args, BDFileName);
                        if (envia == null)
                            envia = "Erro, sem dados obtidos";
                        System.out.println("envia: " + envia);
                        out.writeObject(envia);
                        out.flush();
                    }
                }

                if (role == null || cc == null) {
                    System.out.println("Role e cc error: [null]");
                    return; //Temos de fechar esta conexao do cliente
                }

                if (role.equalsIgnoreCase(ADMIN)) {

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
                                int registo = evento.editaEvento(aux[2], aux[3], aux[4], args, BDFileName,queryArray);
                                System.out.println(Arrays.toString(queryArray));
                                notifyObservers(queryArray);
                                defaultRegistoReturn(registo);
                            } else
                                defaultRegistoReturn(-3);
                        }
                    }
                    else if (aux[0].equalsIgnoreCase(EVENTO) && aux[1].equalsIgnoreCase(APAGAR)) {
                        if (aux.length != 3)
                            defaultRegistoReturn(-3);
                        else {
                            int registo = evento.eliminaEvento(aux[2], args, BDFileName,queryArray);
                            System.out.println(Arrays.toString(queryArray));
                            notifyObservers(queryArray);
                            defaultRegistoReturn(registo);
                        }
                    }
                    else if (aux[0].equalsIgnoreCase(EVENTO) && aux[1].equalsIgnoreCase(EMAIL) && aux.length == 3) {
                        envia = evento.consultaEventoEmail(aux[2], args, BDFileName); //
                    }
                    else if (aux[0].equalsIgnoreCase(EVENTO) && aux[1].equalsIgnoreCase(CONSULTA) && aux.length == 3) {
                        envia = evento.consultaEvento(aux[2], args, BDFileName);
                    }
                    else if (aux[0].equalsIgnoreCase(EVENTO) && aux[1].equalsIgnoreCase(CONSULTA) && aux.length == 4) {
                        envia = evento.consultaEventoFiltro(aux[2].toLowerCase(), aux[3], args, BDFileName);
                    }
                    else if (aux[0].equalsIgnoreCase(EVENTO)) {
                        if (aux.length != 7)
                            defaultRegistoReturn(-3);
                        else {
                            int registo = evento.criaEvento(aux[1], aux[2], aux[3], aux[4], aux[5], aux[6], args, BDFileName,queryArray);
                            System.out.println(Arrays.toString(queryArray));
                            notifyObservers(queryArray);
                            defaultRegistoReturn(registo);
                        }
                    }
                    if (aux[0].equalsIgnoreCase(PRESENCAS) && aux.length == 3) {
                        int registo = evento.inserePresenca(aux[1], aux[2], args, BDFileName,queryArray);
                        System.out.println(Arrays.toString(queryArray));
                        notifyObservers(queryArray);
                        defaultRegistoReturn(registo);
                    }
                    if (aux[0].equalsIgnoreCase(PRESENCAS) && aux[1].equalsIgnoreCase(APAGAR) && aux.length == 4) {
                        int registo = evento.eliminaPresenca(aux[2], aux[3], args, BDFileName,queryArray);
                        System.out.println(Arrays.toString(queryArray));
                        notifyObservers(queryArray);
                        defaultRegistoReturn(registo);
                    }
                    if (aux[0].equalsIgnoreCase(PRESENCAS) && aux.length == 2) {
                        envia = evento.consultaPresenca(aux[1], args, BDFileName);
                    }
                    if (aux[0].equalsIgnoreCase(GERAR)) {
                        if (aux.length != 3)
                            defaultRegistoReturn(-3);
                        else {
                            int x = 0;
                            boolean atualiza = true;
                            try {
                                x = Integer.parseInt(aux[2]);
                                if (x < 0) {
                                    throw new Exception("Valores negativos");
                                }
                            } catch (Exception e) {
                                atualiza = false;
                            }
                            if (atualiza) {
                                //Só precisa da validade, encontra depois um evento que existe com esta validade
                                int registo = evento.geraCodigo(aux[1], x, args, BDFileName, queryArray);
                                System.out.println(Arrays.toString(queryArray));
                                if (registo < 0)
                                    defaultRegistoReturn(registo);
                                else {
                                    notifyObservers(queryArray);
                                    envia = "Codigo de registo de presenças: " + registo;
                                }
                            } else
                                defaultRegistoReturn(-3);
                        }
                    }
                    if (aux[0].equalsIgnoreCase(EVENTO) && aux[1].equalsIgnoreCase(CSV2) && aux.length == 3) {
                        int registo = evento.geraCSV2(aux[2], args, BDFileName);
                        System.out.println("cria csv2");
                        //envia = evento.getCanonicalPathCSV();
                        enviaFicheiro(registo,"CSVfile.csv");
                    }
                    if (aux[0].equalsIgnoreCase(EVENTO) && aux[1].equalsIgnoreCase(CSV) && aux.length == 3) {
                        int registo = evento.geraCSV1(aux[2], args, BDFileName);
                        //envia = evento.getCanonicalPathCSV();
                        enviaFicheiro(registo,"CSV1file.csv");
                    }
                } else { //cliente
                    if (aux[0].equalsIgnoreCase(EVENTO) && aux[1].equalsIgnoreCase(CSV) && aux.length == 4) {
                        System.out.println("cria csv cliente");
                        int registo = evento.geraCSVClient(aux[2],aux[3], args, BDFileName);
                        //envia = evento.getCanonicalPathCSV();
                        enviaFicheiro(registo,"CSVClientfile.csv");
                    }
                    if (aux[0].equalsIgnoreCase(CONSULTA) && aux.length <= 2) {
                        envia = evento.consultaEventoCLienteFiltro(cc, aux[1], args, BDFileName);
                    }
                    if (aux[0].equalsIgnoreCase(CODIGO) && aux.length == 2) {
                        int x = 0;
                        try {
                            x = Integer.parseInt(aux[1]);
                        } catch (Exception e) {
                            defaultRegistoReturn(-3);
                        }
                        envia = evento.insereCodigo(cc, x, args, BDFileName,queryArray);
                        System.out.println("queryarray: " + Arrays.toString(queryArray));
                        notifyObservers(queryArray);
                    }
                }

                out.writeObject(envia);
                out.flush();

                verificaConectados();
            }while (open);

        } catch (IOException e) {
            msgShow = "\nErro na comunicação com o cliente atual: " + e;
        } catch (ClassNotFoundException e) {
            msgShow = "\nErro, dados passados não são reconhecidos " + e;
        }
    }

    private void enviaFicheiro(int registo,String fileName) {
        boolean enviaFile = false;
        if (registo == 0)
            enviaFile = new SendFile(toClientSocket).sendFile(fileName);
        if (enviaFile)
            envia = "Sucesso, ficheiro enviado";
        else
            envia = "Erro: Não foi possivel enviar o ficheiro";
    }

    private void defaultRegistoReturn(int registo) {
        if (registo == 1)
            envia = "Sucesso";
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

    private void verificaConectados() {
        for (Socket s : clients) {
            if (!s.isConnected()) {
                if (clients.remove(s)) {
                    nClients--;
                    System.out.println("Cliente desconectado");
                }
            }
        }
    }

    private void notifyObservers(String[] msg) {
        HeartbeatSender.sendHeartbeat(args[2], Integer.parseInt(args[3]), 12);
        fileService.notifyObservers(msg);
    }

    public List<Socket> getClients() {
        return clients;
    }

    public int getnClients() {
        return nClients;
    }
}
