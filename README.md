# Sistema de Pedidos e Mesas para Restaurante

Projeto da disciplina **Padrões de Projeto (PP)** — Avaliação M1, Parte 2.

## Como rodar (console)

1. Instale MySQL e crie o banco executando `sql/schema.sql`.
2. Ajuste usuário/senha em `ConnectionFactory.java` se necessário (padrão: `root`/`root`).
3. Compile e execute com Maven:
   ```
   mvn clean package
   java -jar target/restaurante-app.jar
   ```

## Tela interativa (HTML)

Há duas versões da tela web, dentro da pasta `web/`:

- **`web/restaurante-app-demo.html`** — demonstração autônoma: roda 100% no
  navegador, guarda os dados no `localStorage` da própria página. Não
  precisa do Java nem do MySQL rodando; é só abrir o arquivo. Boa para
  mostrar o fluxo do sistema rapidamente, sem precisar subir o banco.

- **`web/restaurante-app-integrado.html`** — versão conectada de verdade
  à API Java (e por trás dela, ao MySQL). Os dados que aparecem na tela
  são os mesmos que o programa de console lê e grava.

### Como rodar a versão integrada

1. Suba o banco (`sql/schema.sql`) e gere o jar com as dependências:
   ```
   mvn clean package
   ```
2. Suba a API REST (fica ouvindo em `http://localhost:8080/api`):
   ```
   java -cp target/restaurante-app.jar com.restaurante.ApiMain
   ```
3. Abra `web/restaurante-app-integrado.html` no navegador (duplo clique).
   A página mostra "✅ Conectado à API" quando tudo estiver certo.

A API (pacote `com.restaurante.api`) é só uma camada HTTP fina por cima
do `RestauranteController` já existente — ela não duplica regra de
negócio nenhuma, apenas expõe as mesmas operações (abrir pedido,
adicionar item, fechar comanda, etc.) como rotas JSON:

| Método | Rota                          | Ação                                  |
|--------|-------------------------------|----------------------------------------|
| GET    | `/api/mesas`                  | Lista mesas                            |
| POST   | `/api/mesas`                  | Cadastra mesa                          |
| DELETE | `/api/mesas/{id}`              | Remove mesa                            |
| GET    | `/api/produtos`                | Lista produtos                         |
| POST   | `/api/produtos`                | Cadastra produto                       |
| PATCH  | `/api/produtos/{id}/toggle`    | Alterna disponibilidade                |
| GET    | `/api/clientes`                | Lista clientes                         |
| POST   | `/api/clientes`                | Cadastra cliente                       |
| GET    | `/api/pedidos`                 | Lista pedidos                          |
| GET    | `/api/pedidos/{id}`            | Consulta um pedido                     |
| POST   | `/api/pedidos`                 | Abre pedido `{mesaId, clienteId, garcom}` |
| POST   | `/api/pedidos/{id}/itens`      | Adiciona item `{produtoId, quantidade, observacao}` |
| POST   | `/api/pedidos/{id}/fechar`     | Fecha comanda `{formaPagamento}` (automação de negócio) |

## Entidades (mais de 10 atributos em `Pedido`)

- **Mesa**: id, numero, capacidade, localizacao, status
- **Cliente**: id, nome, telefone, cpf
- **Produto**: id, nome, categoria, preco, descricao, disponivel, tempoPreparoMin
- **ItemPedido**: id, pedidoId, produto, quantidade, precoUnitario, observacao
- **Pedido** (13 atributos): id, mesa, cliente, garcom, itens, status, dataAbertura,
  dataFechamento, subtotal, taxaServico, valorTotal, formaPagamento, observacao

## Relacionamentos

- **1:1** — `Mesa` ↔ `Pedido` aberto (uma mesa tem no máximo um pedido em aberto por vez;
  coluna `mesa_id` é `UNIQUE` na tabela `pedido`).
- **1:n** — `Pedido` → `ItemPedido` (um pedido tem vários itens).
- (bônus) **n:1** — `Cliente` → `Pedido` (um cliente pode ter vários pedidos ao longo do tempo).

## Funcionalidades (CRUD + automação)

- Inserir / listar / remover **Mesa**, **Produto**, **Cliente**.
- Inserir / consultar por id / listar **Pedido**.
- **Automação de processo de negócio**: `fecharPedido()` calcula automaticamente
  subtotal, aplica 10% de taxa de serviço, define o valor total, registra a forma
  de pagamento e libera a mesa — tudo numa única operação (ver `RestauranteController`
  e `FecharPedidoCommand`).

## Onde cada Design Pattern está

| Padrão | Onde | Por quê |
|---|---|---|
| **DAO** | pacote `dao` (interfaces) e `dao.impl` (implementações JDBC) | Isola o acesso a dados (SQL) do resto da aplicação. |
| **MVC** | `model` (entidades), `view` (`ConsoleView`, `MenuView`), `controller` (`RestauranteController`) | Separa dados, interface e regra de negócio. |
| **FACTORY METHOD** | `ConnectionFactory` (cria `Connection`) e `DAOFactory` (cria os DAOs) | O código cliente não conhece as classes concretas, facilita trocar a implementação. |
| **BUILDER** | `PedidoBuilder` | Monta um `Pedido` (objeto com muitos atributos) passo a passo, de forma legível. |
| **COMMAND** | pacote `command` + `MenuView` (invoker) | Cada opção do menu é um objeto `Command` independente, desacoplando o menu da lógica executada. |

## Próximos passos sugeridos para fechar os critérios da rubrica

- Adicionar diagrama de classes e de sequência (ex. em draw.io ou PlantUML).
- Se o projeto evoluir para Web/API, documentar os endpoints.
- Subir o repositório no GitHub e configurar um deploy (ex. Railway, Render).
- Revisar SOLID/Object Calisthenics (ex. extrair validações do Controller para
  classes de validação dedicadas, se quiser ir além).
