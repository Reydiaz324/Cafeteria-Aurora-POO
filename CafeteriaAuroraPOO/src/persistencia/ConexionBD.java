package persistencia;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import modelo.Empleado;
import modelo.Pedido;
import modelo.Producto;

/**
 * Gestor de Conexión y Sincronización con la Base de Datos SQL Server BD_Cafeteria_POO.
 * Soporta ejecución directa contra .\SQLEXPRESS mediante autenticación de Windows,
 * con consultas de ventas mensuales (5600+ pedidos) y respaldo automático a POO.
 */
public class ConexionBD {

    private static final String SERVIDOR = ".\\SQLEXPRESS";
    private static final String BASE_DATOS = "BD_Cafeteria_POO";
    private static Boolean conexionDisponible = null;

    /**
     * VERIFICACIÓN DE CONECTIVIDAD Y TOLERANCIA A FALLOS (HEARTBEAT):
     * 1. 'synchronized': Asegura que múltiples hilos no compitan al evaluar la conexión.
     * 2. 'SELECT 1': Consulta ultra rápida para saber si SQL Server está respondiendo.
     * 3. Si la base de datos no está disponible, atrapa la excepción y retorna 'false'
     *    sin tumbar la aplicación, activando el modo offline de contingencia con CSVs.
     */
    public static synchronized boolean hayConexion() {
        if (conexionDisponible != null) {
            return conexionDisponible; // Caché del estado para no saturar con consultas repetitivas
        }
        try {
            List<String> lineas = ejecutarSqlcmd("SET NOCOUNT ON; SELECT 1;");
            conexionDisponible = (lineas != null && !lineas.isEmpty() && lineas.get(0).trim().equals("1"));
        } catch (Exception e) {
            conexionDisponible = false; // Entra en modo offline si hay error
        }
        return conexionDisponible;
    }

    /**
     * EJECUCIÓN DIRECTA DE COMANDOS SQL MEDIANTE PROCESSBUILDER Y SQLCMD:
     * - Autenticación integrada de Windows (-E) y certificado confiable (-C).
     * - Evita problemas de configuración de DLLs nativas de JDBC en laboratorios universitarios.
     */
    public static List<String> ejecutarSqlcmd(String sql) {
        List<String> lineas = new ArrayList<>();
        try {
            ProcessBuilder pb = new ProcessBuilder(
                "sqlcmd", "-S", SERVIDOR, "-E", "-C", "-d", BASE_DATOS,
                "-Q", sql,
                "-s", ";", "-W", "-h", "-1"
            );
            pb.redirectErrorStream(true);
            Process p = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    linea = linea.trim();
                    if (!linea.isEmpty() && !linea.startsWith("Msg ") && !linea.startsWith("---")) {
                        lineas.add(linea);
                    }
                }
            }
            int exitCode = p.waitFor();
            if (exitCode != 0) {
                return null;
            }
            return lineas;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Sincroniza el catálogo de productos y empleados desde BD_Cafeteria_POO.
     */
    public static boolean sincronizarDesdeBD(List<Producto> productos, List<Empleado> empleados) {
        if (!hayConexion()) {
            return false;
        }
        try {
            // Sincronizar Stock de Productos desde BD
            List<String> prods = ejecutarSqlcmd("SET NOCOUNT ON; SELECT id_producto, stock FROM PRODUCTO;");
            if (prods != null && !prods.isEmpty()) {
                for (String lin : prods) {
                    String[] partes = lin.split(";");
                    if (partes.length >= 2) {
                        try {
                            int id = Integer.parseInt(partes[0].trim());
                            int stock = Integer.parseInt(partes[1].trim());
                            for (Producto p : productos) {
                                if (p.getIdProducto() == id) {
                                    p.setStock(stock);
                                    break;
                                }
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene el resumen de ventas acumuladas por mes o global en BD_Cafeteria_POO.
     * Retorna array: [totalVendido, cantidadPedidos, ticketPromedio]
     */
    public static double[] obtenerResumenVentasBD(int mes, int anio) {
        if (!hayConexion()) return null;
        try {
            String sql;
            if (mes > 0) {
                sql = String.format("SET NOCOUNT ON; SELECT COUNT(*), ISNULL(SUM(total), 0), ISNULL(AVG(total), 0) " +
                                    "FROM PEDIDO WHERE YEAR(fecha_pedido) = %d AND MONTH(fecha_pedido) = %d;", anio, mes);
            } else {
                sql = "SET NOCOUNT ON; SELECT COUNT(*), ISNULL(SUM(total), 0), ISNULL(AVG(total), 0) FROM PEDIDO;";
            }

            List<String> res = ejecutarSqlcmd(sql);
            if (res != null && !res.isEmpty()) {
                String[] p = res.get(0).split(";");
                if (p.length >= 3) {
                    double cant = Double.parseDouble(p[0].trim());
                    double tot = Double.parseDouble(p[1].trim());
                    double avg = Double.parseDouble(p[2].trim());
                    return new double[]{tot, cant, avg};
                }
            }
        } catch (Exception ex) {
            System.err.println("ERROR obtenerVentasMetodoPagoBD: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Obtiene los productos más vendidos desde BD_Cafeteria_POO (filtrable por mes o global).
     */
    public static List<Object[]> obtenerProductosMasVendidosBD(int mes, int anio) {
        if (!hayConexion()) return null;
        List<Object[]> lista = new ArrayList<>();
        try {
            String sql;
            if (mes > 0) {
                sql = String.format("SET NOCOUNT ON; SELECT TOP 15 p.nombre_producto, SUM(dp.cantidad) AS cant, SUM(dp.subtotal) AS total " +
                                    "FROM DETALLE_PEDIDO dp INNER JOIN PEDIDO ped ON dp.id_pedido = ped.id_pedido " +
                                    "INNER JOIN PRODUCTO p ON dp.id_producto = p.id_producto " +
                                    "WHERE YEAR(ped.fecha_pedido) = %d AND MONTH(ped.fecha_pedido) = %d " +
                                    "GROUP BY p.nombre_producto ORDER BY cant DESC;", anio, mes);
            } else {
                sql = "SET NOCOUNT ON; SELECT TOP 15 p.nombre_producto, SUM(dp.cantidad) AS cant, SUM(dp.subtotal) AS total " +
                      "FROM DETALLE_PEDIDO dp INNER JOIN PRODUCTO p ON dp.id_producto = p.id_producto " +
                      "GROUP BY p.nombre_producto ORDER BY cant DESC;";
            }

            List<String> filas = ejecutarSqlcmd(sql);
            if (filas != null) {
                for (String f : filas) {
                    String[] p = f.split(";");
                    if (p.length >= 3) {
                        String prod = p[0].trim();
                        int cant = Integer.parseInt(p[1].trim());
                        double tot = Double.parseDouble(p[2].trim());
                        lista.add(new Object[]{prod, cant, tot});
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("ERROR obtenerVentasMetodoPagoBD: " + ex.getMessage());
            ex.printStackTrace();
        }
        return lista;
    }

    /**
     * Obtiene las ventas agrupadas por mes desde BD_Cafeteria_POO para el desglose mensual histórico.
     * Retorna filas: {Mes / Año, Cantidad de Pedidos, Total Vendido, Ticket Promedio}
     */
    public static List<Object[]> obtenerVentasPorMesBD() {
        if (!hayConexion()) return null;
        List<Object[]> lista = new ArrayList<>();
        try {
            String sql = "SET NOCOUNT ON; " +
                         "SELECT " +
                         "    CASE MONTH(fecha_pedido) " +
                         "        WHEN 1 THEN 'Enero 2026' " +
                         "        WHEN 2 THEN 'Febrero 2026' " +
                         "        WHEN 3 THEN 'Marzo 2026' " +
                         "        WHEN 4 THEN 'Abril 2026' " +
                         "        WHEN 5 THEN 'Mayo 2026' " +
                         "        WHEN 6 THEN 'Junio 2026' " +
                         "        WHEN 7 THEN 'Julio 2026' " +
                         "        WHEN 8 THEN 'Agosto 2026' " +
                         "        WHEN 9 THEN 'Septiembre 2026' " +
                         "        WHEN 10 THEN 'Octubre 2026' " +
                         "        WHEN 11 THEN 'Noviembre 2026' " +
                         "        WHEN 12 THEN 'Diciembre 2026' " +
                         "    END AS mes, " +
                         "    COUNT(*) AS cant, " +
                         "    SUM(total) AS total, " +
                         "    AVG(total) AS ticket " +
                         "FROM PEDIDO " +
                         "GROUP BY YEAR(fecha_pedido), MONTH(fecha_pedido) " +
                         "ORDER BY YEAR(fecha_pedido) DESC, MONTH(fecha_pedido) DESC;";
            List<String> filas = ejecutarSqlcmd(sql);
            if (filas != null) {
                for (String f : filas) {
                    String[] p = f.split(";");
                    if (p.length >= 4) {
                        String mes = p[0].trim();
                        int cant = Integer.parseInt(p[1].trim());
                        double tot = Double.parseDouble(p[2].trim());
                        double ticket = Double.parseDouble(p[3].trim());
                        lista.add(new Object[]{mes, cant, tot, ticket});
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("ERROR obtenerVentasMetodoPagoBD: " + ex.getMessage());
            ex.printStackTrace();
        }
        return lista;
    }

    /**
     * Obtiene las ventas por método de pago desde BD_Cafeteria_POO (filtrable por mes).
     */
    public static List<Object[]> obtenerVentasMetodoPagoBD(int mes, int anio) {
        if (!hayConexion()) return null;
        List<Object[]> lista = new ArrayList<>();
        try {
            String sql;
            String exprMetodo = "CASE WHEN metodo_pago LIKE 'Tarjeta%' THEN 'Tarjeta' ELSE metodo_pago END";
            if (mes > 0) {
                sql = "SET NOCOUNT ON; SELECT " + exprMetodo + ", COUNT(*), SUM(total) " +
                      "FROM PEDIDO WHERE YEAR(fecha_pedido) = " + anio + " AND MONTH(fecha_pedido) = " + mes + " " +
                      "GROUP BY " + exprMetodo + " ORDER BY SUM(total) DESC;";
            } else {
                sql = "SET NOCOUNT ON; SELECT " + exprMetodo + ", COUNT(*), SUM(total) " +
                      "FROM PEDIDO GROUP BY " + exprMetodo + " ORDER BY SUM(total) DESC;";
            }

            List<String> filas = ejecutarSqlcmd(sql);
            if (filas != null) {
                for (String f : filas) {
                    String[] p = f.split(";");
                    if (p.length >= 3) {
                        String met = p[0].trim();
                        if (met.toLowerCase().startsWith("tarjeta")) {
                            met = "Tarjeta";
                        }
                        int cant = Integer.parseInt(p[1].trim());
                        double tot = Double.parseDouble(p[2].trim());
                        lista.add(new Object[]{met, cant, tot});
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("ERROR obtenerVentasMetodoPagoBD: " + ex.getMessage());
            ex.printStackTrace();
        }
        return lista;
    }

    /**
     * Obtiene los clientes frecuentes desde BD_Cafeteria_POO (filtrable por mes).
     */
    public static List<Object[]> obtenerClientesFrecuentesBD(int mes, int anio) {
        if (!hayConexion()) return null;
        List<Object[]> lista = new ArrayList<>();
        try {
            String sql;
            if (mes > 0) {
                sql = String.format("SET NOCOUNT ON; SELECT TOP 15 (c.nombres + ' ' + c.apellidos), COUNT(p.id_pedido), SUM(p.total) " +
                                    "FROM CLIENTE c INNER JOIN PEDIDO p ON c.id_cliente = p.id_cliente " +
                                    "WHERE YEAR(p.fecha_pedido) = %d AND MONTH(p.fecha_pedido) = %d " +
                                    "GROUP BY c.id_cliente, c.nombres, c.apellidos ORDER BY COUNT(p.id_pedido) DESC;", anio, mes);
            } else {
                sql = "SET NOCOUNT ON; SELECT TOP 15 (c.nombres + ' ' + c.apellidos), COUNT(p.id_pedido), SUM(p.total) " +
                      "FROM CLIENTE c INNER JOIN PEDIDO p ON c.id_cliente = p.id_cliente " +
                      "GROUP BY c.id_cliente, c.nombres, c.apellidos ORDER BY COUNT(p.id_pedido) DESC;";
            }

            List<String> filas = ejecutarSqlcmd(sql);
            if (filas != null) {
                for (String f : filas) {
                    String[] p = f.split(";");
                    if (p.length >= 3) {
                        String cli = p[0].trim();
                        int cant = Integer.parseInt(p[1].trim());
                        double tot = Double.parseDouble(p[2].trim());
                        lista.add(new Object[]{cli, cant, tot});
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("ERROR obtenerVentasMetodoPagoBD: " + ex.getMessage());
            ex.printStackTrace();
        }
        return lista;
    }

    /**
     * Obtiene los pedidos atendidos por empleado desde BD_Cafeteria_POO (filtrable por mes).
     */
    public static List<Object[]> obtenerPedidosPorEmpleadoBD(int mes, int anio) {
        if (!hayConexion()) return null;
        List<Object[]> lista = new ArrayList<>();
        try {
            String sql;
            if (mes > 0) {
                sql = String.format("SET NOCOUNT ON; SELECT (e.nombres + ' ' + e.apellidos), e.cargo, COUNT(p.id_pedido), ISNULL(SUM(p.total), 0) " +
                                    "FROM EMPLEADO e LEFT JOIN PEDIDO p ON e.id_empleado = p.id_empleado " +
                                    "AND YEAR(p.fecha_pedido) = %d AND MONTH(p.fecha_pedido) = %d " +
                                    "GROUP BY e.id_empleado, e.nombres, e.apellidos, e.cargo ORDER BY COUNT(p.id_pedido) DESC;", anio, mes);
            } else {
                sql = "SET NOCOUNT ON; SELECT (e.nombres + ' ' + e.apellidos), e.cargo, COUNT(p.id_pedido), ISNULL(SUM(p.total), 0) " +
                      "FROM EMPLEADO e LEFT JOIN PEDIDO p ON e.id_empleado = p.id_empleado " +
                      "GROUP BY e.id_empleado, e.nombres, e.apellidos, e.cargo ORDER BY COUNT(p.id_pedido) DESC;";
            }

            List<String> filas = ejecutarSqlcmd(sql);
            if (filas != null) {
                for (String f : filas) {
                    String[] p = f.split(";");
                    if (p.length >= 4) {
                        String emp = p[0].trim();
                        String cargo = p[1].trim();
                        int cant = Integer.parseInt(p[2].trim());
                        double tot = Double.parseDouble(p[3].trim());
                        lista.add(new Object[]{emp, cargo, cant, tot});
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("ERROR obtenerVentasMetodoPagoBD: " + ex.getMessage());
            ex.printStackTrace();
        }
        return lista;
    }

    /**
     * Registra un nuevo pedido en la base de datos SQL Server BD_Cafeteria_POO.
     */
    public static boolean registrarPedidoEnBD(Pedido pedido, String dniCliente, String nombreCliente) {
        if (!hayConexion()) {
            return false;
        }
        try {
            int idEmpleado = pedido.getEmpleado() != null ? pedido.getEmpleado().getId() : 1;
            int idCliente = pedido.getCliente() != null ? pedido.getCliente().getId() : 1;
            String metodoPago = pedido.getMetodoPago() != null ? pedido.getMetodoPago() : "Efectivo";
            double total = pedido.calcularTotal();

            // Insertar en PEDIDO y obtener ID generado
            String sqlPedido = String.format(java.util.Locale.US,
                "SET NOCOUNT ON; INSERT INTO PEDIDO (fecha_pedido, total, metodo_pago, id_cliente, id_empleado) " +
                "VALUES (CONVERT(date, GETDATE()), %.2f, '%s', %d, %d); SELECT SCOPE_IDENTITY();",
                total, metodoPago.replace("'", "''"), idCliente, idEmpleado
            );

            List<String> resp = ejecutarSqlcmd(sqlPedido);
            int idPedidoGenerado = 0;
            if (resp != null && !resp.isEmpty()) {
                try {
                    idPedidoGenerado = (int) Double.parseDouble(resp.get(0).trim());
                } catch (Exception ex) {
            System.err.println("ERROR obtenerVentasMetodoPagoBD: " + ex.getMessage());
            ex.printStackTrace();
        }
            }

            if (idPedidoGenerado > 0) {
                // Insertar detalles del pedido y actualizar stock en BD
                for (modelo.DetallePedido d : pedido.getItems()) {
                    String sqlDetalle = String.format(java.util.Locale.US,
                        "SET NOCOUNT ON; INSERT INTO DETALLE_PEDIDO (id_pedido, id_producto, cantidad, subtotal) " +
                        "VALUES (%d, %d, %d, %.2f); UPDATE PRODUCTO SET stock = stock - %d WHERE id_producto = %d;",
                        idPedidoGenerado, d.getProducto().getIdProducto(), d.getCantidad(), d.calcularSubtotal(),
                        d.getCantidad(), d.getProducto().getIdProducto()
                    );
                    ejecutarSqlcmd(sqlDetalle);
                }

                // Insertar Boleta en COMPROBANTE_PAGO (id_tipo_comprobante = 1 para Boleta)
                String sqlComprobante = String.format(
                    "SET NOCOUNT ON; INSERT INTO COMPROBANTE_PAGO (id_pedido, id_tipo_comprobante, numero_documento, nombre_razon_social, fecha_emision) " +
                    "VALUES (%d, 1, '%s', '%s', GETDATE());",
                    idPedidoGenerado,
                    (dniCliente != null ? dniCliente.replace("'", "''") : "00000000"),
                    (nombreCliente != null ? nombreCliente.replace("'", "''") : "Consumidor Final")
                );
                ejecutarSqlcmd(sqlComprobante);
                return true;
            }
        } catch (Exception ex) {
            System.err.println("Error al registrar en BD: " + ex.getMessage());
        }
        return false;
    }

    /**
     * Actualiza el stock de un producto en la base de datos SQL Server BD_Cafeteria_POO.
     */
    public static boolean agregarStockEnBD(int idProducto, int cantidad) {
        if (!hayConexion()) {
            return false;
        }
        try {
            String sql = String.format("SET NOCOUNT ON; UPDATE PRODUCTO SET stock = stock + %d WHERE id_producto = %d;", cantidad, idProducto);
            List<String> res = ejecutarSqlcmd(sql);
            return res != null;
        } catch (Exception e) {
            return false;
        }
    }
}
