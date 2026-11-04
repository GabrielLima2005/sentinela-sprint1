package sentinela;

public class SensorPerimetral extends DispositivoMonitoramento {

    private final ZonaRestrita setorVigiado;
    private boolean violacaoDetectada;

    public SensorPerimetral(String codigoIdentificador, String localizacao, ZonaRestrita setorVigiado) {
        super(codigoIdentificador, localizacao);
        if (setorVigiado == null) {
            throw new IllegalArgumentException("Setor vigiado é obrigatório.");
        }
        this.setorVigiado = setorVigiado;
    }

    public void registrarViolacaoCerca() { this.violacaoDetectada = true; }

    public void restaurarPerimetro() { this.violacaoDetectada = false; }

    public boolean isViolacaoDetectada() { return violacaoDetectada; }

    public ZonaRestrita getSetorVigiado() { return setorVigiado; }

    @Override
    public String descreverStatus() {
        return violacaoDetectada ? "VIOLAÇÃO DA CERCA DETECTADA" : "perímetro íntegro";
    }
}
