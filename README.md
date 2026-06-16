# Food Store - Sistema de Gestion de Pedidos de Comida

Trabajo Practico Integrador de Programacion 2.

## Descripcion

Food Store es una aplicacion de consola desarrollada en Java, orientada a la gestion de pedidos de comida.

El sistema permite administrar categorias, productos, usuarios y pedidos mediante operaciones CRUD desde un menu interactivo.

La informacion se almacena en memoria durante la ejecucion del programa utilizando colecciones, sin conexion a base de datos.

## Requisitos

- Java 21 o superior.
- Apache NetBeans, IntelliJ IDEA o Visual Studio Code con extension de Java.
- Maven.

## Como ejecutar el proyecto

### Desde NetBeans

1. Descargar o clonar el repositorio.
2. Abrir el proyecto en NetBeans.
3. Verificar que el JDK configurado sea Java 21.
4. Ejecutar la clase principal:

```text
integrado.prog2.Main
```

### Desde consola

Desde la carpeta raiz del proyecto, ejecutar:

```bash
mvn clean compile
mvn exec:java
```

## Funcionalidades implementadas

### Categorias

- Listar categorias activas.
- Crear categorias.
- Editar categorias existentes.
- Eliminar categorias mediante baja logica.
- Validar nombre unico.
- Validar datos de entrada.

### Productos

- Listar productos activos.
- Crear productos asociados a una categoria.
- Editar productos.
- Eliminar productos mediante baja logica.
- Validar precio y stock.
- Validar nombre unico.
- Validar categoria asociada.

### Usuarios

- Listar usuarios activos.
- Crear usuarios.
- Editar usuarios.
- Eliminar usuarios mediante baja logica.
- Validar mail unico.
- Implementar relacion uno a uno entre Usuario y PerfilUsuario.

### Pedidos

- Listar pedidos activos.
- Crear pedidos con detalles.
- Agregar productos y cantidades al pedido.
- Calcular subtotales y total del pedido.
- Actualizar estado y forma de pago.
- Eliminar pedidos mediante baja logica.
- Validar stock disponible.
- Cancelar la creacion del pedido ante errores para evitar inconsistencias.

## Conceptos aplicados

- Programacion Orientada a Objetos.
- Encapsulamiento.
- Herencia.
- Polimorfismo.
- Clases abstractas.
- Interfaces.
- Colecciones.
- Enumeraciones.
- Excepciones propias.
- Baja logica.
- Separacion por capas.

## Estructura del proyecto

```text
src/main/java/integrado/prog2
├── Main.java
├── entities
├── enums
├── exceptions
├── interfaces
├── menu
└── services
```

## Clase principal

```text
integrado.prog2.Main
```

## Video demostrativo

Pendiente de carga.

## Informe PDF

Pendiente de carga.

## Repositorio

Este repositorio contiene el codigo fuente completo del proyecto Food Store.
