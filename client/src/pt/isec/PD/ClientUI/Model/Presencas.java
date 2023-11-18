package pt.isec.PD.ClientUI.Model;

public class Presencas {
    private String codigo;
    private String idEvento;
    private String cartaoCidadao;
    private String horaInicio;
    private String horaFim;

    public Presencas(String codigo, String idEvento, String cartaoCidadao, String horaInicio, String horaFim) {
        this.codigo = codigo;
        this.idEvento = idEvento;
        this.cartaoCidadao = cartaoCidadao;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public String getCodigo() {
        return codigo;
    }
    public String getIdEvento() {
        return idEvento;
    }
    public String getCartaoCidadao() {
        return cartaoCidadao;
    }
    public String getHoraInicio() {
        return horaInicio;
    }
    public String getHoraFim() {
        return horaFim;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }
    public void setCartaoCidadao(String cartaoCidadao) {
        this.cartaoCidadao = cartaoCidadao;
    }
    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }
    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }
}
