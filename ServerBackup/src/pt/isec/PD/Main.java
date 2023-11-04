package pt.isec.PD;

import java.io.*;
import pt.isec.PD.ServerBackup;

public class Main {

    public static void main(String[] args) throws IOException {
        ServerBackup serverBackup = new ServerBackup(args);
        serverBackup.serverBackup();
    }
}