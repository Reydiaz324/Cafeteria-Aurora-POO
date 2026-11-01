package controlador;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import modelo.Boleta;
import modelo.Categoria;
import modelo.Cliente;
import modelo.ComprobantePago;
import modelo.DatosInvalidosException;
import modelo.DetallePedido;
import modelo.Empleado;
import modelo.Factura;
import modelo.HistorialPrecio;
import modelo.MovimientoEntrada;
import modelo.MovimientoInventario;
import modelo.MovimientoSalida;
import modelo.Pedido;
import modelo.Producto;
import modelo.Proveedor;
import modelo.StockInsuficienteException;
import persistencia.GestorArchivos;

public class GestorCafeteria {
    private final ArrayList<Categoria> categorias;
    private final ArrayList<Proveedor> proveedores;
    private final ArrayList<Producto> productos;
    private final ArrayList<Cliente> clientes;
    private final ArrayList<Empleado> empleados;
    private final ArrayList<Pedido> pedidos;
    private final ArrayList<ComprobantePago> comprobantes;
    private final ArrayList<MovimientoInventario> movimientosKardex;
    private final ArrayList<HistorialPrecio> historialPrecios;
    private final Map<String, String> padronRucSunat;

    private Empleado empleadoActivo;
    private int correlativoBoleta = 101;
    private int correlativoFactura = 501;

    public GestorCafeteria() {
        this.categorias = new ArrayList<>();
        this.proveedores = new ArrayList<>();
        this.productos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        this.comprobantes = new ArrayList<>();
        this.movimientosKardex = new ArrayList<>();
        this.historialPrecios = new ArrayList<>();
        this.padronRucSunat = new HashMap<>();

        inicializarDatosSemilla();
        persistencia.ConexionBD.sincronizarDesdeBD(productos, empleados);
        sincronizarArchivosIniciales();
    }

    private void inicializarDatosSemilla() {
        // 1. Categorias reales de BD_Cafeteria
        Categoria catCalientes = new Categoria(1, "Lattes/Cappuccino", "Café y bebidas calientes preparadas en la cafetería");
        Categoria catFrias = new Categoria(2, "Frappuccinos", "Bebidas frías y refrescantes preparadas en la cafetería");
        Categoria catPanaderia = new Categoria(3, "Panadería", "Productos de panadería para venta directa");
        Categoria catPostres = new Categoria(4, "Postres", "Postres y dulces para acompañamiento");
        categorias.add(catCalientes);
        categorias.add(catFrias);
        categorias.add(catPanaderia);
        categorias.add(catPostres);

        // 2. Proveedores reales de BD_Cafeteria
        Proveedor provMaria = new Proveedor(1, "Maria Almenara", "20514879632", "Productos de panadería y postres", "989111222", "ventas@mariaalmenara.com");
        Proveedor provCocina = new Proveedor(2, "Cocina Central", "20547896321", "Alimentos y productos preparados", "989333444", "contacto@cocinacentral.com");
        Proveedor provRansa = new Proveedor(3, "Ransa", "20100039207", "Suministros e insumos esenciales para bebidas", "989555666", "atencion@ransa.com");
        proveedores.add(provMaria);
        proveedores.add(provCocina);
        proveedores.add(provRansa);

        // 3. Productos reales de BD_Cafeteria (22 productos con stock estándar de cafetería)
        productos.add(new Producto(1, "Latte", "Bebida caliente con café espresso y leche vaporizada", 12.00, 45, 10, catCalientes, provRansa, "Activo"));
        productos.add(new Producto(2, "Cappuccino", "Bebida caliente con espresso, leche y espuma", 11.50, 42, 10, catCalientes, provRansa, "Activo"));
        productos.add(new Producto(3, "Mocha", "Bebida caliente con café, leche y chocolate", 13.00, 38, 10, catCalientes, provRansa, "Activo"));
        productos.add(new Producto(4, "Mocha Frapuccino", "Bebida fría con café, leche, chocolate y hielo", 13.00, 35, 10, catFrias, provRansa, "Activo"));
        productos.add(new Producto(5, "Espresso", "Café concentrado servido en porción pequeña", 8.00, 50, 10, catCalientes, provRansa, "Activo"));
        productos.add(new Producto(6, "Espresso Doble", "Café concentrado servido en porción doble", 9.50, 48, 10, catCalientes, provRansa, "Activo"));
        productos.add(new Producto(7, "Cold Brew", "Café frío preparado por infusión lenta", 13.50, 30, 10, catFrias, provRansa, "Activo"));
        productos.add(new Producto(8, "Muffins de naranja", "Muffin de naranja en porción individual", 7.00, 25, 10, catPostres, provMaria, "Activo"));
        productos.add(new Producto(9, "Brownies", "Postre de chocolate en porción individual", 8.50, 20, 10, catPostres, provMaria, "Activo"));
        productos.add(new Producto(10, "Cheesecake", "Postre frío a base de queso crema", 12.50, 18, 10, catPostres, provMaria, "Activo"));
        productos.add(new Producto(11, "Croissants", "Postre de masa hojaldrada para acompañamiento", 9.00, 24, 10, catPostres, provCocina, "Activo"));
        productos.add(new Producto(12, "Muffins de manzana", "Muffin de manzana en porción individual", 7.00, 22, 10, catPostres, provMaria, "Activo"));
        productos.add(new Producto(13, "Panino Vesubio", "Panino Vesubio preparado para venta directa", 14.00, 28, 10, catPanaderia, provCocina, "Activo"));
        productos.add(new Producto(14, "Chicken Panino", "Panino de pollo preparado para venta directa", 13.50, 30, 10, catPanaderia, provCocina, "Activo"));
        productos.add(new Producto(15, "Chicken Ciabatta", "Ciabatta de pollo preparada para venta directa", 14.50, 32, 10, catPanaderia, provCocina, "Activo"));
        productos.add(new Producto(16, "Triple", "Sándwich triple preparado para venta directa", 8.50, 35, 10, catPanaderia, provCocina, "Activo"));
        productos.add(new Producto(17, "Galleta de chocolate", "Galleta de chocolate para acompañamiento", 5.00, 40, 10, catPostres, provMaria, "Activo"));
        productos.add(new Producto(18, "Galleta de chispas de chocolate", "Galleta con chispas de chocolate", 5.50, 45, 10, catPostres, provMaria, "Activo"));
        productos.add(new Producto(19, "Vainilla Frapuccino", "Bebida fría sabor vainilla con leche y hielo", 13.50, 30, 10, catFrias, provRansa, "Activo"));
        productos.add(new Producto(20, "Caramelo Frapuccino", "Bebida fría sabor caramelo con leche y hielo", 14.00, 28, 10, catFrias, provRansa, "Activo"));
        productos.add(new Producto(21, "Fresa Frapuccino", "Bebida fría sabor fresa con leche y hielo", 13.50, 26, 10, catFrias, provRansa, "Activo"));
        productos.add(new Producto(22, "Chocolate Blanco Frapuccino", "Bebida fría sabor chocolate blanco", 14.50, 25, 10, catFrias, provRansa, "Activo"));

        // 4. Empleados reales de BD_Cafeteria_POO
        Empleado emp1 = new Empleado(1, "Mariana", "Vega", "45678912", "Encargado", "Full Time", 2500.00, "Mañana", "Activo", "1234");
        Empleado emp2 = new Empleado(2, "Jose", "Castillo", "46789123", "Barista", "Full Time", 1400.00, "Mañana", "Activo", "1111");
        Empleado emp3 = new Empleado(3, "Paola", "Mendoza", "47891234", "Barista", "Full Time", 1400.00, "Mañana", "Activo", "2222");
        Empleado emp4 = new Empleado(4, "Ruben", "Flores", "48912345", "Barista", "Full Time", 1400.00, "Tarde", "Activo", "3333");
        Empleado emp5 = new Empleado(5, "Elena", "Paredes", "49123456", "Barista", "Part Time", 800.00, "Tarde", "Activo", "4444");
        empleados.add(emp1);
        empleados.add(emp2);
        empleados.add(emp3);
        empleados.add(emp4);
        empleados.add(emp5);
        this.empleadoActivo = emp1;

        // 5. Clientes semilla
        clientes.add(new Cliente(1, "Valeria", "Mendoza Ríos", "72849102", "987654321", "valeria.mendoza@gmail.com", "Frecuente", "", "", 35));
        clientes.add(new Cliente(2, "Diego", "Ramos Paredes", "45891203", "912345678", "diego.ramos@hotmail.com", "General", "", "", 10));
        clientes.add(new Cliente(3, "Camila", "Torres Vargas", "74125896", "945612378", "camila.torres@upn.pe", "Corporativo", "20601234567", "INVERSIONES NORTE PERU S.A.C.", 50));
        clientes.add(new Cliente(4, "Luis", "Paredes Silva", "70159482", "963852741", "luis.paredes@empresa.pe", "Corporativo", "20559874123", "CONSULTORA TECNOLOGICA DEL NORTE E.I.R.L.", 25));

        // 6. Padrón RUC para consulta inmediata tipo SUNAT
        padronRucSunat.put("20601234567", "INVERSIONES NORTE PERU S.A.C.");
        padronRucSunat.put("20559874123", "CONSULTORA TECNOLOGICA DEL NORTE E.I.R.L.");
        padronRucSunat.put("20100039207", "RANSA COMERCIAL S.A.");
        padronRucSunat.put("20514879632", "MARIA ALMENARA S.A.C.");
        padronRucSunat.put("20547896321", "COCINA CENTRAL PERU S.A.C.");
        padronRucSunat.put("20131312955", "SUNAT - SUPERINTENDENCIA NACIONAL DE ADUANAS");
        padronRucSunat.put("20215276024", "UNIVERSIDAD PRIVADA DEL NORTE S.A.C.");

        // 7. Operaciones semilla iniciales (Pedidos, Comprobantes, Kardex e Historial de Precios)
        try {
            // Movimiento de Entrada inicial de Maria Almenara
            MovimientoEntrada mov1 = new MovimientoEntrada(1, buscarProductoPorId(8), 20, emp1,
                    "Abastecimiento matutino de repostería", provMaria, "F001-0008920", 4.20);
            mov1.ejecutarMovimiento();
            movimientosKardex.add(mov1);

            // Movimiento de Entrada de Ransa
            MovimientoEntrada mov2 = new MovimientoEntrada(2, buscarProductoPorId(1), 15, emp1,
                    "Reposición de insumos para café espresso y leche", provRansa, "F002-0014510", 6.50);
            mov2.ejecutarMovimiento();
            movimientosKardex.add(mov2);

            // Historial de precios inicial
            historialPrecios.add(new HistorialPrecio(1, buscarProductoPorId(1), 11.00, 12.00,
                    emp1.getNombreCompleto(), "Actualización de costos de insumos lácteos Ransa"));

            // Pedido semilla 1 (Boleta)
            Pedido ped1 = new Pedido(1, clientes.get(0), emp2, "Yape", "En local");
            ped1.agregarItem(buscarProductoPorId(1), 2, "Regular", "Caliente", "Ninguno");
            ped1.agregarItem(buscarProductoPorId(9), 1, "Regular", "Caliente", "Ninguno");
            registrarPedidoYComprobante(ped1, "BOLETA", clientes.get(0).getDni(), "", "");

            // Pedido semilla 2 (Boleta)
            Pedido ped2 = new Pedido(2, clientes.get(2), emp1, "Tarjeta", "En local");
            ped2.agregarItem(buscarProductoPorId(15), 2, "Regular", "Caliente", "Ninguno");
            ped2.agregarItem(buscarProductoPorId(4), 2, "Grande", "Frío", "Crema batida");
            registrarPedidoYComprobante(ped2, "BOLETA", clientes.get(2).getDni(), "", "");
        } catch (Exception ignored) {
        }
    }

    public void sincronizarArchivosIniciales() {
        try {
            persistirTodoEnArchivos();
        } catch (IOException ignored) {
        }
    }

    public void persistirTodoEnArchivos() throws IOException {
        GestorArchivos.guardarCatalogoProductos(productos);
        GestorArchivos.guardarPedidos(pedidos);
        GestorArchivos.guardarKardex(movimientosKardex);
        GestorArchivos.guardarHistorialPrecios(historialPrecios);
        GestorArchivos.guardarClientes(clientes);
        GestorArchivos.guardarEmpleados(empleados);
    }

    public ComprobantePago registrarPedidoYComprobante(Pedido pedido, String tipoComprobante,
                                                        String dniBoleta, String rucFactura, String razonSocialFactura)
            throws StockInsuficienteException, DatosInvalidosException, IOException {
        if (pedido == null || pedido.getItems().isEmpty()) {
            throw new DatosInvalidosException("El carrito de compras está vacío. Agregue al menos un producto.");
        }

        String fechaEmision = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        ComprobantePago comprobante;

        if ("FACTURA".equalsIgnoreCase(tipoComprobante)) {
            String serie = String.format("F001-%06d", correlativoFactura++);
            comprobante = new Factura(serie, fechaEmision, pedido, rucFactura, razonSocialFactura, "Local Central");
        } else {
            String serie = String.format("B001-%06d", correlativoBoleta++);
            comprobante = new Boleta(serie, fechaEmision, pedido, dniBoleta);
        }

        comprobante.validarDatosFiscales();
        pedido.confirmarPedido();

        for (DetallePedido dp : pedido.getItems()) {
            MovimientoSalida salidaVenta = new MovimientoSalida(
                    movimientosKardex.size() + 1,
                    dp.getProducto(),
                    dp.getCantidad(),
                    pedido.getEmpleado(),
                    "Venta " + pedido.getCodigoPedido() + " (" + comprobante.getNumeroSerie() + ")",
                    "VENTA"
            );
            movimientosKardex.add(salidaVenta);
        }

        pedidos.add(pedido);
        comprobantes.add(comprobante);

        GestorArchivos.exportarComprobanteTXT(comprobante);
        persistirTodoEnArchivos();
        persistencia.ConexionBD.registrarPedidoEnBD(pedido, dniBoleta,
                pedido.getCliente() != null ? pedido.getCliente().getNombreCompleto() : "Consumidor Final");

        return comprobante;
    }

    public MovimientoEntrada registrarAbastecimientoProveedor(Producto producto, Proveedor proveedor,
                                                               int cantidad, double costoUnitario,
                                                               String guiaFactura, String motivo)
            throws DatosInvalidosException, IOException {
        if (producto == null) {
            throw new DatosInvalidosException("Seleccione un producto válido del catálogo.");
        }
        if (proveedor == null) {
            throw new DatosInvalidosException("Seleccione un proveedor asociado.");
        }
        MovimientoEntrada entrada = new MovimientoEntrada(
                movimientosKardex.size() + 1,
                producto,
                cantidad,
                empleadoActivo,
                motivo != null && !motivo.trim().isEmpty() ? motivo.trim() : "Abastecimiento de proveedor",
                proveedor,
                guiaFactura,
                costoUnitario
        );
        entrada.ejecutarMovimiento();
        movimientosKardex.add(entrada);
        persistirTodoEnArchivos();
        return entrada;
    }

    public MovimientoSalida registrarSalidaOMerma(Producto producto, int cantidad, String tipoSalida, String motivo)
            throws DatosInvalidosException, StockInsuficienteException, IOException {
        if (producto == null) {
            throw new DatosInvalidosException("Seleccione un producto válido del catálogo.");
        }
        MovimientoSalida salida = new MovimientoSalida(
                movimientosKardex.size() + 1,
                producto,
                cantidad,
                empleadoActivo,
                motivo,
                tipoSalida
        );
        salida.ejecutarMovimiento();
        movimientosKardex.add(salida);
        persistirTodoEnArchivos();
        return salida;
    }

    public HistorialPrecio actualizarPrecioProducto(Producto producto, double nuevoPrecio, String motivo)
            throws DatosInvalidosException, IOException {
        if (producto == null) {
            throw new DatosInvalidosException("Debe seleccionar un producto del catálogo.");
        }
        if (nuevoPrecio <= 0) {
            throw new DatosInvalidosException("El nuevo precio debe ser un valor numérico mayor a S/ 0.00.");
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new DatosInvalidosException("Debe ingresar el motivo del cambio de precio para la auditoría.");
        }
        double anterior = producto.getPrecio();
        producto.setPrecio(nuevoPrecio);
        HistorialPrecio hp = new HistorialPrecio(
                historialPrecios.size() + 1,
                producto,
                anterior,
                nuevoPrecio,
                empleadoActivo != null ? empleadoActivo.getNombreCompleto() : "Encargado",
                motivo.trim()
        );
        historialPrecios.add(hp);
        persistirTodoEnArchivos();
        return hp;
    }

    public Producto registrarNuevoProducto(String nombre, String descripcion, double precio, int stock,
                                           int stockMinimo, Categoria categoria, Proveedor proveedor)
            throws DatosInvalidosException, IOException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre del producto no puede estar vacío.");
        }
        if (precio <= 0) {
            throw new DatosInvalidosException("El precio del producto debe ser mayor a cero.");
        }
        if (stock < 0 || stockMinimo < 0) {
            throw new DatosInvalidosException("El stock inicial y stock mínimo no pueden ser negativos.");
        }
        int nuevoId = productos.size() + 1;
        Producto nuevo = new Producto(nuevoId, nombre.trim(),
                descripcion != null && !descripcion.trim().isEmpty() ? descripcion.trim() : "Producto Cafetería Aurora",
                precio, stock, stockMinimo, categoria, proveedor, "Activo");
        productos.add(nuevo);
        persistirTodoEnArchivos();
        return nuevo;
    }

    public Cliente registrarNuevoCliente(String nombres, String apellidos, String dni, String telefono,
                                         String correo, String ruc, String razonSocial)
            throws DatosInvalidosException, IOException {
        if (nombres == null || nombres.trim().isEmpty() || apellidos == null || apellidos.trim().isEmpty()) {
            throw new DatosInvalidosException("Los nombres y apellidos del cliente son obligatorios.");
        }
        if (dni == null || !dni.trim().matches("\\d{8}")) {
            throw new DatosInvalidosException("El DNI del cliente debe contener exactamente 8 dígitos numéricos.");
        }
        for (Cliente c : clientes) {
            if (c.getDni().equals(dni.trim())) {
                throw new DatosInvalidosException("Ya existe un cliente registrado con el DNI " + dni.trim() + ".");
            }
        }
        String tipo = "General";
        if (ruc != null && !ruc.trim().isEmpty()) {
            if (!ruc.trim().matches("(10|20)\\d{9}")) {
                throw new DatosInvalidosException("El RUC debe tener 11 dígitos numéricos (iniciar con 10 o 20).");
            }
            tipo = "Corporativo";
            if (razonSocial != null && !razonSocial.trim().isEmpty()) {
                padronRucSunat.put(ruc.trim(), razonSocial.trim().toUpperCase());
            }
        }
        Cliente nuevo = new Cliente(clientes.size() + 1, nombres.trim(), apellidos.trim(), dni.trim(),
                telefono != null ? telefono.trim() : "", correo != null ? correo.trim() : "",
                tipo, ruc != null ? ruc.trim() : "", razonSocial != null ? razonSocial.trim() : "", 0);
        clientes.add(nuevo);
        persistirTodoEnArchivos();
        return nuevo;
    }

    public Empleado registrarNuevoEmpleado(String nombres, String apellidos, String dni, String cargo,
                                           String contrato, double sueldoBase, String turno, String clave)
            throws DatosInvalidosException, IOException {
        if (nombres == null || nombres.trim().isEmpty() || apellidos == null || apellidos.trim().isEmpty()) {
            throw new DatosInvalidosException("Los nombres y apellidos del empleado son obligatorios.");
        }
        if (dni == null || !dni.trim().matches("\\d{8}")) {
            throw new DatosInvalidosException("El DNI del empleado debe contener 8 dígitos numéricos.");
        }
        if (sueldoBase <= 0) {
            throw new DatosInvalidosException("El sueldo base debe ser mayor a S/ 0.00.");
        }
        Empleado nuevo = new Empleado(empleados.size() + 1, nombres.trim(), apellidos.trim(), dni.trim(),
                cargo, contrato, sueldoBase, turno, "Activo",
                clave != null && !clave.trim().isEmpty() ? clave.trim() : "1234");
        empleados.add(nuevo);
        persistirTodoEnArchivos();
        return nuevo;
    }

    public boolean autenticarEmpleado(Empleado empleado, String clave) {
        if (empleado != null && empleado.getClaveAcceso().equals(clave)) {
            this.empleadoActivo = empleado;
            return true;
        }
        return false;
    }

    public String consultarRazonSocialSunat(String ruc) throws DatosInvalidosException {
        if (ruc == null || !ruc.trim().matches("(10|20)\\d{9}")) {
            throw new DatosInvalidosException("RUC inválido: Debe ingresar 11 dígitos numéricos iniciados en 10 o 20.");
        }
        String limpio = ruc.trim();
        if (padronRucSunat.containsKey(limpio)) {
            return padronRucSunat.get(limpio);
        }
        if (limpio.startsWith("20")) {
            String generada = "CORPORACION COMERCIAL AURORA " + limpio.substring(7) + " S.A.C.";
            padronRucSunat.put(limpio, generada);
            return generada;
        } else {
            String generada = "NEGOCIO PERSONA NATURAL RUC " + limpio;
            padronRucSunat.put(limpio, generada);
            return generada;
        }
    }

    public List<Producto> filtrarProductos(int idCategoria, String textoBusqueda) {
        List<Producto> resultado = new ArrayList<>();
        String filtro = textoBusqueda != null ? textoBusqueda.trim().toLowerCase() : "";
        for (Producto p : productos) {
            boolean coincideCat = (idCategoria <= 0)
                    || (p.getCategoria() != null && p.getCategoria().getIdCategoria() == idCategoria);
            boolean coincideTexto = filtro.isEmpty()
                    || p.getNombreProducto().toLowerCase().contains(filtro)
                    || p.getDescripcion().toLowerCase().contains(filtro);
            if (coincideCat && coincideTexto) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public Producto buscarProductoPorId(int id) {
        for (Producto p : productos) {
            if (p.getIdProducto() == id) {
                return p;
            }
        }
        return null;
    }

    public double calcularTotalVentasAcumuladas() {
        double suma = 0.0;
        for (Pedido p : pedidos) {
            suma += p.calcularTotal();
        }
        return Math.round(suma * 100.0) / 100.0;
    }

    public double calcularTicketPromedio() {
        if (pedidos.isEmpty()) {
            return 0.0;
        }
        return Math.round((calcularTotalVentasAcumuladas() / pedidos.size()) * 100.0) / 100.0;
    }

    public List<Producto> obtenerProductosStockBajo() {
        List<Producto> alerta = new ArrayList<>();
        for (Producto p : productos) {
            if (p.requiereReposicion()) {
                alerta.add(p);
            }
        }
        return alerta;
    }

    public Map<String, Integer> obtenerRankingProductosVendidos() {
        Map<String, Integer> conteo = new LinkedHashMap<>();
        for (Pedido p : pedidos) {
            for (DetallePedido dp : p.getItems()) {
                String prod = dp.getProducto().getNombreProducto();
                conteo.put(prod, conteo.getOrDefault(prod, 0) + dp.getCantidad());
            }
        }
        return conteo;
    }

    public String generarTextoReporteGeneral() {
        StringBuilder sb = new StringBuilder();
        sb.append("==================================================================\n");
        sb.append("       REPORTE EJECUTIVO DE OPERACIONES - CAFETERIA AURORA        \n");
        sb.append("==================================================================\n");
        sb.append("Fecha de Generacion : ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
        sb.append("Usuario Responsable : ").append(empleadoActivo != null ? empleadoActivo.generarResumenTexto() : "Encargado").append("\n");
        sb.append("------------------------------------------------------------------\n");
        sb.append("1. INDICADORES CLAVE DE RENDIMIENTO (KPIs)\n");
        sb.append(String.format("   - Total de Pedidos Registrados : %d\n", pedidos.size()));
        sb.append(String.format("   - Ingresos Totales Acumulados  : S/ %.2f\n", calcularTotalVentasAcumuladas()));
        sb.append(String.format("   - Ticket Promedio por Venta    : S/ %.2f\n", calcularTicketPromedio()));
        sb.append(String.format("   - Productos en Alerta de Stock : %d\n", obtenerProductosStockBajo().size()));
        sb.append("------------------------------------------------------------------\n");
        sb.append("2. RESUMEN DE PEDIDOS EMITIDOS\n");
        for (ComprobantePago cp : comprobantes) {
            sb.append("   * ").append(cp.generarResumenTexto())
              .append(" | Pedido: ").append(cp.getPedido().getCodigoPedido())
              .append(" | Pago: ").append(cp.getPedido().getMetodoPago()).append("\n");
        }
        sb.append("------------------------------------------------------------------\n");
        sb.append("3. MOVIMIENTOS RECIENTES DE KARDEX\n");
        for (MovimientoInventario m : movimientosKardex) {
            sb.append("   * ").append(m.generarResumenTexto()).append("\n");
        }
        sb.append("==================================================================\n");
        return sb.toString();
    }

    public void agregarStockProducto(int idProducto, int cantidad) throws DatosInvalidosException, IOException {
        if (cantidad <= 0) {
            throw new DatosInvalidosException("La cantidad a agregar debe ser mayor a 0.");
        }
        Producto prod = buscarProductoPorId(idProducto);
        if (prod == null) {
            throw new DatosInvalidosException("Producto no encontrado.");
        }
        prod.aumentarStock(cantidad);
        persistencia.ConexionBD.agregarStockEnBD(idProducto, cantidad);
        Proveedor prov = prod.getProveedor() != null ? prod.getProveedor() : proveedores.get(0);
        Empleado emp = empleadoActivo != null ? empleadoActivo : empleados.get(0);
        MovimientoEntrada mov = new MovimientoEntrada(
            movimientosKardex.size() + 1,
            prod,
            cantidad,
            emp,
            "Reabastecimiento de stock en local",
            prov,
            "FAC-" + (2000 + idProducto),
            prod.getPrecio() * 0.65
        );
        movimientosKardex.add(mov);
        persistirTodoEnArchivos();
    }

    public double[] obtenerResumenGeneralVentas(int mes, int anio) {
        double[] bdRes = persistencia.ConexionBD.obtenerResumenVentasBD(mes, anio);
        if (bdRes != null) {
            return bdRes;
        }
        double total = calcularTotalVentasAcumuladas();
        int cant = pedidos.size();
        double ticket = calcularTicketPromedio();
        return new double[]{total, cant, ticket};
    }

    public double[] obtenerResumenGeneralVentas() {
        return obtenerResumenGeneralVentas(10, 2026);
    }

    public List<Object[]> obtenerReporteProductosMasVendidos(int mes, int anio) {
        List<Object[]> bdList = persistencia.ConexionBD.obtenerProductosMasVendidosBD(mes, anio);
        if (bdList != null && !bdList.isEmpty()) {
            return bdList;
        }
        return obtenerReporteProductosMasVendidos();
    }

    public List<Object[]> obtenerReporteProductosMasVendidos() {
        Map<String, int[]> map = new HashMap<>();
        for (Pedido p : pedidos) {
            for (DetallePedido dp : p.getItems()) {
                String prod = dp.getProducto().getNombreProducto();
                map.putIfAbsent(prod, new int[]{0, 0});
                map.get(prod)[0] += dp.getCantidad();
                map.get(prod)[1] += (int) Math.round(dp.calcularSubtotal() * 100);
            }
        }
        List<Object[]> res = new ArrayList<>();
        for (Map.Entry<String, int[]> e : map.entrySet()) {
            res.add(new Object[]{e.getKey(), e.getValue()[0], e.getValue()[1] / 100.0});
        }
        res.sort((a, b) -> Integer.compare((Integer) b[1], (Integer) a[1]));
        return res;
    }

    public List<Object[]> obtenerReporteStockActual() {
        List<Object[]> res = new ArrayList<>();
        for (Producto p : productos) {
            res.add(new Object[]{
                p.getNombreProducto(),
                p.getCategoria() != null ? p.getCategoria().getNombreCategoria() : "General",
                p.getStock(),
                p.getProveedor() != null ? p.getProveedor().getNombreProveedor() : "Aurora",
                p.getEstado()
            });
        }
        return res;
    }

    public List<Object[]> obtenerReporteVentasPorMes() {
        List<Object[]> bdList = persistencia.ConexionBD.obtenerVentasPorMesBD();
        if (bdList != null && !bdList.isEmpty()) {
            return bdList;
        }
        List<Object[]> res = new ArrayList<>();
        res.add(new Object[]{"Octubre 2026", pedidos.size(), calcularTotalVentasAcumuladas(), calcularTicketPromedio()});
        return res;
    }

    public List<Object[]> obtenerReporteVentasMetodoPago(int mes, int anio) {
        List<Object[]> bdList = persistencia.ConexionBD.obtenerVentasMetodoPagoBD(mes, anio);
        if (bdList != null && !bdList.isEmpty()) {
            return bdList;
        }
        return obtenerReporteVentasMetodoPago();
    }

    public List<Object[]> obtenerReporteVentasMetodoPago() {
        Map<String, double[]> map = new LinkedHashMap<>();
        for (Pedido p : pedidos) {
            String metodo = p.getMetodoPago() != null ? p.getMetodoPago() : "Efectivo";
            if (metodo.toLowerCase().startsWith("tarjeta")) {
                metodo = "Tarjeta";
            }
            map.putIfAbsent(metodo, new double[]{0, 0.0});
            map.get(metodo)[0] += 1;
            map.get(metodo)[1] += p.calcularTotal();
        }
        List<Object[]> res = new ArrayList<>();
        for (Map.Entry<String, double[]> e : map.entrySet()) {
            res.add(new Object[]{e.getKey(), (int) e.getValue()[0], Math.round(e.getValue()[1] * 100.0) / 100.0});
        }
        return res;
    }

    public List<Object[]> obtenerReporteClientesFrecuentes(int mes, int anio) {
        List<Object[]> bdList = persistencia.ConexionBD.obtenerClientesFrecuentesBD(mes, anio);
        if (bdList != null && !bdList.isEmpty()) {
            return bdList;
        }
        return obtenerReporteClientesFrecuentes();
    }

    public List<Object[]> obtenerReporteClientesFrecuentes() {
        Map<String, double[]> map = new HashMap<>();
        for (Pedido p : pedidos) {
            String cliente = p.getCliente() != null ? p.getCliente().getNombres() + " " + p.getCliente().getApellidos() : "Consumidor Final";
            map.putIfAbsent(cliente, new double[]{0, 0.0});
            map.get(cliente)[0] += 1;
            map.get(cliente)[1] += p.calcularTotal();
        }
        List<Object[]> res = new ArrayList<>();
        for (Map.Entry<String, double[]> e : map.entrySet()) {
            res.add(new Object[]{e.getKey(), (int) e.getValue()[0], Math.round(e.getValue()[1] * 100.0) / 100.0});
        }
        res.sort((a, b) -> Integer.compare((Integer) b[1], (Integer) a[1]));
        return res;
    }

    public List<Object[]> obtenerReporteEmpleadosMasPedidos(int mes, int anio) {
        List<Object[]> bdList = persistencia.ConexionBD.obtenerPedidosPorEmpleadoBD(mes, anio);
        if (bdList != null && !bdList.isEmpty()) {
            return bdList;
        }
        return obtenerReporteEmpleadosMasPedidos();
    }

    public List<Object[]> obtenerReporteEmpleadosMasPedidos() {
        Map<String, Object[]> map = new LinkedHashMap<>();
        for (Pedido p : pedidos) {
            String emp = p.getEmpleado() != null ? p.getEmpleado().getNombres() + " " + p.getEmpleado().getApellidos() : "Personal Turno";
            String cargo = p.getEmpleado() != null ? p.getEmpleado().getCargo() : "Barista";
            map.putIfAbsent(emp, new Object[]{cargo, 0, 0.0});
            map.get(emp)[1] = (Integer) map.get(emp)[1] + 1;
            map.get(emp)[2] = (Double) map.get(emp)[2] + p.calcularTotal();
        }
        List<Object[]> res = new ArrayList<>();
        for (Map.Entry<String, Object[]> e : map.entrySet()) {
            res.add(new Object[]{e.getKey(), e.getValue()[0], e.getValue()[1], Math.round(((Double) e.getValue()[2]) * 100.0) / 100.0});
        }
        res.sort((a, b) -> Integer.compare((Integer) b[2], (Integer) a[2]));
        return res;
    }

    public ArrayList<Categoria> getCategorias() { return categorias; }
    public ArrayList<Proveedor> getProveedores() { return proveedores; }
    public ArrayList<Producto> getProductos() { return productos; }
    public ArrayList<Cliente> getClientes() { return clientes; }
    public ArrayList<Empleado> getEmpleados() { return empleados; }
    public ArrayList<Pedido> getPedidos() { return pedidos; }
    public ArrayList<ComprobantePago> getComprobantes() { return comprobantes; }
    public ArrayList<MovimientoInventario> getMovimientosKardex() { return movimientosKardex; }
    public ArrayList<HistorialPrecio> getHistorialPrecios() { return historialPrecios; }
    public Empleado getEmpleadoActivo() { return empleadoActivo; }
}
