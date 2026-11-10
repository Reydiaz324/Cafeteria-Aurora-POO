package modelo;

/**
 * CLASE ABSTRACTA BASE: Persona
 * Aplica:
 * 1. ABSTRACCIÓN: Modela los datos comunes de cualquier persona en el sistema.
 * 2. ENCAPSULAMIENTO: Atributos privados accesibles únicamente mediante getters y setters.
 * 3. HERENCIA: Sirve como clase padre para Cliente y Empleado (no se puede instanciar directamente).
 * 4. POLIMORFISMO: Define el método abstracto obtenerTipoPersona(), que cada subclase implementa a su manera.
 */
public abstract class Persona implements IExportableArchivo {
    // Atributos encapsulados (privados)
    private int id;
    private String nombres;
    private String apellidos;
    private String dni;
    private String telefono;
    private String correo;

    // Constructor base invocado por las subclases mediante super(...)
    public Persona(int id, String nombres, String apellidos, String dni, String telefono, String correo) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.telefono = telefono;
        this.correo = correo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    // Método concreto reutilizado por herencia en Cliente y Empleado
    public String getNombreCompleto() {
        return (nombres != null ? nombres : "") + " " + (apellidos != null ? apellidos : "");
    }

    // MÉTODO ABSTRACTO: Obliga a las subclases a definir cómo se identifican (Polimorfismo)
    public abstract String obtenerTipoPersona();
}
