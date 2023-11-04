package pt.isec.PD.Server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class Heartbeat implements Serializable {
    private static final long serialVersionUID = 1L;
    private String registryName;
    private int registryPort;
    private int databaseVersion;

    public Heartbeat(String registryName, int registryPort, int databaseVersion) {
        this.registryName = registryName;
        this.registryPort = registryPort;
        this.databaseVersion = databaseVersion;
    }

    public String getRegistryName() {
        return registryName;
    }

    public int getRegistryPort() {
        return registryPort;
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }
}

public class HeartbeatSender extends Thread {
    private final String multicastAddress = "230.44.44.44";
    private final int port = 4444;
    private String username; // Adicione o campo de nome de usuário
    private int registryPort; // Adicione o campo de porta de registro
    private int databaseVersion; // Adicione o campo de versão do banco de dados

    public HeartbeatSender(String username, int registryPort, int databaseVersion) {
        this.username = username;
        this.registryPort = registryPort;
        this.databaseVersion = databaseVersion;
    }

    public void run() {
        try {
            InetAddress group = InetAddress.getByName(multicastAddress);
            DatagramSocket socket = new DatagramSocket();

            while (true) {
                // Envie o heartbeat
                Heartbeat heartbeat = new Heartbeat(username, registryPort, databaseVersion);
                try (ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                     ObjectOutputStream out = new ObjectOutputStream(buffer)) {
                    out.writeObject(heartbeat);

                    DatagramPacket dgram = new DatagramPacket(buffer.toByteArray(), buffer.size(), group, port);
                    socket.send(dgram);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Aguarde um intervalo de 10 segundos antes de enviar o próximo heartbeat
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String username = "name";
        int registryPort = Integer.parseInt(args[4]);
        int databaseVersion = 1;
        // Inicialize e comece a thread de envio de heartbeat
        HeartbeatSender heartbeatSender = new HeartbeatSender(username, registryPort, databaseVersion);
        heartbeatSender.start();
    }
}