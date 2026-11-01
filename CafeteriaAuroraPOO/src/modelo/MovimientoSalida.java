package modelo;

public class MovimientoSalida extends MovimientoInventario {
    private String tipoSalida;

    public MovimientoSalida(int idMovimiento, Producto producto, int cantidad,
                            Empleado empleado, String motivo, String tipoSalida) {
        super(idMovimiento, producto, cantidad, empleado, motivo);
        this.tipoSalida = tipoSalida != null ? tipoSalida : "MERMA";
    }

    public String getTipoSalida() {
        return tipoSalida;
    }

    @Override
    public String getTipoMovimiento() {
        return "SALIDA_" + tipoSalida.toUpperCase();
    }

    @Override
    public void ejecutarMovimiento() throws DatosInvalidosException, StockInsuficienteException {
        if (getMotivo() == null || getMotivo().trim().isEmpty()) {
            throw new DatosInvalidosException("Debe especificar el motivo de la salida o merma en el Kardex.");
        }
        if (getCantidad() <= 0) {
            throw new DatosInvalidosException("La cantidad de salida debe ser mayor a cero.");
        }
        setStockAnterior(getProducto().getStock());
        getProducto().descontarStock(getCantidad());
        setStockResultante(getProducto().getStock());
    }

    @Override
    public String aFormatoCSV() {
        return getIdMovimiento() + ";" + getFechaHora() + ";" + getTipoMovimiento() + ";"
                + getProducto().getNombreProducto() + ";" + getCantidad() + ";"
                + getStockAnterior() + ";" + getStockResultante() + ";Interno;S/D;0.00;"
                + (getEmpleado() != null ? getEmpleado().getNombreCompleto() : "Encargado") + ";"
                + getMotivo();
    }

    @Override
    public String generarResumenTexto() {
        return "[" + getTipoMovimiento() + "] " + getProducto().getNombreProducto()
                + " (-" + getCantidad() + ") | Motivo: " + getMotivo();
    }
}
