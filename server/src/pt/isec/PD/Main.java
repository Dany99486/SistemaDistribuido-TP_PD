package pt.isec.PD;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Main {
    public static void main(String[] args) {
        File localDirectory;
        String requestedFileName;
        String requestedCanonicalFilePath=null;
        OutputStream out;

        if (args.length != 4) {
            System.err.println("Sintaxe: <Porto conexão do cliente> <Diretoria> <NomeRMI> <Porto registry>");
            return;
        }

        localDirectory = new File(args[1].trim());

        if (!localDirectory.exists()) {
            System.err.println("A diretoria " + localDirectory + " não existe");
            return;
        }

        if (!localDirectory.isDirectory()) {
            System.err.println("O caminho " + localDirectory + " não se refere a uma directoria!");
            return;
        }

        if (!localDirectory.canRead()) {
            System.err.println("Sem permissões de leitura na directoria " + localDirectory + "!");
            return;
        }

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
        }
    }
}