package pt.isec.PD.Model;

import pt.isec.PD.RMI.GetRemoteBDObserver;
import pt.isec.PD.RMI.GetRemoteBDServiceInterface;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HeartbeatReceiver extends Thread {
    private final static String multicastAddress = "230.44.44.44";
    private static final int TIMEOUT_SECONDS = 30;
    private int count = 0;
    private String RMIRegistryName;
    private int RMIRegistryPort;

    public void run() {
        System.out.println("Heartbeat Receiver started");
        try {

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
                        RMIRegistryName = heartbeat.getRegistryName();
                        System.out.println("Registry Port: " + heartbeat.getRegistryPort());
                        RMIRegistryPort = heartbeat.getRegistryPort();
                        System.out.println("Database Version: " + heartbeat.getDatabaseVersion());
                        if (count == 0) {
                            launchRMIReceiver();
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
    private void launchRMIReceiver() {
        try {
            System.out.println("Launching RMI Receiver");
            String objectUrl = "rmi://localhost:"+RMIRegistryPort + "/"+RMIRegistryName;
            GetRemoteBDServiceInterface getRemoteFileService = (GetRemoteBDServiceInterface) Naming.lookup(objectUrl);

            System.setProperty("java.rmi.server.hostname", "localhost");


            System.out.println("RMI Receiver launched");
            GetRemoteBDObserver observer = new GetRemoteBDObserver();
            System.out.println("Observer registado no servidor RMI");

            getRemoteFileService.addObserver(observer);

            System.out.println("<Enter> para terminar...");
            System.out.println();
            System.in.read();
            System.out.println("Removing observer");

            getRemoteFileService.removeObserver(observer);
            UnicastRemoteObject.unexportObject(observer, true);

        } catch (MalformedURLException e) {
            System.out.println("Erro a obter o URL do objecto remoto");
        } catch (NotBoundException e) {
            System.out.println("Erro a obter a referencia para o objecto remoto");
        } catch (RemoteException e) {
            System.out.println("Erro de comunicacao com o objecto remoto");
        } catch (IOException e) {
            System.out.println("Erro de E/S");
        }

    }

}