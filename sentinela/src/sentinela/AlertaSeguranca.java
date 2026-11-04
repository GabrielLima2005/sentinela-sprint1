package sentinela;

public class AlertaSeguranca {

    private final DispositivoMonitoramento sensorOrigem;
    private final ZonaRestrita zonaAfetada;
    private final Severidade severidade;
    private String mensagem = "";

    public AlertaSeguranca(DispositivoMonitoramento sensorOrigem, ZonaRestrita zonaAfetada, Severidade severidade) {
        if (sensorOrigem == null || zonaAfetada == null || severidade == null) {
            throw new IllegalArgumentException("Origem, zona e severidade são obrigatórias.");
        }
        this.sensorOrigem = sensorOrigem;
        this.zonaAfetada = zonaAfetada;
        this.severidade = severidade;
    }

    public AlertaSeguranca comMensagem(String mensagem) {
        this.mensagem = mensagem;
        return this;
    }

    public DispositivoMonitoramento getSensorOrigem() { return sensorOrigem; }

    public ZonaRestrita getZonaAfetada() { return zonaAfetada; }

    public Severidade getSeveridade() { return severidade; }

    public String getMensagem() { return mensagem; }

    @Override
    public String toString() {
        return "[ALERTA " + severidade + "] " + mensagem
                + "\n    origem: " + sensorOrigem + " | zona: " + zonaAfetada;
    }
}
