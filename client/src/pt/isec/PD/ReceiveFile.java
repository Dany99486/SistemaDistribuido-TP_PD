package pt.isec.PD;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceiveFile {
    private final int MAX_SIZE = 4000;
    private final int TIMEOUT = 5;
    private final String canonicalPath;
    private final String[] args;

    public ReceiveFile(String c, String[] a) {
        this.canonicalPath = c;
        this.args = a;
    }

    public boolean receiveFile() {
        try (FileOutputStream localFileOutputStream = new FileOutputStream(canonicalPath);
             Socket socketToServer = new Socket(args[0], Integer.parseInt(args[1]));
             PrintWriter pout = new PrintWriter(socketToServer.getOutputStream(), true)) {
            int nbytes, nLoops = 0, totalBytes = 0;
            byte[] fileChunk = new byte[MAX_SIZE];

            socketToServer.setSoTimeout(TIMEOUT * 1000);
            pout.println(canonicalPath);

            while ((nbytes = socketToServer.getInputStream().read(fileChunk)) > 0) {
                nLoops++;
                totalBytes += nbytes;
                localFileOutputStream.write(fileChunk, 0, nbytes);
                localFileOutputStream.flush();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
