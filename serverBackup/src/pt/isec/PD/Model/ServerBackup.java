package pt.isec.PD.Model;

import java.io.File;

public class ServerBackup {
    private String[] args;
    private String CC;

    public ServerBackup(String[] args) {
        this.args = args;
    }

    public void serverBackup() {
        String message = null;
        if (args.length != 1) {
            System.out.println("Sintaxe: java ServerBackup DirectoryToStoreData");
            return;
        }

        String directoryPath = args[0];
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null && files.length == 0) {
                System.out.println("A diretoria está vazia.");
            } else {
                System.out.println("A diretoria não está vazia. Ela contém " + files.length + " arquivo(s) ou subdiretório(s).");
                System.exit(-1);
            }
        } else {
            System.out.println("O diretório especificado não existe ou não é uma diretoria válida.");
        }

        HeartbeatReceiver heartbeatReceiver = new HeartbeatReceiver();
        heartbeatReceiver.start();
        //TODO: RMI
    }
}
