package pt.isec.PD.Server;

import java.io.Serial;
import java.io.Serializable;

public class RequestToClient implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected int id;
    protected int nClients;

    public RequestToClient(int id, int nClients) {
        this.id = id;
        this.nClients = nClients;
    }

    public int getId() {
        return id;
    }

    public int getnClients() {
        return nClients;
    }
}
