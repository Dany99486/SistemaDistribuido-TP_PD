package pt.isec.PD.ClientUI.Model;

public class Evento {
    private String nome;
    private String local;
    private String data;
    private String horaInicio;
    private String horaFim;

    // Construtor da classe
    public Evento (String nome, String local, String data, String horaInicio, String horaFim) {
        this.nome = nome;
        this.local = local;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public String getNome() {
        return nome;
    }
    public String getLocal() {
        return local;
    }
    public String getData() {
        return data;
    }
    public String getHoraInicio() {
        return horaInicio;
    }
    public String getHoraFim() {
        return horaFim;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setLocal(String local) {
        this.local = local;
    }
    public void setData(String data) {
        this.data = data;
    }
    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }
    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

}
