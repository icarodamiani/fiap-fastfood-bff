# fiap-fastfood


![img.png](challenge/img.png)


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
