package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HistorialPrecio implements IExportableArchivo {
    private int idHistorial;
    private String fechaHora;
    private Producto producto;
    private double precioAnterior;
    private double precioNuevo;
    private String empleadoResponsable;
    private String motivo;

    public HistorialPrecio(int idHistorial, Producto producto, double precioAnterior,
                           double precioNuevo, String empleadoResponsable, String motivo) {
        this.idHistorial = idHistorial;
        this.fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.producto = producto;
        this.precioAnterior = precioAnterior;
        this.precioNuevo = precioNuevo;
        this.empleadoResponsable = empleadoResponsable;
        this.motivo = motivo != null && !motivo.trim().isEmpty() ? motivo : "Actualización comercial de carta";
    }

    public int getIdHistorial() {
        return idHistorial;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public Producto getProducto() {
        return producto;
    }

    public double getPrecioAnterior() {
        return precioAnterior;
    }

    public double getPrecioNuevo() {
        return precioNuevo;
    }

    public String getEmpleadoResponsable() {
        return empleadoResponsable;
    }

    public String getMotivo() {
        return motivo;
    }

    @Override
    public String aFormatoCSV() {
        return idHistorial + ";" + fechaHora + ";" + producto.getIdProducto() + ";"
                + producto.getNombreProducto() + ";"
                + String.format(Locale.US, "%.2f", precioAnterior) + ";"
                + String.format(Locale.US, "%.2f", precioNuevo) + ";"
                + empleadoResponsable + ";" + motivo;
    }

    @Override
    public String generarResumenTexto() {
        return fechaHora + " | " + producto.getNombreProducto() + ": S/ "
                + String.format("%.2f", precioAnterior) + " -> S/ " + String.format("%.2f", precioNuevo)
                + " (" + empleadoResponsable + ")";
    }
}
