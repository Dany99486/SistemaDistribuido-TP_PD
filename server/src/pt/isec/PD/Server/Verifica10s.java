package pt.isec.PD.Server;

public class Verifica10s extends Thread {
    private final int TIMEOUT;
    private boolean timeout = false;
    private int tempo = 0;

    public Verifica10s(int TIMEOUT) {
        this.TIMEOUT = TIMEOUT;
    }

    @Override
    public void run() {
        // Verifique se o cliente está autenticado
        // Se não estiver, feche a ligação
        // Se estiver, reinicia o timeout
        while(tempo < TIMEOUT / 100) {
            try {
                Thread.sleep(1000); // 1 segundo
                tempo++;
                System.out.println("Tempo: " + tempo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        timeout = true;
    }

    public boolean isTimeout() {
        return timeout;
    }
}
