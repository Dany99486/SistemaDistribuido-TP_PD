package pt.isec.PD;

import java.io.*;

public class Main {
    public static final int MAX_SIZE = 4000;
    public static final int TIMEOUT = 10000;

    public static void main(String[] args) throws IOException {
        Client client = new Client(args);
        client.client();
    }
}