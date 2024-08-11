# fiap-fastfood

## Recursos e Bibliotecas
- [x] Java 17
- [x] Mongo
- [x] PostgreSQL
- [x] SQS
- [x] Spring Boot
- [x] MapStruct
- [x] Vavr
- [x] JsonPatch


## Dicionário de Linguagem Ubíqua

Termos utilizados na implementação (Presentes em Código)

- **Cliente/Customer**: O consumidor que realiza um pedido no restaurante.
- **Pedido/Order**: A lista de produtos (seja uma bebida, lanche, acompanhamento e/ou sobremesa) realizada pelo cliente no restaurante.
- **Produto/Product**: Item que será consumido pelo cliente, que se enquadra dentro de uma categoria, como por exemplo: bebida, lanche, acompanhamento e/ou sobremesa.
- **Categoria/Product Type**: Como os produtos são dispostos e gerenciados pelo estabelecimento: bebidas, lanches, acompanhamentos e/ou sobremesas.
- **Esteira de Pedidos/Order Tracking**: Responsável pelo andamento e monitoramento do estado do pedido.
- **Funcionário/Employee**: Funcionário do estabelecimento.


## Arquitetura da Solução

![arquitetura.png](.img%2Farquitetura.png)

## Fluxo Comum
![happy-path.png](.img%2Fhappy-path.png)

## Fluxo de Erro de Inserção de Pedidos
![falha-inserção-pedido.png](.img%2Ffalha-inser%C3%A7%C3%A3o-pedido.png)

## Fluxo de Erro para Pagamentos rejeitados(ou com erro) pelo "Mercado Livre"
![falha-pagamento.png](.img%2Ffalha-pagamento.png)

## Fluxo de Cancelamento Manual de um Pedido
![pagamento-rejeitado.png](.img%2Fpagamento-rejeitado.png)

## Saga da Criação de Pedidos
![saga-pedido.png](.img%2Fsaga-pedido.png)


## [Gerenciamento de produtos]([ProductController.java](fastfood-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Fproduct%2FProductController.java))

A aplicação dispõe de operações que permitem a inserção, edição, listagem(paginada) e a remoção de produtos. Em detalhe
a possibilidade de se filtar produtos por categoria ao listar os mesmos.
Não há controle de estoque implementado na versão atual da aplicação.

### Mapeamento da entidade

    id : Identificador do produto, gerado automaticamente pela base de dados
    id da categoria : Identificador da categoria do produto
    descricao : Descrição do produto
    categoria : { 
        id da categoria : Identificador da categoria do produto
        descricao : Descrição da categoria
    }

## [Gerenciamento de Clientes]([CustomerController.java](fastfood-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Fcustomer%2FCustomerController.java))

A inserção do cliente ocorre de forma opcional e em conjunto ao registro do pedido. No entanto, conforme requisito,
a aplicação dispõe de um endpoint para remoção total dos dados do cliente.

### Mapeamento da entidade

    id : Identificador do produto, gerado automaticamente pela base de dados
    nome : Identificador da categoria do produto
    cpf : Value Added Tax Identifier (CPF/CNPJ/VAT)
    email : Email do cliente
    telefone : Telefone do Cliente

## Contador de pedidos

Durante a descoberta do produto identificamos a necessidade de se ter um número de pedido de fácil compreensão ao
cliente.
Desta forma, implementamos uma sequence.

### Mapeamento da entidade

    id : Identificador único da base de dados
    nome : Nome da sequência
    sequência : Valor atual da sequência

## [Pedido e Pagamento]([OrderController.java](fastfood-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Forder%2FOrderController.java))

Persistidos separadamente na base de dados, porém criados em conjunto ao se iniciar um novo pedido.

Para o pagamento não existem operações que exponham consultas ou edições ao mesmo, sendo o estado do mesmo, nesta
versão, mapeado em conjunto na esteira de pedidos.

Já para o pedido estão disponíveis as operações de inserção, edição e listagem. Sendo a listagem paginada e com opção de
filtragem por id de pedido.

### Mapeamento da entidade de Pedidos

    id : Identificador único da base de dados
    id do cliente : Id de referência do cliente
    items : { 
        id do produto : Id de referência do produto
        quantidade : Quantidade solicitada do item em questão
    }
    data e hora : Data e hora do pedido
    numero do pedido : Número do Pedido, gerado pelo contador de pedidos

### Mapeamento da entidade de Pagamentos

    id : Identificador único da base de dados
    meio de pagamento : Meio escolhido para realização do pagamento
    valor : Valor a ser pago
    data e hora : Data e hora do pagamento
    id do pedido : Id de referência do pedido

## [Esteira de Pedidos]([OrderTrackingController.java](fastfood-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Ftracking%2FOrderTrackingController.java))

Criada para evitar alterações seguidas no domínio de pedidos, controla o fluxo de estados dos pedidos.
Dispõe de operações que permitem a inserção, listagem por pedido e um relatório que cumpre um dos requisitos de negócio,
listando pedidos com sua data de início e tempo decorrido.

### Mapeamento da entidade da Esteira de Pedidos

    id : Identificador único da base de dados
    id do pedido : Id de referência do pedido
    numero do pedido : Número do Pedido, gerado pelo contador de pedidos
    estado do pedido : [Estado do pedido]([OrderTrackingStatusTypeDTO.java](fastfood-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Ftracking%2Fdto%2FOrderTrackingStatusTypeDTO.java))
    visibilidade : Permissão de visibilidade do estado em questão, [Permissões]([OrderTrackingRoleTypeDTO.java](fastfood-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Ftracking%2Fdto%2FOrderTrackingRoleTypeDTO.java))
    data e hora : Data e hora da inserção do estado

## [Pagamentos (Integração)]([payment-mock-api](payment-mock-api))

Os pagamentos são inseridos em conjunto ao pedido, porém processados separadamente em uma api que mocka superficialmente
os comportamentos de um broker de pagamentos.

A operação de pagamento segue o seguinte fluxo:

1. O cliente finaliza o preenchimento do
   pedido/pagamento [na controladora de pedidos do fastfood](fastfood-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Forder%2FOrderController.java);
2. Ambas as entidades são persistidas;
3. Uma requisição REST é feita para a api
   de [mock de pagamentos](payment-mock-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Fpayment%2FPaymentController.java);
4. A api
   de [mock de pagamentos](payment-mock-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Fpayment%2FPaymentController.java)
   persiste o pagamento em seu lado e então o encaminha a uma fila para processamento assíncrono;
5. Cada mensagem persistida na fila
   é [lida](payment-mock-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fmessaging%2Fpayment%2FPaymentProcessorListener.java)
   e, neste mock, aprovada automáticamente;
6. Ainda no mesmo processo da fila, é feita uma chamada de callback para notificar do
   aceite daquele pagamento em questão;
7. Ao receber a confirmação de pagamento, registra-se um novo estado do pedido como 'PAYMENT_CONFIRMED'.


## Início rápido

```shell 
docker-compose up
```
A aplicação será disponibilizada em [localhost:8080](http://localhost:8080), tendo seu swagger em [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

## Dependências
Este projeto possui depende da existência de artefatos que são gerados por outros repositórios, segue o mapeamento:

>https://github.com/icarodamiani/fiap-fastfood-order
>>io.fiap.fastfood:fastfood-order-api:1.0.0-SNAPSHOT

>https://github.com/icarodamiani/fiap-fastfood-tracking
>>io.fiap.fastfood:fastfood-tracking-api:1.0.0-SNAPSHOT

>https://github.com/icarodamiani/fiap-fastfood-product
>>io.fiap.fastfood:fastfood-tracking-api:1.0.0-SNAPSHOT

>https://github.com/icarodamiani/fiap-fastfood-payment
>>io.fiap.fastfood:fastfood-payment-api:1.0.0-SNAPSHOT

>https://github.com/icarodamiani/fiap-fastfood-customer
>>io.fiap.fastfood:fastfood-customer-api:1.0.0-SNAPSHOT

## Deploy

O deploy pode ser realizado através da execução do pipeline "Deploy product" no Github Actions. 
No entanto, anteriormente a execução, faz-se necessária a configuração do ID e SECRET da AWS nos secrets do repositório. 
Como o acesso às variáveis e secrets do respositório é limitado ao owner e maintainers, recomendo a execução dos passos do script de deploy localmente com apontamento para a cloud.
Seguem abaixo os passos:

1 - 
```
./mvnw clean install -Dmaven.test.skip=true -U -P dev
```
2 - 
```
docker login registry-1.docker.io
```
3-
```
docker build . -t icarodamiani/fastfood-bff:latest
```
4 - 
```
aws eks update-kubeconfig --name {CLUSTER_NAME} --region={AWS_REGION}
```
5 - 
```
helm upgrade --install fastfood-bff charts/fastfood-bff \
--kubeconfig $HOME/.kube/config \
--set containers.image=icarodamiani/fastfood-bff \
--set image.tag=latest
```

## [Coleções Postman](fastfood-api/collection)
Ambas as coleções estão configuradas para apontar para http://localhost:8080, porém podem ser alteradas as variáveis url para que se adeque a uri/porta escolhia.

### Postman
[Collection JSON](collection%2Ffiap-fasfood.collection.json)
