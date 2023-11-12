package pt.isec.PD.Model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

public class HeartbeatReceiver extends Thread {
    private static final int TIMEOUT_SECONDS = 30;

    public void run() {
        System.out.println("Heartbeat Receiver started");
        try {
            String multicastAddress = "230.44.44.44";
            int port = 4444;

            InetAddress group = InetAddress.getByName(multicastAddress);
            MulticastSocket socket = new MulticastSocket(port);
            socket.joinGroup(group);

            socket.setSoTimeout(TIMEOUT_SECONDS * 1000);


            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                try {
                    socket.receive(packet);
                } catch (SocketTimeoutException e) {
                    System.out.println("No heartbeat received for " + TIMEOUT_SECONDS + " seconds. \nExiting...");
                    System.exit(0);
                }

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
                try (ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                    Object object = objectInputStream.readObject();

                    if (object instanceof Heartbeat) {
                        Heartbeat heartbeat = (Heartbeat) object;
                        System.out.println("Received Heartbeat:");
                        System.out.println("Registry Name: " + heartbeat.getRegistryName());
                        System.out.println("Registry Port: " + heartbeat.getRegistryPort());
                        System.out.println("Database Version: " + heartbeat.getDatabaseVersion());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HeartbeatReceiver heartbeatReceiver = new HeartbeatReceiver();
        heartbeatReceiver.start();
    }
}