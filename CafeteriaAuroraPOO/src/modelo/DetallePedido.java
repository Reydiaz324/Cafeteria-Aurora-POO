package modelo;

public class DetallePedido {
    private Producto producto;
    private int cantidad;
    private double precioUnitario;
    private String tamano;
    private String temperatura;
    private String extras;

    public DetallePedido(Producto producto, int cantidad) {
        this(producto, cantidad, "Regular", "Caliente", "Ninguno");
    }

    public DetallePedido(Producto producto, int cantidad, String tamano, String temperatura, String extras) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.tamano = tamano != null ? tamano : "Regular";
        this.temperatura = temperatura != null ? temperatura : "Caliente";
        this.extras = extras != null ? extras : "Ninguno";
        double recargo = "Grande".equalsIgnoreCase(this.tamano) ? 2.00 : 0.00;
        if (this.extras != null && !this.extras.equalsIgnoreCase("Ninguno") && !this.extras.trim().isEmpty()) {
            recargo += 1.50;
        }
        this.precioUnitario = producto.getPrecio() + recargo;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public double calcularSubtotal() {
        return Math.round(precioUnitario * cantidad * 100.0) / 100.0;
    }

    public String getPersonalizacion() {
        return tamano + " | " + temperatura + (!"Ninguno".equalsIgnoreCase(extras) ? " | +" + extras : "");
    }
}
