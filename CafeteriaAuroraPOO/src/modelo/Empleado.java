package modelo;

public class Empleado extends Persona {
    private String cargo;
    private String tipoContrato;
    private double sueldoBase;
    private String turno;
    private String estado;
    private String claveAcceso;

    public Empleado(int id, String nombres, String apellidos, String dni, String cargo,
                    String tipoContrato, double sueldoBase, String turno, String estado, String claveAcceso) {
        super(id, nombres, apellidos, dni, "999000111", nombres.toLowerCase().replace(" ", "") + "@cafeteriaaurora.pe");
        this.cargo = cargo;
        this.tipoContrato = tipoContrato;
        this.sueldoBase = sueldoBase;
        this.turno = turno;
        this.estado = estado;
        this.claveAcceso = claveAcceso;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public double getSueldoBase() {
        return sueldoBase;
    }

    public void setSueldoBase(double sueldoBase) {
        this.sueldoBase = sueldoBase;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public boolean esEncargado() {
        return "Encargado".equalsIgnoreCase(cargo);
    }

    public double calcularBonificacionTurno() {
        double bono = "Encargado".equalsIgnoreCase(cargo) ? 250.0 : 120.0;
        if ("Tarde".equalsIgnoreCase(turno)) {
            bono += 50.0;
        }
        return bono;
    }

    public double calcularSueldoTotal() {
        return sueldoBase + calcularBonificacionTurno();
    }

    @Override
    public String obtenerTipoPersona() {
        return "Empleado - " + cargo + " (" + turno + ")";
    }

    @Override
    public String aFormatoCSV() {
        return getId() + ";" + getNombres() + ";" + getApellidos() + ";" + getDni() + ";"
                + cargo + ";" + tipoContrato + ";" + String.format(java.util.Locale.US, "%.2f", sueldoBase)
                + ";" + turno + ";" + estado;
    }

    @Override
    public String generarResumenTexto() {
        return getNombreCompleto() + " - " + cargo + " (" + turno + ")";
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " - " + cargo;
    }
}
