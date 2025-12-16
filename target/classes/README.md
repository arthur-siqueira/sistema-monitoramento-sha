# Painel Fictício de Monitoramento de Hidrômetros (SMH)

Projeto desenvolvido para a disciplina de **Padrões de Projeto** (Professor Katyusco Santos), com foco na aplicação do padrão **Facade** para centralizar o acesso aos subsistemas de um painel de monitoramento de consumo de água para concessionárias como a CAGEPA.

O sistema deve gerenciar usuários, monitorar consumo por meio de processamento de imagens (OCR simulado) de hidrômetros (SHAs) e disparar alertas quando limites forem ultrapassados.

## 📋 Status do Projeto (Atualizado em 12/12/2025)

1. **Documentação e Modelagem**
    - ✅ Especificação completa da Fachada (PDF incluso: `Especificação de Fachada para Painel Fictício de Monitoramento.pdf`)
    - ✅ Diagrama de Classes geral com comunicação entre fachada e subsistemas
    - ✅ Definição detalhada dos Requisitos Funcionais (RF-001, RF-002, RF-003)

2. **Estrutura do Código**
    - ✅ Pacotes criados: `dto`, `facade`, `services`, `strategy`, `hardware`
    - ✅ Interface `ISMH_Facade` totalmente especificada
    - ✅ Implementação inicial da classe `MonitoramentoFacade` (padrão Facade)
    - ⬜ Implementação completa dos delegates para todos os subsistemas

3. **Subsistemas e Funcionalidades**
    - ⬜ Subsistema de Usuários e Contas (CRUD completo + associação de SHAs)
    - ⬜ Subsistema de Sensores (processamento de imagens com OCR simulado via Tess4J)
    - ⬜ Integração com simuladores de SHAs de outros alunos (download de imagens do GitHub)
    - ⬜ Monitoramento individual e agregado de consumo
    - ⬜ Subsistema de Alertas (motor de regras com Strategy Pattern)
    - ⬜ Subsistema de Notificações (e-mail/webhook)
    - ⬜ Subsistema de Log e Rastreabilidade

**Progresso Geral**: 20%

## 🚀 Como Executar (Estado Atual)

**Pré-requisitos**
- Java JDK 17 ou superior
- IDE (IntelliJ ou Eclipse recomendado)

**Passos**
1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/sistema-monitoramento-sha.git

2. Abra o projeto na IDE
3. Compile as classes existentes
4. Execute a classe Main.java (pacote br.edu.ifpb.smh) para testar chamadas mockadas na fachada

Observação: A implementação atual utiliza dados mockados. A integração real com OCR (Tess4J) e download automático de imagens de SHAs de outros repositórios será adicionada nas próximas semanas.
## 🛠 Padrões de Projeto Aplicados

- Facade → MonitoramentoFacade (centraliza acesso aos subsistemas)
- DTO → Objetos de transferência de dados (pasta dto)
- Strategy → Planejado para regras configuráveis de alertas (pasta strategy)
- Repository → Planejado para abstração de persistência no Subsistema de Usuários
- Singleton → Futuro uso no serviço de logging

## 👨‍💻 Informações
- Desenvolvido por: Arthur Henrique Siqueira Pantaleaão
- Professor: Katyusco Santos
- Disciplina: Padrões de Projeto
- Curso: Engenharia de Computação - IFPB
- Em construção! Próximos passos: integração com OCR, conexão real com SHAs de colegas e implementação completa dos alertas. 🚧