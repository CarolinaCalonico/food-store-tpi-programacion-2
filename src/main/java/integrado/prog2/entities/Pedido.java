package integrado.prog2.entities;

import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exceptions.DatoInvalidoException;
import integrado.prog2.exceptions.StockInsuficienteException;
import integrado.prog2.interfaces.Calculable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    private Estado estado;
    private double total;
    private FormaPago formaPago;
    private Usuario usuario;
    private List<DetallePedido> detalles;
    private long siguienteDetalleId;

    public Pedido(Long id, Usuario usuario, FormaPago formaPago) {
        super(id);
        this.fecha = LocalDate.now();
        this.estado = Estado.PENDIENTE;
        this.total = 0;
        this.formaPago = formaPago;
        this.usuario = usuario;
        this.detalles = new ArrayList<>();
        this.siguienteDetalleId = 1;
    }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public FormaPago getFormaPago() { return formaPago; }
    public void setFormaPago(FormaPago formaPago) { this.formaPago = formaPago; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public List<DetallePedido> getDetalles() { return detalles; }

    public void addDetallePedido(int cantidad, Double precioUnitario, Producto producto)
            throws DatoInvalidoException, StockInsuficienteException {
        if (producto == null || producto.isEliminado() || !producto.isDisponible()) {
            throw new DatoInvalidoException("El producto no existe, esta eliminado o no esta disponible.");
        }
        if (cantidad <= 0) {
            throw new DatoInvalidoException("La cantidad debe ser mayor a cero.");
        }
        if (precioUnitario == null || precioUnitario < 0) {
            throw new DatoInvalidoException("El precio unitario no puede ser negativo.");
        }
        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        DetallePedido existente = findeDetallePedidoByProducto(producto);
        double subtotal = cantidad * precioUnitario;

        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + cantidad);
            existente.setSubtotal(existente.getSubtotal() + subtotal);
        } else {
            DetallePedido detalle = new DetallePedido(siguienteDetalleId++, cantidad, subtotal, producto);
            detalles.add(detalle);
        }

        producto.descontarStock(cantidad);
        calcularTotal();
    }

    public DetallePedido findeDetallePedidoByProducto(Producto producto) {
        for (DetallePedido detalle : detalles) {
            if (!detalle.isEliminado() && detalle.getProducto().equals(producto)) {
                return detalle;
            }
        }
        return null;
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalle = findeDetallePedidoByProducto(producto);
        if (detalle != null) {
            detalle.setEliminado(true);
            calcularTotal();
        }
    }

    @Override
    public void calcularTotal() {
        double acumulador = 0;
        for (DetallePedido detalle : detalles) {
            if (!detalle.isEliminado()) {
                acumulador += detalle.getSubtotal();
            }
        }
        this.total = acumulador;
    }

    @Override
    public String toString() {
        String usuarioTexto = usuario != null ? usuario.getNombre() + " " + usuario.getApellido() : "Sin usuario";
        return "ID: " + getId() + " | Usuario: " + usuarioTexto + " | Estado: " + estado
                + " | Pago: " + formaPago + " | Total: $" + total + " | Fecha: " + fecha;
    }
}
