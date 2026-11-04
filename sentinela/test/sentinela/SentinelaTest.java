package sentinela;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class SentinelaTest {

    private ZonaRestrita novaZona() {
        return new ZonaRestrita("ZNR-01", "Casa do Reator", 3.0);
    }

    @Test
    void zonaNaoDeveSerNulaAposInstanciacao() {
        assertNotNull(novaZona());
    }

    @Test
    void leituraAltaAtualizaNivelEMudaClassificacao() {
        ZonaRestrita zona = novaZona();
        zona.registrarLeituraRadiacao(0.2);
        assertEquals("NORMAL", zona.classificarRisco());
        zona.registrarLeituraRadiacao(4.7);
        assertEquals(4.7, zona.getNivelRadiacaoAtual());
        assertEquals("CRITICO", zona.classificarRisco());
    }

    @Test
    void leituraNegativaDeveSerRejeitadaMantendoUltimoValorValido() {
        ZonaRestrita zona = novaZona();
        zona.registrarLeituraRadiacao(1.0);
        assertThrows(IllegalArgumentException.class, () -> zona.registrarLeituraRadiacao(-5));
        assertEquals(1.0, zona.getNivelRadiacaoAtual());
    }

    @Test
    void leituraAbsurdaDeveSerRejeitada() {
        assertThrows(IllegalArgumentException.class, () -> novaZona().registrarLeituraRadiacao(1_000_001));
    }

    @Test
    void identificadorVazioDeveSerRejeitado() {
        assertThrows(IllegalArgumentException.class, () -> new ZonaRestrita("", "Casa do Reator", 3.0));
    }

    @Test
    void alertaDeveGuardarMesmaReferenciaDoSensorOrigem() {
        ZonaRestrita zona = novaZona();
        SensorRadiacao sensor = new SensorRadiacao("RAD-01", "Reator", zona);
        sensor.registrarLeitura(4.7);
        CentralMonitoramento central = new CentralMonitoramento(new DispositivoMonitoramento[]{sensor});
        List<AlertaSeguranca> alertas = central.analisarLeituras();
        assertEquals(1, alertas.size());
        assertSame(sensor, alertas.get(0).getSensorOrigem());
        assertEquals(Severidade.CRITICA, alertas.get(0).getSeveridade());
    }

    @Test
    void filhasHerdamCodigoIdentificadorDaMae() {
        ZonaRestrita zona = novaZona();
        assertEquals("RAD-01", new SensorRadiacao("RAD-01", "Reator", zona).getCodigoIdentificador());
        assertEquals("PER-01", new SensorPerimetral("PER-01", "Cerca", zona).getCodigoIdentificador());
        assertEquals("DRN-01", new DronePatrulha("DRN-01", "Base", "Rota A").getCodigoIdentificador());
    }
}
