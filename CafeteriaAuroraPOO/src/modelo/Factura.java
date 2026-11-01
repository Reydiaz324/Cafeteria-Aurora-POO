package modelo;

public class Factura extends ComprobantePago {
    private String rucCliente;
    private String razonSocialCliente;
    private String direccionFiscal;

    public Factura(String numeroSerie, String fechaEmision, Pedido pedido,
                   String rucCliente, String razonSocialCliente, String direccionFiscal) {
        super(numeroSerie, fechaEmision, pedido);
        this.rucCliente = rucCliente != null ? rucCliente.trim() : "";
        this.razonSocialCliente = razonSocialCliente != null ? razonSocialCliente.trim() : "";
        this.direccionFiscal = direccionFiscal != null && !direccionFiscal.trim().isEmpty()
                ? direccionFiscal.trim() : "Local Central";
    }

    public String getRucCliente() {
        return rucCliente;
    }

    public void setRucCliente(String rucCliente) {
        this.rucCliente = rucCliente;
    }

    public String getRazonSocialCliente() {
        return razonSocialCliente;
    }

    public void setRazonSocialCliente(String razonSocialCliente) {
        this.razonSocialCliente = razonSocialCliente;
    }

    public String getDireccionFiscal() {
        return direccionFiscal;
    }

    public void setDireccionFiscal(String direccionFiscal) {
        this.direccionFiscal = direccionFiscal;
    }

    @Override
    public String getTipoComprobante() {
        return "FACTURA ELECTRONICA";
    }

    @Override
    public boolean validarDatosFiscales() throws DatosInvalidosException {
        if (rucCliente == null || !rucCliente.matches("(10|20)\\d{9}")) {
            throw new DatosInvalidosException("El RUC para Factura Electrónica es obligatorio y debe tener 11 dígitos válidos (iniciar con 10 o 20).");
        }
        if (razonSocialCliente == null || razonSocialCliente.trim().length() < 3) {
            throw new DatosInvalidosException("La Razón Social del cliente corporativo es obligatoria para emitir Factura.");
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
        sb.append("               FACTURA ELECTRONICA                  \n");
        sb.append("                 ").append(getNumeroSerie()).append("\n");
        sb.append("====================================================\n");
        sb.append("Fecha Emision : ").append(getFechaEmision()).append("\n");
        sb.append("Codigo Pedido : ").append(p.getCodigoPedido()).append("\n");
        sb.append("RUC Cliente   : ").append(rucCliente).append("\n");
        sb.append("Razon Social  : ").append(razonSocialCliente).append("\n");
        sb.append("Dir. Fiscal   : ").append(direccionFiscal).append("\n");
        sb.append("Atendido por  : ").append(p.getEmpleado().getNombreCompleto()).append("\n");
        sb.append("Modalidad     : ").append(p.getModalidad()).append("\n");
        sb.append("Metodo Pago   : ").append(p.getMetodoPago()).append("\n");
        sb.append("----------------------------------------------------\n");
        sb.append(String.format("%-24s %5s %9s %10s\n", "DESCRIPCION", "CANT", "V.UNIT", "IMPORTE"));
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
        if (p.calcularDescuento() > 0) {
            sb.append(String.format("Descuento Comercial (%s):         -S/ %8.2f\n",
                    p.getCodigoPromocion(), p.calcularDescuento()));
        }
        sb.append(String.format("VALOR VENTA GRAVADO (SIN IGV):        S/ %8.2f\n", calcularSubtotal()));
        sb.append(String.format("I.G.V. (18.00%%):                      S/ %8.2f\n", calcularIGV()));
        sb.append(String.format("IMPORTE TOTAL FACTURADO:              S/ %8.2f\n", calcularTotal()));
        sb.append("====================================================\n");
        sb.append("  Representacion impresa de la Factura Electronica  \n");
        sb.append("        Autorizado mediante Resolucion SUNAT        \n");
        sb.append("====================================================\n");
        return sb.toString();
    }
}
