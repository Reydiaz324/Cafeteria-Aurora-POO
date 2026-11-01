package modelo;

public class Boleta extends ComprobantePago {
    private String dniCliente;

    public Boleta(String numeroSerie, String fechaEmision, Pedido pedido, String dniCliente) {
        super(numeroSerie, fechaEmision, pedido);
        this.dniCliente = dniCliente != null && !dniCliente.trim().isEmpty() ? dniCliente.trim() : "00000000";
    }

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    @Override
    public String getTipoComprobante() {
        return "BOLETA DE VENTA ELECTRONICA";
    }

    @Override
    public boolean validarDatosFiscales() throws DatosInvalidosException {
        if (dniCliente != null && !dniCliente.equals("00000000") && !dniCliente.matches("\\d{8}")) {
            throw new DatosInvalidosException("El DNI para la Boleta debe contener exactamente 8 dígitos numéricos.");
        }
        return true;
    }

    @Override
    public String generarContenidoImprimible() {
        Pedido p = getPedido();
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append("                 CAFETERIA AURORA                   \n");
        sb.append("              RUC: 20608945123                      \n");
        sb.append("====================================================\n");
        sb.append("            BOLETA DE VENTA ELECTRONICA             \n");
        sb.append("                 ").append(getNumeroSerie()).append("\n");
        sb.append("====================================================\n");
        sb.append("Fecha Emision : ").append(getFechaEmision()).append("\n");
        sb.append("Codigo Pedido : ").append(p.getCodigoPedido()).append("\n");
        sb.append("Cliente       : ").append(p.getCliente().getNombreCompleto()).append("\n");
        sb.append("DNI           : ").append(dniCliente).append("\n");
        sb.append("Atendido por  : ").append(p.getEmpleado().getNombreCompleto()).append("\n");
        sb.append("Metodo Pago   : ").append(p.getMetodoPago()).append("\n");
        sb.append("----------------------------------------------------\n");
        sb.append(String.format("%-24s %5s %9s %10s\n", "PRODUCTO", "CANT", "P.UNIT", "SUBTOTAL"));
        sb.append("----------------------------------------------------\n");
        for (DetallePedido dp : p.getItems()) {
            String nombre = dp.getProducto().getNombreProducto();
            if (nombre.length() > 23) {
                nombre = nombre.substring(0, 23);
            }
            sb.append(String.format("%-24s %5d %9.2f %10.2f\n",
                    nombre, dp.getCantidad(), dp.getPrecioUnitario(), dp.calcularSubtotal()));
        }
        sb.append("----------------------------------------------------\n");
        sb.append(String.format("Op. Gravada (Subtotal):               S/ %8.2f\n", calcularSubtotal()));
        sb.append(String.format("IGV (18%% Incluido):                   S/ %8.2f\n", calcularIGV()));
        sb.append(String.format("IMPORTE TOTAL A PAGAR:                S/ %8.2f\n", calcularTotal()));
        sb.append("====================================================\n");
        sb.append("   Representacion impresa de Boleta Electronica     \n");
        sb.append("        ¡Gracias por preferir Cafeteria Aurora!     \n");
        sb.append("====================================================\n");
        return sb.toString();
    }
}
