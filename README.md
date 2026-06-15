# Food Store - Sistema de Gestion de Pedidos de Comida

Trabajo Practico Integrador de Programacion 2.

## Requisitos

- Java 21 o superior.
- IDE recomendado: NetBeans, IntelliJ IDEA o VS Code con extension de Java.

## Como ejecutar desde consola

Desde la carpeta raiz del proyecto:

```bash
javac -d out $(find src -name "*.java")
java -cp out integrado.prog2.Main
```

En Windows PowerShell, una alternativa es compilar desde el IDE o ejecutar:

```powershell
Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName } > sources.txt
javac -d out @sources.txt
java -cp out integrado.prog2.Main
```

## Funcionalidades implementadas

- CRUD de Categorias.
- CRUD de Productos.
- CRUD de Usuarios.
- CRUD de Pedidos con detalles.
- Baja logica mediante el atributo `eliminado`.
- Colecciones en memoria mediante `ArrayList`.
- Herencia desde clase abstracta `Base`.
- Interface `Calculable` implementada por `Pedido`.
- Excepciones propias para datos invalidos, duplicados, entidades no encontradas y stock insuficiente.
- Relacion 1 a 1 real entre `Usuario` y `PerfilUsuario`.
- Validaciones de entrada desde menu.

## Datos iniciales

El sistema carga automaticamente:

- 2 categorias.
- 2 productos.
- 1 usuario.

Esto permite probar pedidos sin tener que cargar todo desde cero.

## Video demostrativo

Agregar aqui el enlace publico al video.

## Documentacion PDF

Agregar aqui el enlace al PDF o incluir el PDF en la raiz del repositorio.
