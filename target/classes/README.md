# Painel de Monitoramento de Hidrômetros (SMH)

> **Projeto Final - Padrões de Projeto** > **Curso:** Engenharia de Computação - IFPB  
> **Disciplina:** Padrões de Projeto (Prof. Katyusco Santos)

O **SMH** é um sistema distribuído simulado que aplica o padrão **Facade** para centralizar o acesso a subsistemas complexos de monitoramento de consumo de água. O sistema realiza leitura automatizada de hidrômetros via OCR (Reconhecimento Óptico de Caracteres), gerencia usuários e dispara alertas baseados em regras de negócio configuráveis.

---

## 📋 Status do Projeto

✅ **Entrega Final (v1.0)** - Sistema completo, estável e testado.

### 1. Documentação e Modelagem
* [x] Especificação completa da Fachada (PDF incluso).
* [x] Diagrama de Classes (UML) detalhando a comunicação Facade <-> Subsistemas.
* [x] Definição dos Requisitos Funcionais (RF-001 a RF-003).

### 2. Estrutura do Código
* **Arquitetura em Camadas:** Pacotes organizados em `dto`, `facade`, `services`, `strategy`, `hardware`.
* **Contrato Rígido:** Interface `ISMH_Facade` implementada integralmente.
* **Controller:** Classe `MonitoramentoFacade` orquestrando o fluxo de dados.

### 3. Funcionalidades Avançadas
* **Leitura OCR Real:** Integração com **Tess4J (Tesseract)** com algoritmos de pré-processamento (zoom/recorte) para alta precisão.
* **CLI Interativa:** Interface de linha de comando com suporte a comandos (`status`, `sair`) e *Graceful Shutdown*.
* **Monitoramento em Tempo Real:** Detecção automática de novos arquivos (Watcher) gerados pelos sensores.
* **Alocação Dinâmica:** Suporte a múltiplos sensores vinculados a um único usuário.

---

## 🛠 Padrões de Projeto Aplicados (6 Padrões)

O sistema utiliza um conjunto robusto de padrões GOF para garantir desacoplamento e extensibilidade:

1.  **Facade (`MonitoramentoFacade`)**:
    * **Objetivo:** Centraliza a complexidade. O cliente (`Main`) desconhece o OCR, o Banco H2 ou o motor de regras, interagindo apenas via método simples `iniciarMonitoramento()`.

2.  **Adapter (`AdapterSimuladorUnico` / `LeitorSHA`)**:
    * **Objetivo:** Permite que o sistema leia dados de diferentes fontes incompatíveis (simuladores de arquivo único vs. pastas de histórico), adaptando-os para a interface padrão `LeitorSHA` esperada pelo sistema.

3.  **Strategy (`CanalNotificacaoStrategy`)**:
    * **Objetivo:** Permite a troca dinâmica do algoritmo de notificação (E-mail Simulado ou Webhook) sem alterar a classe de serviço de alertas, facilitando a adição de novos canais no futuro.

4.  **Singleton (`ConexaoBanco` / `Services`)**:
    * **Objetivo:** Garante que objetos pesados (como a conexão com o banco de dados H2 ou os Serviços de Monitoramento) tenham uma **única instância** compartilhada durante toda a execução, economizando recursos.

5.  **Observer (`Sensor` -> `AlertaService`)**:
    * **Objetivo:** Implementa o comportamento reativo do sistema. O Serviço de Alertas "observa" os Sensores; quando uma nova leitura é realizada, o sistema de alertas é notificado automaticamente para validar as regras de consumo.

6.  **Factory Method (`SensorFactory`)**:
    * **Objetivo:** Encapsula a lógica de criação dos sensores. O sistema decide qual tipo de `Adapter` instanciar (Simulador Arquivo ou Simulador Pasta) com base na configuração, sem expor a lógica de instanciação para o código cliente.

---

## 🚀 Como Executar

### Pré-requisitos
* **Java JDK 21** (Recomendado para compatibilidade com Tess4J).
* **Maven** instalado.
* **Tesseract OCR** instalado no sistema operacional.

### ⚠️ Configuração de Ambiente
Antes de rodar, verifique o arquivo `SensorService.java` (ou `Main.java`) e ajuste o caminho absoluto para as pastas onde os simuladores salvarão as imagens (ex: `C:\temp\simuladores`).

### Passo a Passo

1.  **Inicie o Servidor (Main):**
    * Execute a classe `Main.java` no IntelliJ.
    * Aguarde a mensagem: `>>> [SISTEMA RODANDO] Aguardando leituras...`

2.  **Inicie os Simuladores (Terminais Externos):**
    * Abra um terminal separado (CMD/PowerShell) para cada sensor.
    * Execute os scripts geradores de imagem ou copie imagens manualmente para as pastas monitoradas (ex: `SHA-ARTHUR-01`).

3.  **Interaja com o Sistema:**
    * O console do `Main` exibirá os logs de OCR e alertas em tempo real.
    * Digite **`status`** para ver o resumo de consumo.
    * Digite **`sair`** para encerrar o servidor e salvar o estado.

---

## 👨‍💻 Autor

**Arthur Henrique Siqueira Pantaleaão** *Engenharia de Computação - IFPB*