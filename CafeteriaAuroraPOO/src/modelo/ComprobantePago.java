package modelo;

import java.util.Locale;

public abstract class ComprobantePago implements ICalculable, IExportableArchivo {
    private String numeroSerie;
    private String fechaEmision;
    private Pedido pedido;

    public ComprobantePago(String numeroSerie, String fechaEmision, Pedido pedido) {
        this.numeroSerie = numeroSerie;
        this.fechaEmision = fechaEmision;
        this.pedido = pedido;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public double calcularSubtotal() {
        return pedido != null ? pedido.calcularSubtotal() : 0.0;
    }

    @Override
    public double calcularIGV() {
        return pedido != null ? pedido.calcularIGV() : 0.0;
    }

    @Override
    public double calcularTotal() {
        return pedido != null ? pedido.calcularTotal() : 0.0;
    }

    public abstract String getTipoComprobante();

    public abstract boolean validarDatosFiscales() throws DatosInvalidosException;

    public abstract String generarContenidoImprimible();

    @Override
    public String aFormatoCSV() {
        return getTipoComprobante() + ";" + numeroSerie + ";" + fechaEmision + ";"
                + (pedido != null ? pedido.getCodigoPedido() : "") + ";"
                + String.format(Locale.US, "%.2f", calcularSubtotal()) + ";"
                + String.format(Locale.US, "%.2f", calcularIGV()) + ";"
                + String.format(Locale.US, "%.2f", calcularTotal());
    }

    @Override
    public String generarResumenTexto() {
        return getTipoComprobante() + " [" + numeroSerie + "] - Total: S/ " + String.format("%.2f", calcularTotal());
    }
}
