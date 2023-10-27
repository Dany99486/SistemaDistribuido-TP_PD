package pt.isec.PD.Server;

import java.io.File;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final String[] args;
    private File localDirectory;
    private String requestedFileName;
    private String requestedCanonicalFilePath=null;
    private OutputStream out;
    private List<Socket> clients = new ArrayList<>();

    public Server(String[] a) {
        this.args = a;
    }

    public String server() {
        String show = null;

        if (args.length != 4) {
            show = "Sintaxe: <Porto conexão do cliente> <Diretoria> <NomeRMI> <Porto registry>";
            return show;
        }

        show = CheckBDFolder();

        if (show == null)
            return null;

        /*
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]))) {
            while (true) {
                try(Socket socket = serverSocket.accept();
                    BufferedReader bin = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    socket.setSoTimeout(50000);
                    out = socket.getOutputStream();

                    requestedFileName = bin.readLine();

                    if (requestedFileName == null)
                        continue;

                    System.out.println("Recebido pedido para \"" + requestedFileName + "\" de " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
                    requestedCanonicalFilePath = new File(localDirectory + File.separator + requestedFileName).getCanonicalPath();

                    if (!requestedCanonicalFilePath.startsWith(localDirectory.getCanonicalPath() + File.separator)) {
                        System.err.println("Não é permitido aceder ao ficheiro " + requestedCanonicalFilePath + "!");
                        System.err.println("A diretoria de base não corresponde a " + localDirectory.getCanonicalPath() + "!");
                    }
                    try (InputStream requestedFileInputStream = new FileInputStream(requestedCanonicalFilePath)) {
                        System.out.println("Ficheiro " + requestedCanonicalFilePath + " aberto para leitura.");

                        int totalBytes = 0, nChunks = 0, nbytes;
                        byte[] fileChunk = new byte[1000];
                        do {
                            nbytes = requestedFileInputStream.read(fileChunk);

                            if (nbytes != -1) {
                                out.write(fileChunk, 0, nbytes);
                                out.flush();
                                totalBytes+=nbytes;
                                nChunks++;
                            }
                        } while (nbytes > 0);
                        System.out.println("Transferência concluida em " + nChunks + " blocos com um total de " + nbytes + " bytes\r\n");
                    } catch (SocketTimeoutException e) { //Subclasse de IOException
                        System.err.println("O cliente atual não enviou qualquer nome de ficheiro (timeout)");
                    } catch (FileNotFoundException e) {   //Subclasse de IOException
                        System.err.println("Ocorreu a exceção {" + e + "} ao tentar abrir o ficheiro " + requestedCanonicalFilePath + "!");
                    } catch (IOException e) {
                        System.err.println("Problema de I/O no atendimento ao cliente atual: " + e);
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("O porto de escuta deve ser um inteiro positivo:\n\t" + e);
        } catch (SocketException e) {
            System.err.println("Ocorreu uma exceção ao nível do socket TCP:\n\t" + e);
        } catch (IOException e) {
            System.err.println("Ocorreu a exceção:\n\t" + e);
        } */
        return show;
    }

    private String CheckBDFolder() {
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
}
