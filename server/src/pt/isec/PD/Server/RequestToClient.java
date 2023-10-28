package pt.isec.PD.Server;

import java.io.Serial;
import java.io.Serializable;

public class RequestToClient implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected int id;
    protected int nClients;
    protected long nIntervals;

    public RequestToClient(int id, int nClients, long nIntervals) {
        this.id = id;
        this.nClients = nClients;
        this.nIntervals = nIntervals;
    }

    public int getId() {
        return id;
    }

    public long getnIntervals() {
        return nIntervals;
    }

    public int getnClients() {
        return nClients;
    }
}
