package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class MovimientoInventario implements IExportableArchivo {
    private int idMovimiento;
    private String fechaHora;
    private Producto producto;
    private int cantidad;
    private int stockAnterior;
    private int stockResultante;
    private Empleado empleado;
    private String motivo;

    public MovimientoInventario(int idMovimiento, Producto producto, int cantidad,
                                Empleado empleado, String motivo) {
        this.idMovimiento = idMovimiento;
        this.fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.producto = producto;
        this.cantidad = cantidad;
        this.stockAnterior = producto != null ? producto.getStock() : 0;
        this.stockResultante = this.stockAnterior;
        this.empleado = empleado;
        this.motivo = motivo;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getStockAnterior() {
        return stockAnterior;
    }

    protected void setStockAnterior(int stockAnterior) {
        this.stockAnterior = stockAnterior;
    }

    public int getStockResultante() {
        return stockResultante;
    }

    protected void setStockResultante(int stockResultante) {
        this.stockResultante = stockResultante;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public String getMotivo() {
        return motivo;
    }

    public abstract String getTipoMovimiento();

    public abstract void ejecutarMovimiento() throws DatosInvalidosException, StockInsuficienteException;
}
