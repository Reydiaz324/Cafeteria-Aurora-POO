package modelo;

import java.util.Locale;

public class MovimientoEntrada extends MovimientoInventario {
    private Proveedor proveedor;
    private String documentoReferencia;
    private double costoUnitario;

    public MovimientoEntrada(int idMovimiento, Producto producto, int cantidad, Empleado empleado,
                             String motivo, Proveedor proveedor, String documentoReferencia, double costoUnitario) {
        super(idMovimiento, producto, cantidad, empleado, motivo);
        this.proveedor = proveedor;
        this.documentoReferencia = documentoReferencia;
        this.costoUnitario = costoUnitario;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public String getDocumentoReferencia() {
        return documentoReferencia;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public double calcularCostoTotalLote() {
        return Math.round(getCantidad() * costoUnitario * 100.0) / 100.0;
    }

    @Override
    public String getTipoMovimiento() {
        return "ENTRADA_PROVEEDOR";
    }

    @Override
    public void ejecutarMovimiento() throws DatosInvalidosException {
        if (documentoReferencia == null || documentoReferencia.trim().isEmpty()) {
            throw new DatosInvalidosException("Debe ingresar el número de Guía o Factura del proveedor (ej. F001-0008920).");
        }
        if (getCantidad() <= 0 || costoUnitario <= 0) {
            throw new DatosInvalidosException("La cantidad recibida y el costo unitario deben ser mayores a cero.");
        }
        setStockAnterior(getProducto().getStock());
        getProducto().aumentarStock(getCantidad());
        setStockResultante(getProducto().getStock());
    }

    @Override
    public String aFormatoCSV() {
        return getIdMovimiento() + ";" + getFechaHora() + ";" + getTipoMovimiento() + ";"
                + getProducto().getNombreProducto() + ";" + getCantidad() + ";"
                + getStockAnterior() + ";" + getStockResultante() + ";"
                + (proveedor != null ? proveedor.getNombreProveedor() : "Directo") + ";"
                + documentoReferencia + ";"
                + String.format(Locale.US, "%.2f", calcularCostoTotalLote()) + ";"
                + (getEmpleado() != null ? getEmpleado().getNombreCompleto() : "Encargado") + ";"
                + getMotivo();
    }

    @Override
    public String generarResumenTexto() {
        return "[ENTRADA] " + getProducto().getNombreProducto() + " (+" + getCantidad()
                + ") | Guía: " + documentoReferencia + " | Prov: "
                + (proveedor != null ? proveedor.getNombreProveedor() : "N/A");
    }
}
