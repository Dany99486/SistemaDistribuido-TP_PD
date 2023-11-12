package pt.isec.PD.UI;

import pt.isec.PD.Model.Server;

public class ServerUI {
    private final Server server;

    public ServerUI(Server s) {
        this.server = s;
    }

    public void ServerShow() {
        System.out.println(server.server());
    }
}
