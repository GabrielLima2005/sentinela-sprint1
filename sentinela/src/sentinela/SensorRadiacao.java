package sentinela;

import java.util.Locale;

public class SensorRadiacao extends DispositivoMonitoramento {

    private final ZonaRestrita zonaMonitorada;
    private final double limiteSeguranca;
    private double leituraAtual;

    public SensorRadiacao(String codigoIdentificador, String localizacao, ZonaRestrita zonaMonitorada) {
        super(codigoIdentificador, localizacao);
        if (zonaMonitorada == null) {
            throw new IllegalArgumentException("Zona monitorada é obrigatória.");
        }
        this.zonaMonitorada = zonaMonitorada;
        this.limiteSeguranca = zonaMonitorada.getLimiteRadiacao();
    }

    /** A zona valida primeiro; se rejeitar, a leitura do sensor não é alterada. */
    public void registrarLeitura(double leitura) {
        zonaMonitorada.registrarLeituraRadiacao(leitura);
        this.leituraAtual = leitura;
    }

    public ZonaRestrita getZonaMonitorada() { return zonaMonitorada; }

    public double getLeituraAtual() { return leituraAtual; }

    public double getLimiteSeguranca() { return limiteSeguranca; }

    @Override
    public String descreverStatus() {
        return String.format(Locale.forLanguageTag("pt-BR"),
                "leitura %.2f µSv/h (limite %.2f) -> %s",
                leituraAtual, limiteSeguranca, zonaMonitorada.classificarRisco());
    }
}
