package modelo;

public class Producto implements IExportableArchivo {
    private int idProducto;
    private String nombreProducto;
    private String descripcion;
    private double precio;
    private int stock;
    private int stockMinimo;
    private Categoria categoria;
    private Proveedor proveedor;
    private String estado;

    public Producto(int idProducto, String nombreProducto, String descripcion, double precio,
                    int stock, int stockMinimo, Categoria categoria, Proveedor proveedor, String estado) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.categoria = categoria;
        this.proveedor = proveedor;
        this.estado = estado;
    }

    public Producto(int idProducto, String nombreProducto, double precio, int stock,
                    Categoria categoria, Proveedor proveedor) {
        this(idProducto, nombreProducto, "Producto de carta Cafetería Aurora", precio, stock, 10, categoria, proveedor, "Activo");
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio > 0) {
            this.precio = precio;
        }
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean validarDisponibilidad(int cantidadSolicitada) {
        return "Activo".equalsIgnoreCase(estado) && cantidadSolicitada > 0 && this.stock >= cantidadSolicitada;
    }

    public void descontarStock(int cantidad) throws StockInsuficienteException {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser mayor a cero.");
        }
        if (this.stock < cantidad) {
            throw new StockInsuficienteException(this.nombreProducto, this.stock, cantidad);
        }
        this.stock -= cantidad;
    }

    public void aumentarStock(int cantidad) throws DatosInvalidosException {
        if (cantidad <= 0) {
            throw new DatosInvalidosException("La cantidad de ingreso debe ser mayor a cero.");
        }
        this.stock += cantidad;
    }

    public boolean requiereReposicion() {
        return this.stock <= this.stockMinimo;
    }

    @Override
    public String aFormatoCSV() {
        return idProducto + ";" + nombreProducto + ";" + descripcion + ";"
                + String.format(java.util.Locale.US, "%.2f", precio) + ";" + stock + ";"
                + stockMinimo + ";" + (categoria != null ? categoria.getIdCategoria() : 1) + ";"
                + (proveedor != null ? proveedor.getIdProveedor() : 1) + ";" + estado;
    }

    @Override
    public String generarResumenTexto() {
        return String.format("[%02d] %-28s | S/ %6.2f | Stock: %3d | %s",
                idProducto, nombreProducto, precio, stock,
                categoria != null ? categoria.getNombreCategoria() : "General");
    }

    @Override
    public String toString() {
        return nombreProducto + " (S/ " + String.format("%.2f", precio) + " - Stock: " + stock + ")";
    }
}
