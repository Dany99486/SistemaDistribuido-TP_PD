package pt.isec.PD;

import pt.isec.PD.Server.Server;
import pt.isec.PD.UI.ServerUI;

public class    Main {
    public static void main(String[] args) {
        Server server = new Server(args);
        ServerUI serverUI = new ServerUI(server);
        serverUI.ServerShow();
    }
}