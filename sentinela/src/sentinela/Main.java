package sentinela;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        ZonaRestrita casaDoReator = new ZonaRestrita("ZNR-01", "Casa do Reator", 3.0);
        ZonaRestrita patioCombustivel = new ZonaRestrita("ZNR-02", "Pátio de Armazenamento de Combustível Irradiado", 2.0);

        SensorRadiacao sensorRadiacaoReator = new SensorRadiacao("RAD-01", "Interior da Casa do Reator", casaDoReator);
        SensorRadiacao sensorRadiacaoPatio = new SensorRadiacao("RAD-02", "Pátio de Combustível", patioCombustivel);
        SensorPerimetral sensorCerca = new SensorPerimetral("PER-01", "Cerca Leste", patioCombustivel);
        DronePatrulha dronePatrulha = new DronePatrulha("DRN-01", "Base de Drones", "Rota Perimetral Alfa");

        DispositivoMonitoramento[] dispositivos = {
                sensorRadiacaoReator, sensorRadiacaoPatio, sensorCerca, dronePatrulha
        };
        CentralMonitoramento central = new CentralMonitoramento(dispositivos);

        // Cenário 1
        cabecalho("CENÁRIO 1 - ROTINA NORMAL");
        sensorRadiacaoReator.registrarLeitura(0.4);
        sensorRadiacaoPatio.registrarLeitura(0.8);
        dronePatrulha.patrulhar();
        imprimirLeituras(dispositivos);
        imprimirAlertas(central.analisarLeituras());

        // Cenário 2
        cabecalho("CENÁRIO 2 - PICO DE RADIAÇÃO");
        sensorRadiacaoReator.registrarLeitura(4.7);
        sensorRadiacaoPatio.registrarLeitura(0.8);
        dronePatrulha.patrulhar();
        imprimirLeituras(dispositivos);
        imprimirAlertas(central.analisarLeituras());

        // Cenário 3
        cabecalho("CENÁRIO 3 - ACESSO INDEVIDO");
        sensorRadiacaoReator.registrarLeitura(0.4);
        sensorRadiacaoPatio.registrarLeitura(0.8);
        sensorCerca.registrarViolacaoCerca();
        dronePatrulha.getCamera().simularObjetoNaCena("Veículo não identificado");
        imprimirLeituras(dispositivos);
        imprimirAlertas(central.analisarLeituras());
    }

    private static void cabecalho(String titulo) {
        System.out.println("\n==================================================");
        System.out.println(titulo);
        System.out.println("==================================================");
    }

    private static void imprimirLeituras(DispositivoMonitoramento[] dispositivos) {
        System.out.println("-- Leituras registradas --");
        for (DispositivoMonitoramento dispositivo : dispositivos) {
            System.out.println("  " + dispositivo + ": " + dispositivo.descreverStatus());
        }
    }

    private static void imprimirAlertas(List<AlertaSeguranca> alertas) {
        System.out.println("-- Alertas da central --");
        if (alertas.isEmpty()) {
            System.out.println("  Nenhum alerta. Instalação em condição normal.");
            return;
        }
        for (AlertaSeguranca alerta : alertas) {
            System.out.println("  " + alerta);
        }
    }
}
