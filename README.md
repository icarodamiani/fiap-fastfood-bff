# fiap-fastfood


![img.png](challenge/img.png)

## Entregáveis

![pedido](challenge/pedido.png)
![pagamento](challenge/pagamento.png)
![acompanhamento](challenge/acompanhamento.png)
![entrega](challenge/entrega.png)
![gerenciar produtos](challenge/produtos.png)
![acompanhamento de pedidos](challenge/acompanhamento_pedidos.png)

## Recursos e Bibliotecas
- [x] Java 17
- [x] Mongo
- [x] RabbitMQ
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


## [Gerenciamento de produtos](src%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Fproduct%2FProductController.java)
A aplicação dispõe de operações que permitem a inserção, listagem(paginada) e listagem por tipo de produtos.
Não há controle de estoque implementado na versão atual da aplicação.

### Mapeamento da entidade
    id : Identificador do produto, gerado automaticamente pela base de dados
    typeId : Identificador da categoria do produto
    description : Descrição do produto
    amount : Quantidade em "estoque"
    price : Preço unitário
    type : { 
        id : Identificador da categoria do produto
        description : Descrição da categoria
    }

## Gerenciamento de Clientes
Extraído para um cognito, o gerenciamento de clientes não possui operações nesta aplicação. 
Sendo obrigatório o cadastro do usuário através da UI HOSTED do cognito e a posterior atenticação para uso dos endpoints expostos neste serviço.

## [Faturamento](src%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Fbilling%2FBillingController.java)
Apenas um esboço de aberturae fechamento de dias contábeis.

## Contador de pedidos
Durante a descoberta do produto identifiquei a necessidade de se ter um número de pedido de fácil compreensão ao cliente.
Desta forma, implementei uma sequence que é reiniciada na abertura de um novo dia de faturamento.

## [Pedido e Pagamento](src%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Forder%2FOrderController.java)
Entendendo a existência de um claro vínculo entre as duas entidades e a extrema importância das mesmas, a api foi desenvolvida para tratar ambas as informações em conjunto. 
Garantindo assim a existência mútua de ambas, onde houver um pedido haverá um pagamento a ser realizado ou já realizado. 

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
Dispõe de operações que permitem a inserção, listagem por pedido e um relatório que cumpre um dos requisitos de negócio, listando pedidos com sua data de início e tempo decorrido.

### Mapeamento da entidade da Esteira de Pedidos
    id : Identificador único da base de dados
    id do pedido : Id de referência do pedido
    numero do pedido : Número do Pedido, gerado pelo contador de pedidos
    estado do pedido : [Estado do pedido]([OrderTrackingStatusTypeDTO.java](fastfood-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Ftracking%2Fdto%2FOrderTrackingStatusTypeDTO.java))
    visibilidade : Permissão de visibilidade do estado em questão, [Permissões]([OrderTrackingRoleTypeDTO.java](fastfood-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Ftracking%2Fdto%2FOrderTrackingRoleTypeDTO.java))
    data e hora : Data e hora da inserção do estado

## [Pagamentos (Integração)]([payment-mock-api](payment-mock-api))
Os pagamentos são inseridos em conjunto ao pedido, porém processados separadamente em uma api que mocka superficialmente os comportamentos de um broker de pagamentos.

A operação de pagamento segue o seguinte fluxo:
1. O cliente finaliza o preenchimento do pedido/pagamento [na controladora de pedidos do fastfood](fastfood-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Forder%2FOrderController.java);
2. Ambas as entidades são persistidas;
3. Uma requisição REST é feita para a api de [mock de pagamentos](payment-mock-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Fpayment%2FPaymentController.java);
4. A api de [mock de pagamentos](payment-mock-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Fpayment%2FPaymentController.java) persiste o pagamento em seu lado e então o encaminha a uma fila para processamento assíncrono;
5. Cada mensagem persistida na fila é [lida](payment-mock-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fmessaging%2Fpayment%2FPaymentProcessorListener.java) e, neste mock, aprovada automáticamente;
6. Ainda no mesmo processo da fila, é feita uma chamada de volta à api de [fastfood](fastfood-api) para notificar do aceite daquele pagamento em questão;
7. Ao receber a confirmação de pagamento, a api de [fastfood](fastfood-api) registra um novo estado do pedido como 'PAYMENT_CONFIRMED'.

Um detalhe importante é que a api de [mock pagamentos](payment-mock-api) desconhece por completo a api de [fastfood](fastfood-api), sendo os passos 6 e 7 possíveis através de um campo obrigatório de callback no payload de [criação de um pagamento](payment-mock-api%2Fsrc%2Fmain%2Fjava%2Fio%2Ffiap%2Ffastfood%2Fdriver%2Fcontroller%2Fpayment%2FPaymentController.java). Simulando assim algo similar ao que ocorre na api do Mercado Pago, por exemplo.

## Fluxo da aplicação
O fluxo planejado da aplicação segue os seguintes passos:
1. Cadastro de um ou mais produtos;
2. Cadastro de um ou mais clientes (opcional);
3. Abertura de um dia de faturamento;
4. Inserção de um pedido e pagamento;
5. Confirmação de pagamento automática (Webhook);
6. Atualização do estado do pedido na Esteira de Pedidos, conforme ordem: WAITING_PAYMENT > PAYMENT_CONFIRMED > PREPARING > READY > FINISHED
7. Consulta por pedido ou através do relatório de pedidos.

## Início rápido

```shell 
docker-compose up
```
A aplicação será disponibilizada em [localhost:8080](http://localhost:8080), tendo seu swagger em [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

## Dependências
Este projeto possui depende da existência de artefatos que são gerados por outros repositórios deste projeto, segue o mapeamento:

>https://github.com/icarodamiani/fiap-fastfood-order
>>io.fiap.fastfood:fastfood-order-api:1.0.0-SNAPSHOT

>https://github.com/icarodamiani/fiap-fastfood-tracking
>>io.fiap.fastfood:fastfood-tracking-api:1.0.0-SNAPSHOT

>https://github.com/icarodamiani/fiap-fastfood-product
>>io.fiap.fastfood:fastfood-tracking-api:1.0.1-SNAPSHOT


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
[Collection JSON](collection%2Ffiap-fasfood.postman_collection.json)
