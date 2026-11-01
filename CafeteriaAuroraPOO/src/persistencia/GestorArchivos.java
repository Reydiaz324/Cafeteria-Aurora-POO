package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import modelo.Categoria;
import modelo.Cliente;
import modelo.ComprobantePago;
import modelo.Empleado;
import modelo.HistorialPrecio;
import modelo.IExportableArchivo;
import modelo.MovimientoInventario;
import modelo.Pedido;
import modelo.Producto;
import modelo.Proveedor;

public class GestorArchivos {
    private static final String CARPETA_DATOS = "datos";
    private static final String CARPETA_COMPROBANTES = "comprobantes";
    private static final String CARPETA_REPORTES = "reportes";

    public static final String ARCHIVO_PRODUCTOS = CARPETA_DATOS + File.separator + "productos.csv";
    public static final String ARCHIVO_PEDIDOS = CARPETA_DATOS + File.separator + "pedidos.csv";
    public static final String ARCHIVO_KARDEX = CARPETA_DATOS + File.separator + "kardex.csv";
    public static final String ARCHIVO_HISTORIAL_PRECIOS = CARPETA_DATOS + File.separator + "historial_precios.csv";
    public static final String ARCHIVO_CLIENTES = CARPETA_DATOS + File.separator + "clientes.csv";
    public static final String ARCHIVO_EMPLEADOS = CARPETA_DATOS + File.separator + "empleados.csv";

    public static void asegurarDirectorios() {
        new File(CARPETA_DATOS).mkdirs();
        new File(CARPETA_COMPROBANTES).mkdirs();
        new File(CARPETA_REPORTES).mkdirs();
    }

    public static void guardarListaCSV(String rutaArchivo, String cabecera, List<? extends IExportableArchivo> elementos) throws IOException {
        asegurarDirectorios();
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(rutaArchivo, false), StandardCharsets.UTF_8)))) {
            if (cabecera != null && !cabecera.isEmpty()) {
                pw.println(cabecera);
            }
            for (IExportableArchivo item : elementos) {
                pw.println(item.aFormatoCSV());
            }
        }
    }

    public static void guardarCatalogoProductos(List<Producto> productos) throws IOException {
        guardarListaCSV(ARCHIVO_PRODUCTOS,
                "ID;NOMBRE;DESCRIPCION;PRECIO;STOCK;STOCK_MIN;ID_CATEGORIA;ID_PROVEEDOR;ESTADO",
                productos);
    }

    public static void guardarPedidos(List<Pedido> pedidos) throws IOException {
        guardarListaCSV(ARCHIVO_PEDIDOS,
                "ID_PEDIDO;CODIGO;FECHA_HORA;CLIENTE;EMPLEADO;METODO_PAGO;MODALIDAD;TOTAL;ESTADO;DETALLE_ITEMS",
                pedidos);
    }

    public static void guardarKardex(List<MovimientoInventario> movimientos) throws IOException {
        guardarListaCSV(ARCHIVO_KARDEX,
                "ID_MOV;FECHA_HORA;TIPO;PRODUCTO;CANTIDAD;STOCK_ANT;STOCK_FINAL;PROVEEDOR_ORIGEN;DOC_REF;COSTO_TOTAL;EMPLEADO;MOTIVO",
                movimientos);
    }

    public static void guardarHistorialPrecios(List<HistorialPrecio> historial) throws IOException {
        guardarListaCSV(ARCHIVO_HISTORIAL_PRECIOS,
                "ID_HIST;FECHA_HORA;ID_PROD;PRODUCTO;PRECIO_ANT;PRECIO_NUEVO;RESPONSABLE;MOTIVO",
                historial);
    }

    public static void guardarClientes(List<Cliente> clientes) throws IOException {
        guardarListaCSV(ARCHIVO_CLIENTES,
                "ID_CLIENTE;NOMBRES;APELLIDOS;DNI;TELEFONO;CORREO;TIPO_CLIENTE;RUC;RAZON_SOCIAL;PUNTOS",
                clientes);
    }

    public static void guardarEmpleados(List<Empleado> empleados) throws IOException {
        guardarListaCSV(ARCHIVO_EMPLEADOS,
                "ID_EMPLEADO;NOMBRES;APELLIDOS;DNI;CARGO;CONTRATO;SUELDO_BASE;TURNO;ESTADO",
                empleados);
    }

    public static String exportarComprobanteTXT(ComprobantePago comprobante) throws IOException {
        asegurarDirectorios();
        String prefijo = comprobante.getTipoComprobante().startsWith("FACTURA") ? "Factura_" : "Boleta_";
        String serieLimpia = comprobante.getNumeroSerie().replace("-", "_").replace(" ", "");
        File archivo = new File(CARPETA_COMPROBANTES, prefijo + serieLimpia + ".txt");

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(archivo, false), StandardCharsets.UTF_8))) {
            bw.write(comprobante.generarContenidoImprimible());
        }
        return archivo.getAbsolutePath();
    }

    public static String exportarReporteEjecutivo(String contenidoReporte) throws IOException {
        asegurarDirectorios();
        File archivo = new File(CARPETA_REPORTES, "Reporte_Ejecutivo_Aurora.txt");
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(archivo, false), StandardCharsets.UTF_8))) {
            bw.write(contenidoReporte);
        }
        return archivo.getAbsolutePath();
    }

    public static List<Producto> cargarProductosDesdeCSV(List<Categoria> categorias, List<Proveedor> proveedores) throws IOException {
        File archivo = new File(ARCHIVO_PRODUCTOS);
        List<Producto> lista = new ArrayList<>();
        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) {
                    primera = false;
                    continue;
                }
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] partes = linea.split(";");
                if (partes.length >= 9) {
                    int id = Integer.parseInt(partes[0].trim());
                    String nombre = partes[1].trim();
                    String desc = partes[2].trim();
                    double precio = Double.parseDouble(partes[3].trim());
                    int stock = Integer.parseInt(partes[4].trim());
                    int stockMin = Integer.parseInt(partes[5].trim());
                    int idCat = Integer.parseInt(partes[6].trim());
                    int idProv = Integer.parseInt(partes[7].trim());
                    String estado = partes[8].trim();

                    Categoria cat = buscarCategoria(categorias, idCat);
                    Proveedor prov = buscarProveedor(proveedores, idProv);
                    lista.add(new Producto(id, nombre, desc, precio, stock, stockMin, cat, prov, estado));
                }
            }
        }
        return lista;
    }

    public static String leerArchivoTexto(String ruta) throws IOException {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            return "El archivo solicitado aún no existe: " + archivo.getAbsolutePath();
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea).append("\n");
            }
        }
        return sb.toString();
    }

    public static List<String> listarComprobantesGenerados() {
        asegurarDirectorios();
        List<String> rutas = new ArrayList<>();
        File dir = new File(CARPETA_COMPROBANTES);
        File[] archivos = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (archivos != null) {
            for (File f : archivos) {
                rutas.add(f.getPath());
            }
        }
        return rutas;
    }

    private static Categoria buscarCategoria(List<Categoria> categorias, int id) {
        for (Categoria c : categorias) {
            if (c.getIdCategoria() == id) {
                return c;
            }
        }
        return categorias.isEmpty() ? null : categorias.get(0);
    }

    private static Proveedor buscarProveedor(List<Proveedor> proveedores, int id) {
        for (Proveedor p : proveedores) {
            if (p.getIdProveedor() == id) {
                return p;
            }
        }
        return proveedores.isEmpty() ? null : proveedores.get(0);
    }
}
