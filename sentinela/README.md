# SENTINELA – Sprint 1 (Domínio Base)

Protótipo em console para monitoramento de segurança física de uma instalação nuclear fictícia.

## Como executar
Requer JDK 17+.
```bash
javac -encoding UTF-8 -d out src/sentinela/*.java
java -cp out sentinela.Main
```
Os testes em `test/` usam JUnit 5 (adicione o JUnit Jupiter ao projeto/IDE para rodá-los).

## Modelo
```
DispositivoMonitoramento (abstrata)
 ├── SensorRadiacao   → TEM-UMA ZonaRestrita (zonaMonitorada)
 ├── SensorPerimetral → TEM-UMA ZonaRestrita (setorVigiado)
 └── DronePatrulha    → TEM-UMA CameraVisaoComputacional
AlertaSeguranca → TEM-UM DispositivoMonitoramento (sensorOrigem) e TEM-UMA ZonaRestrita (zonaAfetada)
CentralMonitoramento → TEM-UM array de DispositivoMonitoramento; analisarLeituras() = motor de regras
```
Faixas de risco (em `ZonaRestrita.classificarRisco`): `NORMAL` < 50% do limite; `ATENCAO` ≥ 50%; `CRITICO` ≥ limite.

## Respostas de reflexão
**Classes e Objetos:** `ZonaRestrita` é o molde (define o que toda zona possui e faz); "ZNR-01 - Casa do Reator" é uma instância concreta, com estado próprio (identificador, descrição, nível atual). Existe uma classe, mas podem existir muitos objetos.

**Métodos:** a regra mora na zona porque é ela quem conhece seu limite e seu nível (coesão). Espalhada em `if`s no `main`, seria duplicada, poderia ficar inconsistente e mudar a regra exigiria caçar código em vários lugares.

**Encapsulamento:** com o atributo público, qualquer código poderia gravar valores impossíveis (negativos, 1e9) ou "zerar" um pico crítico antes do relatório, falsificando o histórico da central sem passar por validação nem deixar rastro.

**Construtores:** ao definir um construtor customizado, o Java deixa de gerar o padrão; logo `new ZonaRestrita()` não compila e é impossível criar uma "zona fantasma" sem identificador, descrição e limite válidos.

**Associação:** guardando só a String do código, a central perderia o acesso à localização do sensor, ao estado dele e à zona afetada (nível de radiação, limite, risco), tendo que procurar tudo de novo para escalonar o incidente. Com a referência ao objeto, tudo está a um `get` de distância.

**Herança:** `codigoIdentificador` é `private` na mãe (encapsulamento): só a mãe decide como validá-lo e atribuí-lo. Passar pelo `super()` garante que a validação do construtor da mãe sempre seja executada; a filha não pode contorná-la.

## Decisões de projeto
- `AlertaSeguranca` recebe a mensagem via `comMensagem(...)` para respeitar a regra dos 3 parâmetros no construtor.
- `SensorRadiacao` obtém `limiteSeguranca` da zona monitorada (evita um 4º parâmetro e a duplicação do limite).
- O drone TEM-UMA câmera (associação), não herda de sensor (evita "herança gulosa").
