package pt.isec.PD.ServerBackup;

import java.io.*;

public class MainServerBackup {

    public static void main(String[] args) throws IOException {
        ServerBackup serverBackup = new ServerBackup(args);
        serverBackup.serverBackup();
    }
}