package pt.isec.PD;

import javafx.application.Application;
import pt.isec.PD.ClientUI.MainJFX;
import pt.isec.PD.ClientUI.Model.Cliente;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        //TODO: Text UI not separated from logic
        Client client = new Client(args);
        client.client();

        //TODO: UI
        //Application.launch(MainJFX.class, args);
    }
}