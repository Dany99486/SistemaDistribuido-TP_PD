package pt.isec.PD.Server;

import java.io.*;
import java.net.Socket;

public class SendFile {
    private final int MAX_SIZE = 4000;
    private final String requestedCanonicalFilePath;
    private final Socket socket;

    public SendFile(Socket socket, String requestedCanonicFilePath) {
        this.socket = socket;
        this.requestedCanonicalFilePath = requestedCanonicFilePath;
    }

    public boolean sendFile() {
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

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
