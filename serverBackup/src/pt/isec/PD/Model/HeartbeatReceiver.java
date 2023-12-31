package pt.isec.PD.Model;

import java.io.*;
import java.net.*;

public class HeartbeatReceiver extends Thread {
    private final static String multicastAddress = "230.44.44.44";
    private static final int TIMEOUT_SECONDS = 30;
    private int count = 0;
    private String RMIRegistryName;
    private int RMIRegistryPort;
    private File localDirectory;
    private RmiService rmiService;

    private static int databaseVersion;
    public static int getDatabaseVersion() {
        return databaseVersion;
    }

    public static void setDatabaseVersion(int databaseVersion) {
        HeartbeatReceiver.databaseVersion = databaseVersion;
    }



    public HeartbeatReceiver(File directoryPath) {
        this.localDirectory = directoryPath;
    }

    public void run() {
        System.out.println("Heartbeat Receiver started");
        try {

            int port = 4444;

            InetAddress group = InetAddress.getByName(multicastAddress);
            MulticastSocket socket = new MulticastSocket(port);
            socket.joinGroup(group);

            socket.setSoTimeout(TIMEOUT_SECONDS * 1000);
            Heartbeat lastHeartbeat = null;
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                try {
                    socket.receive(packet);
                } catch (SocketTimeoutException e) {
                    System.out.println("No heartbeat received for " + TIMEOUT_SECONDS + " seconds. \nExiting...");
                    System.exit(0);
                }
                if (count!=0)
                    if (!BD.checkVersion(lastHeartbeat.getDatabaseVersion(),localDirectory.getPath()+File.separator+"serverdatabase.db")) {
                        rmiService.endService();
                    }
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
                try (ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                    Object object = objectInputStream.readObject();

                    if (object instanceof Heartbeat heartbeat) {
                        lastHeartbeat = heartbeat;
                        System.out.println("Received Heartbeat:");
                        System.out.println("Registry Name: " + heartbeat.getRegistryName());
                        RMIRegistryName = heartbeat.getRegistryName();
                        System.out.println("Registry Port: " + heartbeat.getRegistryPort());
                        RMIRegistryPort = heartbeat.getRegistryPort();
                        System.out.println("Database Version: " + heartbeat.getDatabaseVersion());
                        if (count == 0) {
                            setDatabaseVersion(heartbeat.getDatabaseVersion());
                            rmiService = new RmiService(RMIRegistryName, RMIRegistryPort, localDirectory.getPath()+File.separator+"serverdatabase.db", heartbeat.getDatabaseVersion());
                            rmiService.start();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}