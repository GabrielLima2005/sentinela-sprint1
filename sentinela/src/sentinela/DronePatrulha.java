package sentinela;

import java.util.Locale;

public class DronePatrulha extends DispositivoMonitoramento {

    private static final double BATERIA_MINIMA_MISSAO = 20.0;

    private final String rotaPatrulha;
    private final CameraVisaoComputacional camera;
    private double bateriaPercentual;

    public DronePatrulha(String codigoIdentificador, String localizacao, String rotaPatrulha) {
        super(codigoIdentificador, localizacao);
        if (rotaPatrulha == null || rotaPatrulha.isBlank()) {
            throw new IllegalArgumentException("Rota de patrulha é obrigatória.");
        }
        this.rotaPatrulha = rotaPatrulha;
        this.camera = new CameraVisaoComputacional(); // TEM-UMA câmera (associação)
        this.bateriaPercentual = 100.0;
    }

    public void patrulhar() { consumirBateria(5.0); }

    public void despacharPara(ZonaRestrita zonaDestino) {
        consumirBateria(15.0);
    }

    public boolean estaProntoParaMissao() {
        return isEmOperacao() && bateriaPercentual >= BATERIA_MINIMA_MISSAO;
    }

    private void consumirBateria(double percentual) {
        bateriaPercentual = Math.max(0.0, bateriaPercentual - percentual);
    }

    public String getRotaPatrulha() { return rotaPatrulha; }

    public double getBateriaPercentual() { return bateriaPercentual; }

    public CameraVisaoComputacional getCamera() { return camera; }

    @Override
    public String descreverStatus() {
        String deteccao = camera.analisarImagem().map(o -> "câmera detectou: " + o).orElse("sem detecções");
        return String.format(Locale.forLanguageTag("pt-BR"),
                "rota %s, bateria %.0f%%, %s", rotaPatrulha, bateriaPercentual, deteccao);
    }
}
