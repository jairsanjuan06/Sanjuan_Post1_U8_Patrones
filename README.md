# Sanjuan_Post1_U8_Patrones# Taller Post-Contenido 1 - Unidad 8
## Sistema de Pedidos con Clean Architecture

Este proyecto corresponde al taller de la **Unidad 8: Patrones Arquitectonicos II** del curso **Patrones de Diseno de Software**.

El sistema implementa un modulo de gestion de pedidos aplicando **Clean Architecture**, separando claramente los cuatro circulos:

- `Entities` (dominio puro)
- `Use Cases` (casos de uso y puertos)
- `Interface Adapters` (controllers, DTOs y adaptadores)
- `Frameworks & Drivers` (Spring Boot y JPA)

## Objetivo

Construir una API REST para pedidos con:

- entidades y value objects de dominio
- casos de uso desacoplados del framework
- puertos para persistencia
- adaptadores de entrada (web) y salida (JPA)
- wiring explicito en configuracion

## Tecnologias

- Java 17
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- H2 Database
- Validation
- Maven
- Postman

## Estructura del proyecto

```text
com.example.cleanpedidos
├── domain
│   ├── entity
│   │   └── Pedido.java
│   └── valueobject
│       ├── PedidoId.java
│       ├── LineaPedido.java
│       ├── Dinero.java
│       └── EstadoPedido.java
├── usecase
│   ├── CrearPedidoUseCase.java
│   ├── ConsultarPedidoUseCase.java
│   ├── port
│   │   └── PedidoRepositoryPort.java
│   └── impl
│       ├── CrearPedidoService.java
│       └── ConsultarPedidoService.java
├── adapter
│   ├── in/web
│   │   ├── PedidoController.java
│   │   ├── GlobalExceptionHandler.java
│   │   └── dto
│   │       ├── CrearPedidoRequest.java
│   │       ├── LineaPedidoDto.java
│   │       ├── LineaPedidoResponse.java
│   │       └── PedidoResponse.java
│   └── out/persistence
│       ├── PedidoJpaEntity.java
│       ├── PedidoJpaRepository.java
│       └── PedidoRepositoryAdapter.java
├── config
│   └── PedidoConfiguration.java
└── CleanPedidosApplication.java
```

## Reglas arquitectonicas clave

- `domain/` no importa Spring ni JPA.
- `usecase/` no importa Spring.
- DTOs solo en `adapter/in/web/dto`.
- Entidades JPA solo en `adapter/out/persistence`.
- La inyeccion de casos de uso se hace en `config/PedidoConfiguration`.
- Las dependencias del codigo apuntan hacia adentro.

## Modelo de dominio

### `Pedido` (Aggregate Root)

- controla la agregacion de lineas
- valida reglas de negocio:
- cliente obligatorio
- cantidad de linea > 0
- no confirmar pedido sin lineas
- solo agrega lineas en estado `BORRADOR`

### Value Objects

- `PedidoId`: identidad tipada con UUID
- `Dinero`: evita montos negativos y permite sumar
- `LineaPedido`: representa una linea inmutable y calcula subtotal
- `EstadoPedido`: `BORRADOR` / `CONFIRMADO`

## Flujo funcional

1. El cliente llama `POST /api/pedidos`.
2. `PedidoController` delega al caso de uso `CrearPedidoUseCase`.
3. `CrearPedidoService` crea el aggregate `Pedido`, agrega lineas y confirma.
4. El caso de uso persiste por `PedidoRepositoryPort`.
5. `PedidoRepositoryAdapter` traduce dominio <-> JPA.
6. Se retorna `pedidoId` al cliente.

## Configuracion de aplicacion

Archivo `src/main/resources/application.properties`:

```properties
spring.application.name=cleanpedidos

spring.datasource.url=jdbc:h2:mem:pedidosdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## Ejecucion

1. Compilar:

```bash
mvn clean compile
```

2. Ejecutar:

```bash
mvn spring-boot:run
```

3. Accesos:

- API: [http://localhost:8080/api/pedidos](http://localhost:8080/api/pedidos)
- H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

Datos H2:

- JDBC URL: `jdbc:h2:mem:pedidosdb`
- User: `sa`
- Password: vacio

## Endpoints

### Crear pedido

- Metodo: `POST`
- URL: `/api/pedidos`

Body de ejemplo:

```json
{
  "clienteNombre": "Ana Garcia",
  "lineas": [
    { "productoNombre": "Laptop", "cantidad": 1, "precioUnitario": 1500.00 },
    { "productoNombre": "Mouse", "cantidad": 2, "precioUnitario": 80.00 }
  ]
}
```

Respuesta esperada (`201 Created`):

```json
{
  "pedidoId": "f9cfcf5e-3d24-4f60-9acd-26835d5c6f4e"
}
```

### Consultar pedido por ID

- Metodo: `GET`
- URL: `/api/pedidos/{id}`

### Listar pedidos

- Metodo: `GET`
- URL: `/api/pedidos`

## Pruebas sugeridas en Postman

1. `POST /api/pedidos` con datos validos -> `201`.
2. `GET /api/pedidos/{id}` con el UUID retornado -> `200`.
3. `GET /api/pedidos` -> lista de pedidos con lineas y total.
4. `POST /api/pedidos` con `clienteNombre` vacio -> `400`.
5. `POST /api/pedidos` con `cantidad <= 0` -> `400`.

## Checkpoints de verificacion

- proyecto compila con `mvn clean compile`
- `domain/` sin imports de framework
- `usecase/` sin imports de Spring
- `POST /api/pedidos` retorna UUID
- `GET /api/pedidos/{id}` retorna lineas y total
- errores de dominio retornan `400`
- `Pedido` y `CrearPedidoService` son testeables sin `@SpringBootTest`



## Entregables

- repositorio publico en GitHub
- nombre del repositorio: `apellido-post1-u8`
- `README.md`
- codigo fuente Maven
- capturas de pruebas HTTP
- minimo 3 commits descriptivos

## Commits sugeridos

1. `Crear estructura base Clean Architecture para pedidos`
2. `Implementar domain y use cases del modulo de pedidos`
3. `Agregar adapters web/persistencia y wiring de configuracion`

## Autor

- Nombre: Jair Sanjuan
- Curso: Patrones de Diseno de Software
- Unidad: 8
- Actividad: Post-Contenido 1