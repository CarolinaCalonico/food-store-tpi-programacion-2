package integrado.prog2.services;

import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exceptions.DatoInvalidoException;
import integrado.prog2.exceptions.EntidadNoEncontradaException;
import integrado.prog2.exceptions.StockInsuficienteException;
import java.util.ArrayList;
import java.util.List;

public class PedidoService {
    private List<Pedido> pedidos;
    private long siguienteId;

    public PedidoService() {
        this.pedidos = new ArrayList<>();
        this.siguienteId = 1;
    }

    public Pedido crearPedidoVacio(Usuario usuario, FormaPago formaPago) throws DatoInvalidoException {
        if (usuario == null || usuario.isEliminado()) {
            throw new DatoInvalidoException("No se puede crear un pedido sin usuario activo.");
        }
        if (formaPago == null) {
            throw new DatoInvalidoException("La forma de pago es obligatoria.");
        }
        return new Pedido(siguienteId, usuario, formaPago);
    }

    public void agregarDetalleTemporal(Pedido pedido, int cantidad, Producto producto)
            throws DatoInvalidoException, StockInsuficienteException {
        if (pedido == null) {
            throw new DatoInvalidoException("El pedido no existe.");
        }
        pedido.addDetallePedido(cantidad, producto.getPrecio(), producto);
    }

    public Pedido confirmarCreacion(Pedido pedido) throws DatoInvalidoException {
        if (pedido == null) {
            throw new DatoInvalidoException("El pedido no existe.");
        }
        if (pedido.getDetalles().isEmpty()) {
            throw new DatoInvalidoException("No se puede guardar un pedido sin detalles.");
        }
        pedido.setId(siguienteId++);
        pedido.calcularTotal();
        pedidos.add(pedido);
        pedido.getUsuario().agregarPedido(pedido);
        return pedido;
    }

    public void cancelarCreacion(Pedido pedido) {
        if (pedido == null) {
            return;
        }
        for (var detalle : pedido.getDetalles()) {
            if (!detalle.isEliminado() && detalle.getProducto() != null) {
                Producto producto = detalle.getProducto();
                producto.setStock(producto.getStock() + detalle.getCantidad());
                detalle.setEliminado(true);
            }
        }
        pedido.setEliminado(true);
        pedido.calcularTotal();
    }

    public List<Pedido> listarActivos() {
        List<Pedido> activos = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (!pedido.isEliminado()) {
                activos.add(pedido);
            }
        }
        return activos;
    }

    public Pedido buscarActivoPorId(Long id) throws EntidadNoEncontradaException {
        for (Pedido pedido : pedidos) {
            if (pedido.getId().equals(id) && !pedido.isEliminado()) {
                return pedido;
            }
        }
        throw new EntidadNoEncontradaException("No se encontro un pedido activo con ID " + id + ".");
    }

    public void actualizarEstadoYFormaPago(Long id, Estado estado, FormaPago formaPago)
            throws EntidadNoEncontradaException, DatoInvalidoException {
        Pedido pedido = buscarActivoPorId(id);
        if (estado == null || formaPago == null) {
            throw new DatoInvalidoException("Estado y forma de pago son obligatorios.");
        }
        pedido.setEstado(estado);
        pedido.setFormaPago(formaPago);
    }

    public void eliminarLogico(Long id) throws EntidadNoEncontradaException {
        Pedido pedido = buscarActivoPorId(id);
    
        for (var detalle : pedido.getDetalles()) {
            if (!detalle.isEliminado() && detalle.getProducto() != null) {
                Producto producto = detalle.getProducto();
                producto.setStock(producto.getStock() + detalle.getCantidad());
                detalle.setEliminado(true);
            }
        }
    
        pedido.setEliminado(true);
        pedido.calcularTotal();
    }
