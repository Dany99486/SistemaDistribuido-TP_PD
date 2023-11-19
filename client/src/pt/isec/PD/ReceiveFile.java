package pt.isec.PD;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ReceiveFile {

    private final String directory ="files";
    private String localFilePath;
    private final int MAX_SIZE = 4000;
    private final int TIMEOUT = 5;
    private final Socket socketToServer;

    public ReceiveFile(Socket socket) {
        socketToServer = socket;
    }

    public boolean receiveFile(String fileName) {

        InputStream in;
        File localDirectory=new File(directory);

        try{
            localFilePath = localDirectory.getCanonicalPath()+ File.separator+fileName;
        }catch(IOException e){
            System.out.println("Ocorreu a excepcao {" + e +"} ao obter o caminho canonico para o ficheiro local!");
            return false;
        }

        int nbytes, nLoops = 0, totalBytes = 0;
        byte[] fileChunk = new byte[MAX_SIZE];

        try(FileOutputStream localFileOutputStream = new FileOutputStream(localFilePath)) {

            socketToServer.setSoTimeout(TIMEOUT * 10000);
            in=socketToServer.getInputStream();

            boolean marcaDeTerminoRecebida = false;

            while (!marcaDeTerminoRecebida && (nbytes = in.read(fileChunk)) > 0) {
                nLoops++;
                totalBytes += nbytes;

                // Verifique se a mensagem de término foi recebida
                if (marcaDeTerminoRecebida(fileChunk, nbytes)) {
                    marcaDeTerminoRecebida = true;
                    nbytes -= "FIM_TRANSFERENCIA".getBytes().length;
                }

                if (nbytes > 0) {
                    localFileOutputStream.write(fileChunk, 0, nbytes);
                    System.out.println("Recebidos " + nbytes + " bytes no ciclo " + nLoops + " (total: " + totalBytes + " bytes)");
                }
            }
        } catch (SocketException e) {
            System.out.println("Ocorreu a excepcao {" + e + "} ao nível do socket TCP de leitura do cliente!");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Ocorreu a excepcao {" + e + "} ao nível do socket TCP de leitura do cliente!");
            throw new RuntimeException(e);
        }
        return true;

    }
    private boolean marcaDeTerminoRecebida(byte[] buffer, int length) {
        String mensagemTermino = "FIM_TRANSFERENCIA";
        byte[] mensagemBytes = mensagemTermino.getBytes();

        if (length < mensagemBytes.length) {
            return false;
        }

        for (int i = 0; i < mensagemBytes.length; i++) {
            if (buffer[length - mensagemBytes.length + i] != mensagemBytes[i]) {
                return false;
            }
        }

        return true;
    }
}
