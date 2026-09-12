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
- [`postman/`](postman/) — coleção Postman para testar as rotas manualmente.

## Como foi implementado

Spring Boot 4.1.1, Java 26, Gradle (Kotlin DSL), Spring Data JPA, PostgreSQL, Spring Security
(endpoints de negócio) + token interno compartilhado (endpoint de sincronização com o serviço de
venda), Spring Boot Actuator/Micrometer (observabilidade). Estado atual: em desenvolvimento — a
modelagem e os contratos de API estão definidos e documentados em `docs/`; a implementação de
código (entidades, controllers, persistência, job de sincronização, testes e pipeline de CI/CD)
está em andamento.

## Estrutura do projeto

```
src/main/java/com/soat/posvendaauto/
├── veiculo/           # cadastro/edição de veículo (controller, service, repository, entidade)
├── sincronizacao/      # outbox de eventos + job de reenvio para o serviço de venda
├── auditoria/          # log de sucesso/erro das operações
└── config/             # segurança, cliente HTTP do serviço de venda, etc.
docs/                    # arquitetura, HLD/LLD, modelagem, contratos de API, decisões
openapi.yaml             # especificação Swagger/OpenAPI
postman/                 # coleção Postman
k8s/                     # manifests Deployment + Service (deploy local via kind)
```

## Como usar localmente

Pré-requisitos: JDK 26 (ver `docs/decisoes-pendentes.md`, item 6, sobre o risco dessa versão no
CI) e Docker (para o banco de dados via `compose.yaml`).

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
