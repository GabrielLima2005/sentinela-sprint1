package sentinela;

/** Superclasse de tudo que monitora a instalação: sensores e drone. */
public abstract class DispositivoMonitoramento {

    private final String codigoIdentificador;
    private final String localizacao;
    private boolean emOperacao;

    protected DispositivoMonitoramento(String codigoIdentificador, String localizacao) {
        if (codigoIdentificador == null || codigoIdentificador.isBlank()) {
            throw new IllegalArgumentException("Código identificador é obrigatório.");
        }
        if (localizacao == null || localizacao.isBlank()) {
            throw new IllegalArgumentException("Localização é obrigatória.");
        }
        this.codigoIdentificador = codigoIdentificador;
        this.localizacao = localizacao;
        this.emOperacao = true;
    }

    public String getCodigoIdentificador() { return codigoIdentificador; }

    public String getLocalizacao() { return localizacao; }

    public boolean isEmOperacao() { return emOperacao; }

    public void setEmOperacao(boolean emOperacao) { this.emOperacao = emOperacao; }

    public abstract String descreverStatus();

    @Override
    public String toString() {
        return codigoIdentificador + " (" + localizacao + ")";
    }
}
