package integrado.prog2.menu;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.enums.Rol;
import integrado.prog2.exceptions.DatoInvalidoException;
import integrado.prog2.exceptions.DuplicadoException;
import integrado.prog2.exceptions.EntidadNoEncontradaException;
import integrado.prog2.exceptions.StockInsuficienteException;
import integrado.prog2.services.CategoriaService;
import integrado.prog2.services.PedidoService;
import integrado.prog2.services.ProductoService;
import integrado.prog2.services.UsuarioService;
import java.util.List;
import java.util.Scanner;

public class AppMenu {
    private final Scanner scanner;
    private final CategoriaService categoriaService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final PedidoService pedidoService;

    public AppMenu() {
        this.scanner = new Scanner(System.in);
        this.categoriaService = new CategoriaService();
        this.productoService = new ProductoService();
        this.usuarioService = new UsuarioService();
        this.pedidoService = new PedidoService();
        cargarDatosIniciales();
    }

    public void iniciar() {
        int opcion;
        do {
            System.out.println("\n=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
            System.out.println("1. Categorias");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");
            opcion = leerEntero("Seleccione: ");

            switch (opcion) {
                case 1 -> menuCategorias();
                case 2 -> menuProductos();
                case 3 -> menuUsuarios();
                case 4 -> menuPedidos();
                case 0 -> System.out.println("Programa finalizado.");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void menuCategorias() {
        int opcion;
        do {
            System.out.println("\n--- CATEGORIAS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione: ");

            switch (opcion) {
                case 1 -> listarCategorias();
                case 2 -> crearCategoria();
                case 3 -> editarCategoria();
                case 4 -> eliminarCategoria();
                case 0 -> {}
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void listarCategorias() {
        List<Categoria> categorias = categoriaService.listarActivas();
        if (categorias.isEmpty()) {
            System.out.println("No hay categorias cargadas.");
            return;
        }
        categorias.forEach(System.out::println);
    }

    private void crearCategoria() {
        try {
            String nombre = leerTexto("Nombre: ");
            categoriaService.validarNombreDisponible(nombre, null);
            String descripcion = leerTexto("Descripcion: ");
            Categoria categoria = categoriaService.crear(nombre, descripcion);
            System.out.println("Categoria creada correctamente. ID generado: " + categoria.getId());
        } catch (DatoInvalidoException | DuplicadoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editarCategoria() {
        try {
            listarCategorias();
            Long id = leerLong("ID de categoria a editar: ");
            Categoria categoria = categoriaService.buscarActivaPorId(id);
            String nombre = leerTexto("Nuevo nombre: ");
            categoriaService.validarNombreDisponible(nombre, categoria.getId());
            String descripcion = leerTexto("Nueva descripcion: ");
            categoriaService.editar(categoria.getId(), nombre, descripcion);
            System.out.println("Categoria actualizada correctamente.");
        } catch (EntidadNoEncontradaException | DatoInvalidoException | DuplicadoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarCategoria() {
        try {
            listarCategorias();
            Long id = leerLong("ID de categoria a eliminar: ");
            categoriaService.validarEliminacion(id);
            if (confirmar("Confirma la baja logica? (S/N): ")) {
                categoriaService.eliminarLogico(id);
                System.out.println("Categoria eliminada logicamente.");
            } else {
                System.out.println("Operacion cancelada.");
            }
        } catch (EntidadNoEncontradaException | DatoInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void menuProductos() {
        int opcion;
        do {
            System.out.println("\n--- PRODUCTOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione: ");

            switch (opcion) {
                case 1 -> listarProductos();
                case 2 -> crearProducto();
                case 3 -> editarProducto();
                case 4 -> eliminarProducto();
                case 0 -> {}
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void listarProductos() {
        List<Producto> productos = productoService.listarActivos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos cargados.");
            return;
        }
        productos.forEach(System.out::println);
    }

    private void crearProducto() {
        try {
            String nombre = leerTexto("Nombre: ");
            productoService.validarNombreDisponible(nombre, null);
            String descripcion = leerTexto("Descripcion: ");
            productoService.validarDescripcion(descripcion);
            double precio = leerDouble("Precio: ");
            productoService.validarPrecio(precio);
            int stock = leerEntero("Stock: ");
            productoService.validarStock(stock);
            String imagen = leerTexto("Imagen/URL: ");
            productoService.validarImagen(imagen);
            boolean disponible = confirmar("Esta disponible? (S/N): ");
            listarCategorias();
            Long categoriaId = leerLong("ID de categoria: ");
            Categoria categoria = categoriaService.buscarActivaPorId(categoriaId);
            Producto producto = productoService.crear(nombre, precio, descripcion, stock, imagen, disponible, categoria);
            System.out.println("Producto creado correctamente. ID generado: " + producto.getId());
        } catch (EntidadNoEncontradaException | DatoInvalidoException | DuplicadoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editarProducto() {
        try {
            listarProductos();
            Long id = leerLong("ID de producto a editar: ");
            Producto productoActual = productoService.buscarActivoPorId(id);
            String nombre = leerTexto("Nuevo nombre: ");
            productoService.validarNombreDisponible(nombre, productoActual.getId());
            String descripcion = leerTexto("Nueva descripcion: ");
            productoService.validarDescripcion(descripcion);
            double precio = leerDouble("Nuevo precio: ");
            productoService.validarPrecio(precio);
            int stock = leerEntero("Nuevo stock: ");
            productoService.validarStock(stock);
            String imagen = leerTexto("Nueva imagen/URL: ");
            productoService.validarImagen(imagen);
            boolean disponible = confirmar("Esta disponible? (S/N): ");
            listarCategorias();
            Long categoriaId = leerLong("Nuevo ID de categoria: ");
            Categoria categoria = categoriaService.buscarActivaPorId(categoriaId);
            productoService.editar(productoActual.getId(), nombre, precio, descripcion, stock, imagen, disponible, categoria);
            System.out.println("Producto actualizado correctamente.");
        } catch (EntidadNoEncontradaException | DatoInvalidoException | DuplicadoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarProducto() {
        try {
            listarProductos();
            Long id = leerLong("ID de producto a eliminar: ");
            productoService.buscarActivoPorId(id);
            if (confirmar("Confirma la baja logica? (S/N): ")) {
                productoService.eliminarLogico(id);
                System.out.println("Producto eliminado logicamente.");
            } else {
                System.out.println("Operacion cancelada.");
            }
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void menuUsuarios() {
        int opcion;
        do {
            System.out.println("\n--- USUARIOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione: ");

            switch (opcion) {
                case 1 -> listarUsuarios();
                case 2 -> crearUsuario();
                case 3 -> editarUsuario();
                case 4 -> eliminarUsuario();
                case 0 -> {}
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarActivos();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios cargados.");
            return;
        }
        usuarios.forEach(System.out::println);
    }

    private void crearUsuario() {
        try {
            String nombre = leerTexto("Nombre: ");
            usuarioService.validarNombre(nombre);
            String apellido = leerTexto("Apellido: ");
            usuarioService.validarApellido(apellido);
            String mail = leerTexto("Mail: ");
            usuarioService.validarMailDisponible(mail, null);
            String celular = leerTexto("Celular: ");
            usuarioService.validarCelular(celular);
            String contrasena = leerTexto("Contrasena: ");
            usuarioService.validarContrasena(contrasena);
            Rol rol = seleccionarRol();
            String direccion = leerTexto("Direccion del perfil: ");
            usuarioService.validarDireccion(direccion);
            String observaciones = leerTexto("Observaciones del perfil: ");
            Usuario usuario = usuarioService.crear(nombre, apellido, mail, celular, contrasena, rol, direccion, observaciones);
            System.out.println("Usuario creado correctamente. ID generado: " + usuario.getId());
        } catch (DatoInvalidoException | DuplicadoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editarUsuario() {
        try {
            listarUsuarios();
            Long id = leerLong("ID de usuario a editar: ");
            Usuario usuarioActual = usuarioService.buscarActivoPorId(id);
            String nombre = leerTexto("Nuevo nombre: ");
            usuarioService.validarNombre(nombre);
            String apellido = leerTexto("Nuevo apellido: ");
            usuarioService.validarApellido(apellido);
            String mail = leerTexto("Nuevo mail: ");
            usuarioService.validarMailDisponible(mail, usuarioActual.getId());
            String celular = leerTexto("Nuevo celular: ");
            usuarioService.validarCelular(celular);
            String contrasena = leerTexto("Nueva contrasena: ");
            usuarioService.validarContrasena(contrasena);
            Rol rol = seleccionarRol();
            String direccion = leerTexto("Nueva direccion del perfil: ");
            usuarioService.validarDireccion(direccion);
            String observaciones = leerTexto("Nuevas observaciones del perfil: ");
            usuarioService.editar(usuarioActual.getId(), nombre, apellido, mail, celular, contrasena, rol, direccion, observaciones);
            System.out.println("Usuario actualizado correctamente.");
        } catch (EntidadNoEncontradaException | DatoInvalidoException | DuplicadoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        try {
            listarUsuarios();
            Long id = leerLong("ID de usuario a eliminar: ");
            usuarioService.buscarActivoPorId(id);
            if (confirmar("Confirma la baja logica? (S/N): ")) {
                usuarioService.eliminarLogico(id);
                System.out.println("Usuario eliminado logicamente.");
            } else {
                System.out.println("Operacion cancelada.");
            }
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void menuPedidos() {
        int opcion;
        do {
            System.out.println("\n--- PEDIDOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear pedido con detalles");
            System.out.println("3. Actualizar estado/forma de pago");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione: ");

            switch (opcion) {
                case 1 -> listarPedidos();
                case 2 -> crearPedido();
                case 3 -> actualizarPedido();
                case 4 -> eliminarPedido();
                case 0 -> {}
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void listarPedidos() {
        List<Pedido> pedidos = pedidoService.listarActivos();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos cargados.");
            return;
        }
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
            for (DetallePedido detalle : pedido.getDetalles()) {
                if (!detalle.isEliminado()) {
                    System.out.println("   - " + detalle);
                }
            }
        }
    }

    private void crearPedido() {
        Pedido pedido = null;
        try {
            listarUsuarios();
            Long usuarioId = leerLong("ID de usuario: ");
            Usuario usuario = usuarioService.buscarActivoPorId(usuarioId);
            FormaPago formaPago = seleccionarFormaPago();
            pedido = pedidoService.crearPedidoVacio(usuario, formaPago);

            boolean seguir;
            do {
                listarProductos();
                Long productoId = leerLong("ID de producto: ");
                Producto producto = productoService.buscarActivoPorId(productoId);
                int cantidad = leerEntero("Cantidad: ");
                pedidoService.agregarDetalleTemporal(pedido, cantidad, producto);
                System.out.println("Detalle agregado correctamente.");
                seguir = confirmar("Agregar otro producto? (S/N): ");
            } while (seguir);

            Pedido guardado = pedidoService.confirmarCreacion(pedido);
            System.out.println("Pedido creado correctamente. ID generado: " + guardado.getId() + " | Total: $" + guardado.getTotal());
        } catch (EntidadNoEncontradaException | DatoInvalidoException | StockInsuficienteException e) {
            pedidoService.cancelarCreacion(pedido);
            System.out.println("Error: " + e.getMessage());
            System.out.println("La creacion del pedido fue cancelada para evitar datos inconsistentes.");
        }
    }

    private void actualizarPedido() {
        try {
            listarPedidos();
            Long id = leerLong("ID de pedido a actualizar: ");
            pedidoService.buscarActivoPorId(id);
            Estado estado = seleccionarEstado();
            FormaPago formaPago = seleccionarFormaPago();
            pedidoService.actualizarEstadoYFormaPago(id, estado, formaPago);
            System.out.println("Pedido actualizado correctamente.");
        } catch (EntidadNoEncontradaException | DatoInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarPedido() {
        try {
            listarPedidos();
            Long id = leerLong("ID de pedido a eliminar: ");
            pedidoService.buscarActivoPorId(id);
            if (confirmar("Confirma la baja logica? (S/N): ")) {
                pedidoService.eliminarLogico(id);
                System.out.println("Pedido eliminado logicamente.");
            } else {
                System.out.println("Operacion cancelada.");
            }
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Rol seleccionarRol() {
        int opcion;
        do {
            System.out.println("1. ADMIN");
            System.out.println("2. USUARIO");
            opcion = leerEntero("Seleccione rol: ");
        } while (opcion < 1 || opcion > 2);
        return opcion == 1 ? Rol.ADMIN : Rol.USUARIO;
    }

    private Estado seleccionarEstado() {
        int opcion;
        do {
            System.out.println("1. PENDIENTE");
            System.out.println("2. CONFIRMADO");
            System.out.println("3. TERMINADO");
            System.out.println("4. CANCELADO");
            opcion = leerEntero("Seleccione estado: ");
        } while (opcion < 1 || opcion > 4);
        return Estado.values()[opcion - 1];
    }

    private FormaPago seleccionarFormaPago() {
        int opcion;
        do {
            System.out.println("1. TARJETA");
            System.out.println("2. TRANSFERENCIA");
            System.out.println("3. EFECTIVO");
            opcion = leerEntero("Seleccione forma de pago: ");
        } while (opcion < 1 || opcion > 3);
        return FormaPago.values()[opcion - 1];
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero valido.");
            }
        }
    }

    private Long leerLong(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un ID numerico valido.");
            }
        }
    }

    private double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero decimal valido.");
            }
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    private boolean confirmar(String mensaje) {
        String respuesta;
        do {
            System.out.print(mensaje);
            respuesta = scanner.nextLine().trim().toUpperCase();
        } while (!respuesta.equals("S") && !respuesta.equals("N"));
        return respuesta.equals("S");
    }

    private void cargarDatosIniciales() {
        try {
            Categoria hamburguesas = categoriaService.crear("Hamburguesas", "Comidas rapidas con pan y medallon");
            Categoria bebidas = categoriaService.crear("Bebidas", "Gaseosas, aguas y jugos");
            productoService.crear("Hamburguesa clasica", 4500, "Hamburguesa con queso", 10, "hamburguesa.jpg", true, hamburguesas);
            productoService.crear("Agua mineral", 1200, "Botella 500 ml", 20, "agua.jpg", true, bebidas);
            usuarioService.crear("Ana", "Gomez", "ana@mail.com", "1122334455", "1234", Rol.USUARIO, "Av. Siempre Viva 123", "Cliente frecuente");
        } catch (DatoInvalidoException | DuplicadoException e) {
            System.out.println("No se pudieron cargar los datos iniciales: " + e.getMessage());
        }
    }
}
