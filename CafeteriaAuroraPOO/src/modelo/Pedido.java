package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Pedido implements ICalculable, IExportableArchivo {
    public static final String CUPON_OFICIAL = "AURORA10";
    public static final double PORCENTAJE_DESCUENTO = 0.10;
    public static final double TASA_IGV = 0.18;

    private int idPedido;
    private String codigoPedido;
    private String fechaHora;
    private Cliente cliente;
    private Empleado empleado;
    private String metodoPago;
    private String modalidad;
    private String codigoPromocion;
    private String estado;
    private ArrayList<DetallePedido> items;

    public Pedido(int idPedido, Cliente cliente, Empleado empleado, String metodoPago, String modalidad) {
        this.idPedido = idPedido;
        this.codigoPedido = String.format("AU-%04d", 1000 + idPedido);
        this.fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.cliente = cliente;
        this.empleado = empleado;
        this.metodoPago = metodoPago != null ? metodoPago : "Efectivo";
        this.modalidad = modalidad != null ? modalidad : "En local";
        this.codigoPromocion = "";
        this.estado = "EN_PROCESO";
        this.items = new ArrayList<>();
    }

    public void agregarItem(Producto producto, int cantidad) throws StockInsuficienteException, DatosInvalidosException {
        agregarItem(producto, cantidad, "Regular", "Caliente", "Ninguno");
    }

    public void agregarItem(Producto producto, int cantidad, String tamano, String temperatura, String extras)
            throws StockInsuficienteException, DatosInvalidosException {
        if (producto == null) {
            throw new DatosInvalidosException("Debe seleccionar un producto válido.");
        }
        if (cantidad <= 0) {
            throw new DatosInvalidosException("La cantidad solicitada debe ser mayor a cero.");
        }

        int cantidadEnCarrito = obtenerCantidadEnCarrito(producto.getIdProducto());
        if (!producto.validarDisponibilidad(cantidadEnCarrito + cantidad)) {
            throw new StockInsuficienteException(producto.getNombreProducto(),
                    producto.getStock() - cantidadEnCarrito, cantidad);
        }

        for (DetallePedido dp : items) {
            if (dp.getProducto().getIdProducto() == producto.getIdProducto()
                    && dp.getTamano().equalsIgnoreCase(tamano)
                    && dp.getTemperatura().equalsIgnoreCase(temperatura)
                    && dp.getExtras().equalsIgnoreCase(extras)) {
                dp.setCantidad(dp.getCantidad() + cantidad);
                return;
            }
        }

        items.add(new DetallePedido(producto, cantidad, tamano, temperatura, extras));
    }

    public int obtenerCantidadEnCarrito(int idProducto) {
        int suma = 0;
        for (DetallePedido dp : items) {
            if (dp.getProducto().getIdProducto() == idProducto) {
                suma += dp.getCantidad();
            }
        }
        return suma;
    }

    public void quitarItem(int indice) {
        if (indice >= 0 && indice < items.size()) {
            items.remove(indice);
        }
    }

    public void limpiarCarrito() {
        items.clear();
        codigoPromocion = "";
    }

    public boolean aplicarCupon(String cupon) {
        if (cupon != null && CUPON_OFICIAL.equalsIgnoreCase(cupon.trim())) {
            this.codigoPromocion = CUPON_OFICIAL;
            return true;
        }
        this.codigoPromocion = "";
        return false;
    }

    public double calcularBrutoItems() {
        double suma = 0.0;
        for (DetallePedido dp : items) {
            suma += dp.calcularSubtotal();
        }
        return Math.round(suma * 100.0) / 100.0;
    }

    public double calcularDescuento() {
        if (CUPON_OFICIAL.equalsIgnoreCase(codigoPromocion)) {
            return Math.round(calcularBrutoItems() * PORCENTAJE_DESCUENTO * 100.0) / 100.0;
        }
        return 0.0;
    }

    @Override
    public double calcularTotal() {
        double total = calcularBrutoItems() - calcularDescuento();
        return Math.round(Math.max(total, 0.0) * 100.0) / 100.0;
    }

    @Override
    public double calcularSubtotal() {
        double base = calcularTotal() / (1.0 + TASA_IGV);
        return Math.round(base * 100.0) / 100.0;
    }

    @Override
    public double calcularIGV() {
        double igv = calcularTotal() - calcularSubtotal();
        return Math.round(igv * 100.0) / 100.0;
    }

    public void confirmarPedido() throws StockInsuficienteException, DatosInvalidosException {
        if (items.isEmpty()) {
            throw new DatosInvalidosException("El pedido debe contener al menos un producto en el carrito.");
        }
        for (DetallePedido dp : items) {
            if (!dp.getProducto().validarDisponibilidad(dp.getCantidad())) {
                throw new StockInsuficienteException(dp.getProducto().getNombreProducto(),
                        dp.getProducto().getStock(), dp.getCantidad());
            }
        }
        for (DetallePedido dp : items) {
            dp.getProducto().descontarStock(dp.getCantidad());
        }
        if (cliente != null) {
            cliente.acumularPuntos(calcularTotal());
        }
        this.estado = "REGISTRADO";
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getCodigoPromocion() {
        return codigoPromocion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<DetallePedido> getItems() {
        return items;
    }

    @Override
    public String aFormatoCSV() {
        StringBuilder sbItems = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            DetallePedido dp = items.get(i);
            if (i > 0) sbItems.append("|");
            sbItems.append(dp.getProducto().getNombreProducto()).append("x").append(dp.getCantidad());
        }
        return idPedido + ";" + codigoPedido + ";" + fechaHora + ";"
                + (cliente != null ? cliente.getNombreCompleto() : "Cliente General") + ";"
                + (empleado != null ? empleado.getNombreCompleto() : "Barista") + ";"
                + metodoPago + ";" + modalidad + ";"
                + String.format(Locale.US, "%.2f", calcularTotal()) + ";"
                + estado + ";" + sbItems.toString();
    }

    @Override
    public String generarResumenTexto() {
        return codigoPedido + " | " + fechaHora + " | Cliente: "
                + (cliente != null ? cliente.getNombreCompleto() : "General")
                + " | Total: S/ " + String.format("%.2f", calcularTotal());
    }
}
