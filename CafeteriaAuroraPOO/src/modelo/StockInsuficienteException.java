package modelo;

public class StockInsuficienteException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String nombreProducto;
    private final int stockDisponible;
    private final int cantidadSolicitada;

    public StockInsuficienteException(String nombreProducto, int stockDisponible, int cantidadSolicitada) {
        super("Stock insuficiente para '" + nombreProducto + "'. Disponible: " + stockDisponible + ", Solicitado: " + cantidadSolicitada);
        this.nombreProducto = nombreProducto;
        this.stockDisponible = stockDisponible;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }
}
