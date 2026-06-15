package integrado.prog2.services;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exceptions.DatoInvalidoException;
import integrado.prog2.exceptions.DuplicadoException;
import integrado.prog2.exceptions.EntidadNoEncontradaException;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    private List<Producto> productos;
    private long siguienteId;

    public ProductoService() {
        this.productos = new ArrayList<>();
        this.siguienteId = 1;
    }

    public Producto crear(String nombre, double precio, String descripcion, int stock, String imagen, boolean disponible, Categoria categoria)
            throws DatoInvalidoException, DuplicadoException {
        validarProducto(nombre, precio, descripcion, stock, imagen, categoria, null);
        Producto producto = new Producto(siguienteId++, nombre.trim(), precio, descripcion.trim(), stock, imagen.trim(), disponible, categoria);
        productos.add(producto);
        categoria.agregarProducto(producto);
        return producto;
    }

    public List<Producto> listarActivos() {
        List<Producto> activos = new ArrayList<>();
        for (Producto producto : productos) {
            if (!producto.isEliminado()) {
                activos.add(producto);
            }
        }
        return activos;
    }

    public Producto buscarActivoPorId(Long id) throws EntidadNoEncontradaException {
        for (Producto producto : productos) {
            if (producto.getId().equals(id) && !producto.isEliminado()) {
                return producto;
            }
        }
        throw new EntidadNoEncontradaException("No se encontro un producto activo con ID " + id + ".");
    }

    public void editar(Long id, String nombre, double precio, String descripcion, int stock, String imagen, boolean disponible, Categoria categoria)
            throws EntidadNoEncontradaException, DatoInvalidoException, DuplicadoException {
        Producto producto = buscarActivoPorId(id);
        validarProducto(nombre, precio, descripcion, stock, imagen, categoria, id);
        producto.setNombre(nombre.trim());
        producto.setPrecio(precio);
        producto.setDescripcion(descripcion.trim());
        producto.setStock(stock);
        producto.setImagen(imagen.trim());
        producto.setDisponible(disponible);
        producto.setCategoria(categoria);
        categoria.agregarProducto(producto);
    }

    public void eliminarLogico(Long id) throws EntidadNoEncontradaException {
        Producto producto = buscarActivoPorId(id);
        producto.setEliminado(true);
        producto.setDisponible(false);
    }

    public void validarNombreDisponible(String nombre, Long idIgnorado) throws DatoInvalidoException, DuplicadoException {
        validarNombre(nombre);
        if (existeNombre(nombre, idIgnorado)) {
            if (idIgnorado == null) {
                throw new DuplicadoException("Ya existe un producto con ese nombre.");
            }
            throw new DuplicadoException("Ya existe otro producto con ese nombre.");
        }
    }

    public void validarDescripcion(String descripcion) throws DatoInvalidoException {
        validarTexto(descripcion, "La descripcion del producto es obligatoria.");
    }

    public void validarPrecio(double precio) throws DatoInvalidoException {
        if (precio < 0) {
            throw new DatoInvalidoException("El precio no puede ser negativo.");
        }
    }

    public void validarStock(int stock) throws DatoInvalidoException {
        if (stock < 0) {
            throw new DatoInvalidoException("El stock no puede ser negativo.");
        }
    }

    public void validarImagen(String imagen) throws DatoInvalidoException {
        validarTexto(imagen, "La imagen/URL del producto es obligatoria.");
    }

    private void validarProducto(String nombre, double precio, String descripcion, int stock, String imagen, Categoria categoria, Long idIgnorado)
            throws DatoInvalidoException, DuplicadoException {
        validarNombreDisponible(nombre, idIgnorado);
        validarDescripcion(descripcion);
        validarPrecio(precio);
        validarStock(stock);
        validarImagen(imagen);
        if (categoria == null || categoria.isEliminado()) {
            throw new DatoInvalidoException("La categoria no existe o esta eliminada.");
        }
    }

    private void validarNombre(String nombre) throws DatoInvalidoException {
        validarTexto(nombre, "El nombre del producto es obligatorio.");
        if (!nombre.trim().matches("[\\p{L} ]+")) {
            throw new DatoInvalidoException("El nombre del producto solo puede contener letras y espacios.");
        }
    }

    private boolean existeNombre(String nombre, Long idIgnorado) {
        for (Producto producto : productos) {
            boolean mismoNombre = producto.getNombre().equalsIgnoreCase(nombre.trim());
            boolean mismoId = idIgnorado != null && producto.getId().equals(idIgnorado);
            if (!producto.isEliminado() && mismoNombre && !mismoId) {
                return true;
            }
        }
        return false;
    }

    private void validarTexto(String texto, String mensaje) throws DatoInvalidoException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new DatoInvalidoException(mensaje);
        }
    }
}
