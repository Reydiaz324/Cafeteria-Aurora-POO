package vista;

import controlador.GestorCafeteria;

/**
 * VentanaPrincipal hereda de FormPanel para replicar fielmente
 * la interfaz de usuario en C# (separando la pantalla de Administrador
 * y Trabajador) y manteniendo compatibilidad total con la arquitectura POO.
 */
public class VentanaPrincipal extends FormPanel {
    private static final long serialVersionUID = 1L;

    public VentanaPrincipal(GestorCafeteria gestor) {
        super(gestor);
    }

    public VentanaPrincipal() {
        super();
    }
}
