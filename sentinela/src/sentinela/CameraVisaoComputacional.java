package sentinela;

import java.util.Optional;

/** Câmera simulada: o "objeto na cena" é injetado pelo cenário de teste. */
public class CameraVisaoComputacional {

    private String objetoNaCena;

    public void simularObjetoNaCena(String objeto) { this.objetoNaCena = objeto; }

    public void limparCena() { this.objetoNaCena = null; }

    public Optional<String> analisarImagem() {
        return Optional.ofNullable(objetoNaCena);
    }
}
