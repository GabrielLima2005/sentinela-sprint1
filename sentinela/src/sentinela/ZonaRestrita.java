package sentinela;

/**
 * Zona fisicamente delimitada da instalação nuclear (ex.: casa do reator).
 * Nenhuma zona nasce sem identificação, descrição e limite válidos.
 */
public class ZonaRestrita {

    private static final double LEITURA_MAXIMA_VALIDA = 1_000_000.0; // µSv/h
    private static final double FRACAO_LIMITE_ATENCAO = 0.5;

    private final String identificador;
    private final String descricao;
    private final double limiteRadiacao;
    private double nivelRadiacaoAtual;

    public ZonaRestrita(String identificador, String descricao, double limiteRadiacao) {
        if (identificador == null || identificador.isBlank()) {
            throw new IllegalArgumentException("Identificador da zona é obrigatório.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição da zona é obrigatória.");
        }
        if (Double.isNaN(limiteRadiacao) || limiteRadiacao <= 0) {
            throw new IllegalArgumentException("Limite de radiação deve ser positivo.");
        }
        this.identificador = identificador;
        this.descricao = descricao;
        this.limiteRadiacao = limiteRadiacao;
        this.nivelRadiacaoAtual = 0.0;
    }

    /** Comportamento de domínio: atualiza o nível atual da zona. */
    public void registrarLeituraRadiacao(double leitura) {
        setNivelRadiacaoAtual(leitura);
    }

    /** Regra de negócio: classifica o risco conforme as faixas de radiação. */
    public String classificarRisco() {
        if (nivelRadiacaoAtual >= limiteRadiacao) {
            return "CRITICO";
        }
        if (nivelRadiacaoAtual >= limiteRadiacao * FRACAO_LIMITE_ATENCAO) {
            return "ATENCAO";
        }
        return "NORMAL";
    }

    public String getIdentificador() { return identificador; }

    public String getDescricao() { return descricao; }

    public double getLimiteRadiacao() { return limiteRadiacao; }

    public double getNivelRadiacaoAtual() { return nivelRadiacaoAtual; }

    /** Setter apenas valida e atribui; em caso de erro, mantém o último valor válido. */
    private void setNivelRadiacaoAtual(double nivel) {
        if (Double.isNaN(nivel) || nivel < 0) {
            throw new IllegalArgumentException("Radiação negativa não existe: leitura rejeitada.");
        }
        if (nivel > LEITURA_MAXIMA_VALIDA) {
            throw new IllegalArgumentException("Leitura absurda (> 1.000.000 µSv/h): falha de sensor.");
        }
        this.nivelRadiacaoAtual = nivel;
    }

    @Override
    public String toString() {
        return identificador + " - " + descricao;
    }
}
