package pt.isec.PD.Model;

import java.io.IOException;

public class MainServerBackup {

    public static void main(String[] args) throws IOException {
        ServerBackup serverBackup = new ServerBackup(args);
        serverBackup.serverBackup();
    }
}