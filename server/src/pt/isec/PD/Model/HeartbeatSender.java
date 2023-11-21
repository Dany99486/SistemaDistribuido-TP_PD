package pt.isec.PD.Model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HeartbeatSender extends Thread {
    private static final String multicastAddress = "230.44.44.44";
    private static final int port = 4444;
    private String nameRMI;
    private int registryPort;

    private static int databaseVersion;

    public HeartbeatSender(String nameRMI , int registryPort, int databaseVersion) {
        this.nameRMI = nameRMI;
        this.registryPort = registryPort;
        setDatabaseVersion(databaseVersion);
    }
    public static void setDatabaseVersion(int databaseVersion) {
        HeartbeatSender.databaseVersion = databaseVersion;
    }

    public void run() {
        while (true) {
            sendHeartbeat(nameRMI, registryPort, databaseVersion);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void sendHeartbeat(String nameRMI , int registryPort, int databaseVersion) {
        try {
            InetAddress group = InetAddress.getByName(multicastAddress);
            DatagramSocket socket = new DatagramSocket();

                // Envie o heartbeat
                Heartbeat heartbeat = new Heartbeat(nameRMI, registryPort, databaseVersion);
                try (ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                     ObjectOutputStream out = new ObjectOutputStream(buffer)) {
                    out.writeObject(heartbeat);

                    DatagramPacket dgram = new DatagramPacket(buffer.toByteArray(), buffer.size(), group, port);
                    socket.send(dgram);
                } catch (IOException e) {
                    e.printStackTrace();
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public static void main(String[] args) {
        //String username = "name";
        int registryPort = Integer.parseInt(args[3]);
        String nameRMI = args[2];
        int databaseVersion = 1;
        // Inicialize e comece a thread de envio de heartbeat
        HeartbeatSender heartbeatSender = new HeartbeatSender(nameRMI, registryPort, databaseVersion);
        heartbeatSender.start();
    }*/
}