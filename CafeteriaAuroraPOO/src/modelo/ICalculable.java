package modelo;

/**
 * INTERFAZ: ICalculable
 * 
 * ¿POR QUÉ ES UNA INTERFAZ Y NO UNA CLASE PADRE?
 * Porque clases con naturalezas distintas como 'Pedido', 'DetallePedido' y 'ComprobantePago'
 * NO tienen relación de parentesco biológico ("un Detalle NO es un Pedido").
 * La interfaz establece un CONTRATO FORMAL que las obliga a implementar los cálculos
 * monetarios sin forzar una jerarquía de herencia artificial o errónea.
 */
public interface ICalculable {
    double calcularSubtotal();
    double calcularIGV();
    double calcularTotal();
}
