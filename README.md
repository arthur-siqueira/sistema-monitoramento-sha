# Painel Fictício de Monitoramento de Hidrômetros (SMH)

Projeto desenvolvido para a disciplina de **Padrões de Projeto** (Professor Katyusco Santos), com foco na aplicação do padrão **Facade** para centralizar o acesso aos subsistemas de um painel de monitoramento de consumo de água para concessionárias como a CAGEPA.

O sistema gerencia usuários, monitora consumo através de leitura de imagens (OCR) geradas por simuladores de hidrômetros e dispara alertas baseados em regras configuráveis.

## 📋 Status do Projeto (Entrega Final)

### 1. Documentação e Modelagem
- ✅ Especificação completa da Fachada (PDF incluso).
- ✅ Diagrama de Classes geral com comunicação entre fachada e subsistemas.
- ✅ Definição detalhada dos Requisitos Funcionais (RF-001, RF-002, RF-003).

### 2. Estrutura do Código
- ✅ Pacotes organizados: `dto`, `facade`, `services`, `strategy`, `hardware`.
- ✅ Interface `ISMH_Facade` implementada conforme contrato.
- ✅ Classe `MonitoramentoFacade` atuando como controlador central.
- ✅ Injeção de dependência manual entre os serviços (`SensorService` comunicando com `AlertaService`).

### 3. Subsistemas e Funcionalidades
- ✅ **Subsistema de Usuários:** CRUD em memória (HashMap) e vinculação de múltiplos SHAs a um usuário.
- ✅ **Subsistema de Sensores (OCR Real):** - Integração com a biblioteca **Tess4J (Tesseract OCR)**.
   - Implementação de algoritmos de pré-processamento de imagem (Zoom/Escala e Recorte estratégico) para alta precisão de leitura.
   - Leitura em tempo real (file watcher) detectando atualizações nos simuladores.
- ✅ **Integração com Hardware:** Uso do padrão **Adapter** para suportar diferentes tipos de simuladores (Arquivo único ou Histórico em pastas).
- ✅ **Subsistema de Alertas:** Motor de regras que valida o consumo em tempo real.
- ✅ **Subsistema de Notificações:** Implementado com padrão **Strategy** (suporte a E-mail e Webhook simulados no console).

**Progresso Geral**: 🚀 100% (Funcional)

## 🚀 Como Executar

### Pré-requisitos
- **Java JDK 11** ou superior (Testado com JDK 21).
- **Maven** instalado.
- **Simulador de Hidrômetro**: É necessário ter o simulador rodando em paralelo gerando a imagem `hidrometro_saida.png`.

### ⚠️ Configuração Importante (Caminho do Simulador)
Como o sistema lê um arquivo local gerado pelo simulador, é necessário ajustar o caminho absoluto no arquivo `src/main/java/br/edu/ifpb/smh/services/SensorService.java` antes de rodar:

```java
leitoresConfigurados.add(new AdapterSimuladorUnico(
    "SHA-ARTHUR-01",
    "C:\\caminho\\para\\sua\\pasta\\padroes-de-projeto\\hidrometro_saida.png" 
));
```
### Passos para Teste
- Clone o repositório.

- Abra o projeto na IDE (IntelliJ recomendado) e atualize o Maven.

- Execute o Simulador: Rode a classe Simulador.java (do projeto externo) para começar a gerar as imagens.

- Execute o SMH: Rode a classe Main.java (pacote br.edu.ifpb.smh).

- Acompanhe no console:

  - A criação do usuário.

  - A detecção automática de atualização do arquivo de imagem.

  - O log de debug do OCR ([DEBUG OCR]).

  - O disparo de alertas caso o consumo ultrapasse o limite configurado (ex: 0.01).

### 🛠 Padrões de Projeto Aplicados
1. Facade (MonitoramentoFacade):

   - Centraliza a complexidade. O cliente (Main) não conhece o Tesseract ou as regras de alerta, apenas chama facade.iniciarMonitoramento().

2. Adapter (AdapterSimuladorUnico implements LeitorSHA):

   - Permite que o sistema leia dados de um simulador que sobrescreve um arquivo único (.png), adaptando-o para a interface de leitura padrão do sistema.

3. Strategy (EmailStrategy, WebhookStrategy implements CanalNotificacaoStrategy):

   - Permite trocar o método de envio de notificação dinamicamente sem alterar a classe de serviço.

4. DTO (Pacote dto):

   - Desacopla as entidades de domínio da interface pública da fachada.

### 👨‍💻 Informações
- Desenvolvido por: Arthur Henrique Siqueira Pantaleaão

- Professor: Katyusco Santos

- Disciplina: Padrões de Projeto

- Curso: Engenharia de Computação - IFPB