package sentinela;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Motor de regras da sprint: varre os dispositivos e gera alertas. */
public class CentralMonitoramento {

    private final DispositivoMonitoramento[] dispositivos;

    public CentralMonitoramento(DispositivoMonitoramento[] dispositivos) {
        if (dispositivos == null || dispositivos.length == 0) {
            throw new IllegalArgumentException("A central precisa de ao menos um dispositivo.");
        }
        this.dispositivos = dispositivos;
    }

    public List<AlertaSeguranca> analisarLeituras() {
        List<AlertaSeguranca> alertas = new ArrayList<>();
        for (DispositivoMonitoramento dispositivo : dispositivos) {
            if (!dispositivo.isEmOperacao()) {
                continue;
            }
            if (dispositivo instanceof SensorRadiacao sensorRadiacao) {
                analisarRadiacao(sensorRadiacao, alertas);
            } else if (dispositivo instanceof SensorPerimetral sensorPerimetral) {
                analisarPerimetro(sensorPerimetral, alertas);
            }
        }
        return alertas;
    }

    private void analisarRadiacao(SensorRadiacao sensorOrigem, List<AlertaSeguranca> alertas) {
        ZonaRestrita zonaAfetada = sensorOrigem.getZonaMonitorada();
        String risco = zonaAfetada.classificarRisco();
        if (risco.equals("NORMAL")) {
            return;
        }
        Severidade severidade = risco.equals("CRITICO") ? Severidade.CRITICA : Severidade.MEDIA;
        String mensagem = String.format(Locale.forLanguageTag("pt-BR"),
                "Radiação %s: %.2f µSv/h (limite %.2f µSv/h)",
                risco, sensorOrigem.getLeituraAtual(), sensorOrigem.getLimiteSeguranca());
        alertas.add(new AlertaSeguranca(sensorOrigem, zonaAfetada, severidade).comMensagem(mensagem));
    }

    private void analisarPerimetro(SensorPerimetral sensorOrigem, List<AlertaSeguranca> alertas) {
        if (!sensorOrigem.isViolacaoDetectada()) {
            return;
        }
        ZonaRestrita zonaAfetada = sensorOrigem.getSetorVigiado();
        alertas.add(new AlertaSeguranca(sensorOrigem, zonaAfetada, Severidade.ALTA)
                .comMensagem("Violação da cerca detectada. Acionando drone de patrulha."));

        DronePatrulha droneDisponivel = buscarDroneDisponivel();
        if (droneDisponivel == null) {
            alertas.add(new AlertaSeguranca(sensorOrigem, zonaAfetada, Severidade.ALTA)
                    .comMensagem("Nenhum drone disponível para confirmar a ocorrência."));
            return;
        }
        droneDisponivel.despacharPara(zonaAfetada);
        droneDisponivel.getCamera().analisarImagem().ifPresent(objeto ->
                alertas.add(new AlertaSeguranca(droneDisponivel, zonaAfetada, Severidade.CRITICA)
                        .comMensagem("Visão computacional confirmou: " + objeto + ".")));
    }

    private DronePatrulha buscarDroneDisponivel() {
        for (DispositivoMonitoramento dispositivo : dispositivos) {
            if (dispositivo instanceof DronePatrulha drone && drone.estaProntoParaMissao()) {
                return drone;
            }
        }
        return null;
    }

    public DispositivoMonitoramento[] getDispositivos() { return dispositivos; }
}
