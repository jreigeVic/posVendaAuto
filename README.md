# posVendaAuto — Software principal

Autor: [jreigeVic](https://github.com/jreigeVic)

## O que é o projeto

API do **software principal** de uma plataforma de revenda de veículos automotores, desenvolvida
como Trabalho Substitutivo de Tech Challenge (Fase 4, Pós Tech SOAT).

## Para que serve

É responsável pelo **cadastro e edição de veículos** (marca, modelo, ano, cor, preço, estado de
conservação) — a fonte da verdade desses dados. A listagem de veículos e todo o fluxo de
compra/pagamento ficam isolados em um segundo serviço, o
[`venda-veiculos-service`](https://github.com/jreigeVic/venda-veiculos-service), que possui banco
de dados próprio e escala de forma independente para suportar picos de tráfego nas listagens e
compras. Os dois serviços se comunicam apenas por HTTP — nenhum acesso direto a banco entre eles;
a cada cadastro/edição, este serviço propaga a mudança para o serviço de venda de forma resiliente
(padrão Outbox, com reenvio automático se o outro serviço estiver fora do ar).

## Documentação

- [`docs/arquitetura.md`](docs/arquitetura.md) — visão geral da solução e fluxo ponta a ponta.
- [`docs/hld.md`](docs/hld.md) — High-Level Design: contexto, componentes, infraestrutura de deploy.
- [`docs/lld.md`](docs/lld.md) — Low-Level Design: classes, entidades, banco de dados, sequências.
- [`docs/modelagem.md`](docs/modelagem.md) — entidades, campos e ciclo de vida do veículo/venda.
- [`docs/contratos-api.md`](docs/contratos-api.md) — contratos REST dos dois serviços.
- [`docs/decisoes-pendentes.md`](docs/decisoes-pendentes.md) — decisões de projeto tomadas para
  preencher lacunas do enunciado, com a justificativa de cada uma.
- [`openapi.yaml`](openapi.yaml) — especificação Swagger/OpenAPI desta API.
- Swagger UI interativo: `GET /swagger-ui/index.html` (com a app rodando localmente), gerado a
  partir dos controllers reais via springdoc.
- [`postman/`](postman/) — coleção Postman para testar as rotas manualmente.

## Como foi implementado

Spring Boot 4.1.1, Java 26, Gradle (Kotlin DSL), Spring Data JPA, PostgreSQL, Spring Security
(endpoints de negócio) + token interno compartilhado (endpoint de sincronização com o serviço de
venda), Spring Boot Actuator/Micrometer (observabilidade), springdoc-openapi (Swagger UI). Segue
**Arquitetura Hexagonal** (Ports & Adapters): Domain sem dependência de Spring/JPA/HTTP,
Application conhecendo apenas Ports, Adapters isolando a tecnologia concreta — ver detalhamento em
[`docs/lld.md`](docs/lld.md). Testes automatizados cobrindo Domain, Application e Adapters, com
cobertura acima de 98% (mínimo exigido: 80%). CI/CD (build, testes, gate de cobertura e deploy
automatizado em Pull Request/merge) validado de ponta a ponta via GitHub Actions.

## Estrutura do projeto

```
src/main/java/com/soat/posvendaauto/
├── veiculo/
│   ├── domain/          # Veiculo, EstadoConservacao — sem dependência de framework
│   ├── application/     # Use Cases (CadastrarVeiculoService, EditarVeiculoService) e Ports
│   └── adapter/         # in/web (controller) e out/persistence (JPA)
├── sincronizacao/
│   ├── domain/          # EventoSincronizacao (Outbox), StatusEvento, TipoEvento
│   ├── application/     # Use Cases de sincronização/reenvio e Ports
│   └── adapter/         # in/scheduler (job), out/http (cliente) e out/persistence (JPA)
├── auditoria/           # AuditoriaPort + adapter de persistência do log de sucesso/erro
├── web/                 # GlobalExceptionHandler (tratamento de erro transversal)
└── config/              # segurança, cliente HTTP do serviço de venda, etc.
docs/                    # arquitetura, HLD/LLD, modelagem, contratos de API, decisões
openapi.yaml             # especificação Swagger/OpenAPI
postman/                 # coleção Postman
k8s/                     # manifests Deployment + Service (deploy local via kind)
```

## Como usar localmente

Pré-requisitos: JDK 26 (disponibilidade em CI confirmada — ver `docs/decisoes-pendentes.md`, item
6) e Docker (para o banco de dados via `compose.yaml`).

```bash
./gradlew bootRun
```

No Windows, use `gradlew.bat bootRun`.

## Como testar

```bash
./gradlew test      # roda os testes
./gradlew check     # roda os testes e falha se a cobertura ficar abaixo de 80%
```

Relatório de cobertura (JaCoCo) gerado em `build/reports/jacoco/test/html/index.html`.

No Windows, use `gradlew.bat test` / `gradlew.bat check`.
