package pt.isec.PD.Model;

import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class SendFile {
    private final int MAX_SIZE = 4000;
    private final Socket socket;
    private final String directory ="files";
    private String localFilePath;
    private String requestedCanonicalFilePath;

    public SendFile(Socket socket) {
        this.socket = socket;

    }

    public boolean sendFile(String fileName) {

        try {
            requestedCanonicalFilePath=new File(directory).getCanonicalPath()+ File.separator+fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Sending file: " + requestedCanonicalFilePath);
        try (InputStream requestedFileInputStream = new FileInputStream(requestedCanonicalFilePath)) {
            OutputStream out = socket.getOutputStream();
            int totalBytes = 0, nChunks = 0, nbytes;
            byte[] fileChunk = new byte[MAX_SIZE];
            do {
                nbytes = requestedFileInputStream.read(fileChunk);
                if (nbytes != -1) {
                    out.write(fileChunk, 0, nbytes);
                    out.flush();
                    totalBytes += nbytes;
                    nChunks++;
                }
            } while (nbytes > 0);

            String mensagemTermino = "FIM_TRANSFERENCIA";
            out.write(mensagemTermino.getBytes());
            out.flush();
            System.out.println("Transferencia concluida em " + nChunks + " blocos com um total de " + totalBytes + " bytes");
            sleep(10000);
            return true;
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
