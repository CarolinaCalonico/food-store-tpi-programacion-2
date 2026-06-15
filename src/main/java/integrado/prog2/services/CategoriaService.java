package integrado.prog2.services;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exceptions.DatoInvalidoException;
import integrado.prog2.exceptions.DuplicadoException;
import integrado.prog2.exceptions.EntidadNoEncontradaException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaService {
    private List<Categoria> categorias;
    private long siguienteId;

    public CategoriaService() {
        this.categorias = new ArrayList<>();
        this.siguienteId = 1;
    }

    public Categoria crear(String nombre, String descripcion) throws DatoInvalidoException, DuplicadoException {
        validarNombreDisponible(nombre, null);
        validarTexto(descripcion, "La descripcion de la categoria es obligatoria.");
        Categoria categoria = new Categoria(siguienteId++, nombre.trim(), descripcion.trim());
        categorias.add(categoria);
        return categoria;
    }

    public List<Categoria> listarActivas() {
        List<Categoria> activas = new ArrayList<>();
        for (Categoria categoria : categorias) {
            if (!categoria.isEliminado()) {
                activas.add(categoria);
            }
        }
        return activas;
    }

    public Categoria buscarActivaPorId(Long id) throws EntidadNoEncontradaException {
        for (Categoria categoria : categorias) {
            if (categoria.getId().equals(id) && !categoria.isEliminado()) {
                return categoria;
            }
        }
        throw new EntidadNoEncontradaException("No se encontro una categoria activa con ID " + id + ".");
    }

    public void editar(Long id, String nombre, String descripcion)
            throws EntidadNoEncontradaException, DatoInvalidoException, DuplicadoException {
        Categoria categoria = buscarActivaPorId(id);
        validarNombreDisponible(nombre, id);
        validarTexto(descripcion, "La descripcion no puede quedar vacia.");
        categoria.setNombre(nombre.trim());
        categoria.setDescripcion(descripcion.trim());
    }

    public void validarNombreDisponible(String nombre, Long idIgnorado) throws DatoInvalidoException, DuplicadoException {
        validarNombre(nombre);
        if (existeNombre(nombre, idIgnorado)) {
            if (idIgnorado == null) {
                throw new DuplicadoException("Ya existe una categoria con ese nombre.");
            }
            throw new DuplicadoException("Ya existe otra categoria con ese nombre.");
        }
    }

    public void validarEliminacion(Long id) throws EntidadNoEncontradaException, DatoInvalidoException {
        Categoria categoria = buscarActivaPorId(id);
        for (Producto producto : categoria.getProductos()) {
            if (!producto.isEliminado()) {
                throw new DatoInvalidoException("No se puede eliminar la categoria porque tiene productos activos asociados.");
            }
        }
    }

    public void eliminarLogico(Long id) throws EntidadNoEncontradaException, DatoInvalidoException {
        Categoria categoria = buscarActivaPorId(id);
        validarEliminacion(id);
        categoria.setEliminado(true);
    }

    private boolean existeNombre(String nombre, Long idIgnorado) {
        for (Categoria categoria : categorias) {
            boolean mismoNombre = categoria.getNombre().equalsIgnoreCase(nombre.trim());
            boolean mismoId = idIgnorado != null && categoria.getId().equals(idIgnorado);
            if (!categoria.isEliminado() && mismoNombre && !mismoId) {
                return true;
            }
        }
        return false;
    }

    private void validarNombre(String nombre) throws DatoInvalidoException {
        validarTexto(nombre, "El nombre de la categoria es obligatorio.");
        if (!nombre.trim().matches("[\\p{L} ]+")) {
            throw new DatoInvalidoException("El nombre de la categoria solo puede contener letras y espacios.");
        }
    }

    private void validarTexto(String texto, String mensaje) throws DatoInvalidoException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new DatoInvalidoException(mensaje);
        }
    }
}
