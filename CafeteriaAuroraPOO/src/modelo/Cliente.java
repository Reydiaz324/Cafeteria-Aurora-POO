package modelo;

public class Cliente extends Persona {
    private String tipoCliente;
    private String ruc;
    private String razonSocial;
    private int puntosFidelidad;

    public Cliente(int id, String nombres, String apellidos, String dni, String telefono, String correo) {
        super(id, nombres, apellidos, dni, telefono, correo);
        this.tipoCliente = "General";
        this.ruc = "";
        this.razonSocial = "";
        this.puntosFidelidad = 0;
    }

    public Cliente(int id, String nombres, String apellidos, String dni, String telefono, String correo,
                   String tipoCliente, String ruc, String razonSocial, int puntosFidelidad) {
        super(id, nombres, apellidos, dni, telefono, correo);
        this.tipoCliente = tipoCliente;
        this.ruc = ruc != null ? ruc : "";
        this.razonSocial = razonSocial != null ? razonSocial : "";
        this.puntosFidelidad = puntosFidelidad;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public int getPuntosFidelidad() {
        return puntosFidelidad;
    }

    public void setPuntosFidelidad(int puntosFidelidad) {
        this.puntosFidelidad = puntosFidelidad;
    }

    public void acumularPuntos(double montoCompra) {
        int ganados = (int) (montoCompra / 10.0);
        if (ganados > 0) {
            this.puntosFidelidad += ganados;
        }
        if (this.puntosFidelidad >= 20 && "General".equalsIgnoreCase(this.tipoCliente)) {
            this.tipoCliente = "Frecuente";
        }
    }

    @Override
    public String obtenerTipoPersona() {
        return "Cliente (" + tipoCliente + ")";
    }

    @Override
    public String aFormatoCSV() {
        return getId() + ";" + getNombres() + ";" + getApellidos() + ";" + getDni() + ";"
                + getTelefono() + ";" + getCorreo() + ";" + tipoCliente + ";" + ruc + ";"
                + razonSocial + ";" + puntosFidelidad;
    }

    @Override
    public String generarResumenTexto() {
        return getNombreCompleto() + " [DNI: " + getDni()
                + (ruc != null && !ruc.isEmpty() ? " | RUC: " + ruc : "") + "]";
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (" + getDni() + ")";
    }
}
