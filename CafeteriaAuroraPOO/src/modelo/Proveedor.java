package modelo;

public class Proveedor {
    private int idProveedor;
    private String nombreProveedor;
    private String rucProveedor;
    private String tipoSuministro;
    private String telefono;
    private String correo;

    public Proveedor(int idProveedor, String nombreProveedor, String rucProveedor,
                     String tipoSuministro, String telefono, String correo) {
        this.idProveedor = idProveedor;
        this.nombreProveedor = nombreProveedor;
        this.rucProveedor = rucProveedor;
        this.tipoSuministro = tipoSuministro;
        this.telefono = telefono;
        this.correo = correo;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getRucProveedor() {
        return rucProveedor;
    }

    public void setRucProveedor(String rucProveedor) {
        this.rucProveedor = rucProveedor;
    }

    public String getTipoSuministro() {
        return tipoSuministro;
    }

    public void setTipoSuministro(String tipoSuministro) {
        this.tipoSuministro = tipoSuministro;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return nombreProveedor + " (" + tipoSuministro + ")";
    }
}
