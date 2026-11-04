<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="estacionamiento.domain.Cochera" %>
<%@ page import="estacionamiento.domain.Lugar" %>
<%@ page import="estacionamiento.domain.Reserva" %>
<%@ page import="estacionamiento.domain.EstadoReserva" %>
<%@ page import="estacionamiento.domain.claves.ReservaId" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel Operativo de Cochera - MR Estacionamiento</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        .box-lugar {
            transition: transform 0.15s ease, box-shadow 0.15s ease;
            min-height: 115px;
        }
        .box-lugar:hover {
            transform: translateY(-3px);
            box-shadow: 0 .5rem 1rem rgba(0,0,0,.12)!important;
        }
        .box-libre {
            background-color: #e8f5e9;
            border: 2px solid #2e7d32 !important;
            color: #1b5e20;
        }
        .box-ocupado {
            background-color: #ffebee;
            border: 2px solid #c62828 !important;
            color: #b71c1c;
        }
        .box-parcial {
            background-color: #e3f2fd;
            border: 2px solid #1565c0 !important;
            color: #0d47a1;
        }
        .box-inactivo {
            background-color: #eceff1;
            border: 2px dashed #90a4ae !important;
            color: #546e7a;
            opacity: 0.75;
        }
    </style>
</head>
<body class="bg-light">

    <%@ include file="navbar.jsp" %>

    <%
        Boolean sinAsignacion = (Boolean) request.getAttribute("sinAsignacion");
        Cochera cocheraActual = (Cochera) request.getAttribute("cocheraActual");
        List<Cochera> misCocheras = (List<Cochera>) request.getAttribute("misCocheras");
        List<Lugar> lugaresCochera = (List<Lugar>) request.getAttribute("lugaresCochera");
        List<Reserva> reservasActivas = (List<Reserva>) request.getAttribute("reservasActivas");
        Map<Integer, Reserva> mapaOcupacion = (Map<Integer, Reserva>) request.getAttribute("mapaOcupacionFisica");

        ReservaId reservaPendientePago = (ReservaId) session.getAttribute("reservaPendientePago");
        BigDecimal montoPendientePago = (BigDecimal) session.getAttribute("montoPendientePago");

        String error = (String) session.getAttribute("error");
        String exito = (String) session.getAttribute("exito");
        if (error != null) session.removeAttribute("error");
        if (exito != null) session.removeAttribute("exito");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM HH:mm");
    %>

    <div class="container-fluid px-4 mt-4 mb-5">

        <% if (Boolean.TRUE.equals(sinAsignacion) || cocheraActual == null) { %>
            <!-- VISTA CUANDO EL ENCARGADO AÚN NO TIENE COCHERA ASIGNADA -->
            <div class="row justify-content-center mt-5">
                <div class="col-md-6">
                    <div class="card border-0 shadow-sm rounded-4 text-center p-5">
                        <i class="bi bi-building-exclamation text-warning" style="font-size: 4rem;"></i>
                        <h3 class="fw-bold text-dark mt-3">Sin Cochera Asignada</h3>
                        <p class="text-muted">
                            Tu usuario se encuentra activo como <strong>Encargado</strong>, pero todavía no tienes una cochera municipal vinculada para operar hoy.
                        </p>
                        <div class="alert alert-secondary small border-0">
                            Solicitá a un administrativo municipal que te vincule desde el módulo de <em>Administración de Usuarios y Personal</em>.
                        </div>
                    </div>
                </div>
            </div>
        <% } else { 
            // Calculamos métricas rápidas para los contadores superiores
            int totalLugares = (lugaresCochera != null) ? lugaresCochera.size() : 0;
            int ocupados = (mapaOcupacion != null) ? mapaOcupacion.size() : 0;
            int inactivos = 0;
            if (lugaresCochera != null) {
                for (Lugar l : lugaresCochera) {
                    if ("Inactivo".equalsIgnoreCase(l.getDescripcion())) inactivos++;
                }
            }
            int libres = Math.max(0, totalLugares - ocupados - inactivos);
        %>

            <!-- CABECERA OPERATIVA Y SELECTOR DE SEDE -->
            <div class="d-flex flex-wrap justify-content-between align-items-center bg-white p-3 rounded-4 shadow-sm mb-4 border-start border-5 border-primary">
                <div>
                    <span class="badge bg-primary mb-1"><i class="bi bi-shield-lock-fill me-1"></i>Puesto Operativo de Barrera</span>
                    <h3 class="fw-bold text-dark mb-0">
                        <%= cocheraActual.getNombre() %> 
                        <small class="text-muted fs-6 fw-normal ms-2"><i class="bi bi-geo-alt-fill text-danger"></i> <%= cocheraActual.getDireccion() %></small>
                    </h3>
                </div>

                <div class="d-flex align-items-center gap-3 mt-2 mt-md-0">
                    <!-- Contadores rápidos -->
                    <div class="d-flex gap-2">
                        <span class="badge bg-success fs-6 py-2 px-3"><i class="bi bi-check-circle me-1"></i>Libres: <%= libres %></span>
                        <span class="badge bg-danger fs-6 py-2 px-3"><i class="bi bi-car-front-fill me-1"></i>Ocupados: <%= ocupados %></span>
                    </div>

                    <!-- Selector por si el encargado administra más de una cochera -->
                    <% if (misCocheras != null && misCocheras.size() > 1) { %>
                        <form action="<%= request.getContextPath() %>/panel-encargado" method="GET" class="d-flex align-items-center m-0">
                            <select name="idCochera" class="form-select form-select-sm fw-bold border-primary" onchange="this.form.submit()">
                                <% for (Cochera c : misCocheras) { %>
                                    <option value="<%= c.getCodigo() %>" <%= c.getCodigo().equals(cocheraActual.getCodigo()) ? "selected" : "" %>>
                                        Cambiar a: <%= c.getNombre() %>
                                    </option>
                                <% } %>
                            </select>
                        </form>
                    <% } %>
                </div>
            </div>

            <div class="row g-4">
                
                <!-- ================================================================= -->
                <!-- COLUMNA IZQUIERDA: CONTROL DE BARRERA + ALTA DE LUGARES           -->
                <!-- ================================================================= -->
                <div class="col-lg-4 col-md-5">

                    <!-- ALERTA ESPECIAL DE COBRO EN BARRERA (Solo aparece si alguien excedió el horario al salir) -->
                    <% if (reservaPendientePago != null) { %>
                        <div class="card border-0 shadow rounded-4 mb-4 bg-warning bg-opacity-25 border-start border-warning border-5">
                            <div class="card-body p-4">
                                <h5 class="fw-bold text-dark mb-2">
                                    <i class="bi bi-exclamation-octagon-fill text-danger me-1"></i>Barrera Retenida por Exceso
                                </h5>
                                <p class="small text-dark mb-2">
                                    El vehículo <strong><%= reservaPendientePago.getPatenteVehiculo() %></strong> superó el tiempo de tolerancia.
                                </p>
                                <div class="bg-white p-3 rounded-3 mb-3 text-center border">
                                    <span class="text-muted small d-block">Recargo / Saldo a cobrar ahora:</span>
                                    <span class="fs-3 fw-bold text-danger">$ <%= montoPendientePago != null ? montoPendientePago : "0.00" %></span>
                                </div>
                                <form action="<%= request.getContextPath() %>/panel-encargado" method="POST" class="m-0">
                                    <input type="hidden" name="accion" value="cobrarEfectivo">
                                    <input type="hidden" name="codigoCochera" value="<%= cocheraActual.getCodigo() %>">
                                    <input type="hidden" name="patente" value="<%= reservaPendientePago.getPatenteVehiculo() %>">
                                    <input type="hidden" name="numeroUsuario" value="<%= reservaPendientePago.getNumeroUsuario() %>">
                                    <input type="hidden" name="numeroTipoEstadia" value="<%= reservaPendientePago.getNumeroTipoEstadia() %>">
                                    <input type="hidden" name="fechaDesde" value="<%= reservaPendientePago.getFechaDesde() %>">
                                    <button type="submit" class="btn btn-success w-100 fw-bold py-2 shadow-sm">
                                        <i class="bi bi-cash-coin me-2"></i>Confirmar Cobro y Abrir Barrera
                                    </button>
                                </form>
                            </div>
                        </div>
                    <% } %>

                    <!-- 1. TARJETA LECTOR DE PASE QR / TERMINAL DE BARRERA -->
                    <div class="card border-0 shadow-sm rounded-4 mb-4">
                        <div class="card-header bg-dark text-white py-3 rounded-top-4">
                            <h5 class="mb-0 fw-bold"><i class="bi bi-qr-code-scan me-2 text-info"></i>Control de Barrera (Lector QR)</h5>
                        </div>
                        <div class="card-body p-4">
                            <form action="<%= request.getContextPath() %>/panel-encargado" method="POST">
                                <input type="hidden" name="codigoCochera" value="<%= cocheraActual.getCodigo() %>">
                                
                                <div class="mb-3">
                                    <label class="form-label small fw-bold text-muted">Cadena del Pase QR</label>
                                    <input type="text" class="form-control font-monospace" name="codigoQR" id="inputCodigoQR"
                                           placeholder="PATENTE|USUARIO|ESTADIA|FECHA" required>
                                    <div class="form-text small">Escaneá el QR del cliente o usá los botones rápidos de la tabla inferior.</div>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label small fw-bold text-muted">Operación en Barrera</label>
                                    <select class="form-select" name="accion" id="selectAccionBarrera" onchange="toggleOpcionesSalida()">
                                        <option value="registrarIngreso">🟢 Registrar INGRESO (Levantar Barrera)</option>
                                        <option value="procesarSalida">🔴 Registrar SALIDA de Vehículo</option>
                                    </select>
                                </div>

                                <div class="mb-3 d-none" id="divTipoSalida">
                                    <label class="form-label small fw-bold text-muted">Modalidad de Salida</label>
                                    <select class="form-select" name="esDefinitiva">
                                        <option value="true">Salida Definitiva (Libera el lugar)</option>
                                        <option value="false">Salida Parcial (Retiene el lugar - Estadías 12/24h)</option>
                                    </select>
                                </div>

                                <button type="submit" class="btn btn-primary w-100 fw-bold py-2">
                                    <i class="bi bi-boombox me-2"></i>Ejecutar en Barrera
                                </button>
                            </form>
                        </div>
                    </div>

                    <!-- 2. TARJETA ALTA / EDICIÓN DE LUGARES FÍSICOS -->
                    <div class="card border-0 shadow-sm rounded-4" id="cardFormLugar">
                        <div class="card-header bg-secondary text-white py-3 rounded-top-4" id="headerFormLugar">
                            <h5 class="mb-0 fw-bold" id="tituloFormLugar">
                                <i class="bi bi-plus-square-dotted me-2"></i>Agregar Lugar / Box a la Cochera
                            </h5>
                        </div>
                        <div class="card-body p-4">
                            <form action="<%= request.getContextPath() %>/panel-encargado" method="POST" id="formLugar">
                                <input type="hidden" name="accion" id="accionLugar" value="crearLugar">
                                <input type="hidden" name="codigoCochera" value="<%= cocheraActual.getCodigo() %>">
                                <input type="hidden" name="codigoLugar" id="inputCodigoLugar" value="">

                                <div class="row g-2 mb-3">
                                    <div class="col-4">
                                        <label class="form-label small fw-bold text-muted">N° de Piso</label>
                                        <input type="number" class="form-control" name="numeroPiso" id="inputPisoLugar" min="0" max="20" value="0" required>
                                    </div>
                                    <div class="col-8">
                                        <label class="form-label small fw-bold text-muted">Descripción / Sector</label>
                                        <input type="text" class="form-control" name="descripcion" id="inputDescLugar" placeholder="Ej: Techado PB - Box A" required>
                                    </div>
                                </div>
                                <div class="form-text small mb-3 d-none text-dark" id="ayudaReactivar">
                                    <i class="bi bi-info-circle-fill text-warning me-1"></i>Si el box estaba <strong>Inactivo</strong>, al guardar una nueva descripción volverá a quedar <strong>Disponible</strong>.
                                </div>

                                <button type="submit" class="btn btn-outline-secondary w-100 fw-bold" id="btnSubmitLugar">
                                    <i class="bi bi-plus-circle me-1"></i>Dar de Alta Lugar
                                </button>
                                <button type="button" class="btn btn-light border w-100 fw-bold mt-2 d-none" id="btnCancelarLugar" onclick="cancelarEdicionLugar()">
                                    <i class="bi bi-x-circle me-1"></i>Cancelar Edición
                                </button>
                            </form>
                        </div>
                    </div>

                </div><!-- Fin Columna Izquierda -->

                <!-- ================================================================= -->
                <!-- COLUMNA DERECHA: MAPA VISUAL DE LUGARES + TABLA OPERATIVA         -->
                <!-- ================================================================= -->
                <div class="col-lg-8 col-md-7">

                    <!-- 1. MAPA VISUAL DE OCUPACIÓN EN TIEMPO REAL -->
                    <div class="card border-0 shadow-sm rounded-4 mb-4">
                        <div class="card-header bg-white py-3 border-bottom d-flex justify-content-between align-items-center">
                            <h5 class="mb-0 fw-bold text-dark">
                                <i class="bi bi-grid-3x3-gap-fill me-2 text-primary"></i>Mapa de Ocupación Físico
                            </h5>
                            <div class="small">
                                <span class="badge bg-success me-1">🟢 Libre</span>
                                <span class="badge bg-danger me-1">🔴 En Cochera</span>
                                <span class="badge bg-primary me-1">🔵 Salida Parcial</span>
                                <span class="badge bg-secondary">⚫ Inactivo</span>
                            </div>
                        </div>
                        <div class="card-body p-4">
                            <% if (lugaresCochera != null && !lugaresCochera.isEmpty()) { 
                                Integer pisoActual = null;
                            %>
                                <div class="row g-3">
                                <% for (Lugar l : lugaresCochera) { 
                                    if (pisoActual == null || !pisoActual.equals(l.getNumeroPiso())) {
                                        pisoActual = l.getNumeroPiso();
                                %>
                                    <div class="col-12 mt-3 mb-1">
                                        <h6 class="fw-bold text-secondary border-bottom pb-1 mb-0">
                                            <i class="bi bi-layers-fill me-1"></i>Piso <%= pisoActual == 0 ? "0 (Planta Baja)" : pisoActual %>
                                        </h6>
                                    </div>
                                <%  }

                                    Reserva rOcupante = (mapaOcupacion != null) ? mapaOcupacion.get(l.getCodigo()) : null;
                                    boolean esInactivo = "Inactivo".equalsIgnoreCase(l.getDescripcion());
                                    
                                    String claseBox = "box-libre";
                                    String iconoEstado = "bi-check-circle-fill text-success";
                                    String textoEstado = "DISPONIBLE";

                                    if (esInactivo) {
                                        claseBox = "box-inactivo";
                                        iconoEstado = "bi-slash-circle text-secondary";
                                        textoEstado = "INACTIVO";
                                    } else if (rOcupante != null) {
                                        if (rOcupante.getEstado() == EstadoReserva.SALIDA_PARCIAL) {
                                            claseBox = "box-parcial";
                                            iconoEstado = "bi-arrow-left-right text-primary";
                                            textoEstado = "SALIDA PARCIAL";
                                        } else {
                                            claseBox = "box-ocupado";
                                            iconoEstado = "bi-car-front-fill text-danger";
                                            textoEstado = "OCUPADO";
                                        }
                                    }
                                %>
                                    <div class="col-6 col-sm-4 col-md-3">
                                        <div class="card box-lugar rounded-3 p-2 h-100 d-flex flex-column justify-content-between <%= claseBox %>">
                                            <div class="d-flex justify-content-between align-items-center">
                                                <span class="fw-bold">Box #<%= l.getCodigo() %></span>
                                                <i class="bi <%= iconoEstado %> fs-5"></i>
                                            </div>

                                            <div class="my-1 text-center">
                                                <% if (rOcupante != null) { %>
                                                    <div class="badge bg-dark text-white fs-6 font-monospace"><%= rOcupante.getVehiculo().getPatente() %></div>
                                                    <div class="small mt-1" style="font-size: 0.72rem;">Hasta: <%= rOcupante.getFechaHastaTentativa().format(dtf) %></div>
                                                <% } else { %>
                                                    <div class="small fw-semibold"><%= textoEstado %></div>
                                                    <div class="text-muted" style="font-size: 0.72rem;"><%= l.getDescripcion() %></div>
                                                <% } %>
                                            </div>

                                            <div class="d-flex justify-content-between align-items-center mt-1 pt-1 border-top border-secondary border-opacity-25">
                                                <small style="font-size: 0.7rem;">Piso <%= l.getNumeroPiso() %></small>
                                                
                                                <% if (rOcupante == null) { 
                                                    String descEscapada = (l.getDescripcion() != null) ? l.getDescripcion().replace("'", "\\'") : "";
                                                %>
                                                    <div class="d-flex align-items-center gap-2">
                                                        <button type="button" class="btn btn-link text-primary p-0 m-0 small fw-semibold" 
                                                                style="font-size: 0.72rem; text-decoration: none;"
                                                                onclick="cargarEdicionLugar('<%= l.getCodigo() %>', '<%= l.getNumeroPiso() %>', '<%= descEscapada %>')"
                                                                title="Editar o Reactivar este Box">
                                                            <i class="bi <%= esInactivo ? "bi-arrow-counterclockwise text-success" : "bi-pencil-square" %>"></i> 
                                                            <%= esInactivo ? "Reactivar" : "Editar" %>
                                                        </button>

                                                        <% if (!esInactivo) { %>
                                                            <form action="<%= request.getContextPath() %>/panel-encargado" method="POST" class="m-0 d-inline">
                                                                <input type="hidden" name="accion" value="bajaLugar">
                                                                <input type="hidden" name="codigoCochera" value="<%= cocheraActual.getCodigo() %>">
                                                                <input type="hidden" name="codigoLugar" value="<%= l.getCodigo() %>">
                                                                <button type="submit" class="btn btn-link text-danger p-0 m-0 small" 
                                                                        style="font-size: 0.72rem; text-decoration: none;"
                                                                        onclick="return confirm('¿Inhabilitar el Box #<%= l.getCodigo() %>?');" title="Inhabilitar lugar">
                                                                    <i class="bi bi-tools"></i> Baja
                                                                </button>
                                                            </form>
                                                        <% } %>
                                                    </div>
                                                <% } %>
                                            </div>
                                        </div>
                                    </div>
                                <% } %>
                                </div>
                            <% } else { %>
                                <div class="text-center py-4 text-muted">
                                    <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                                    Esta cochera aún no tiene lugares dados de alta. Utilizá el formulario de la izquierda para crear el primer box.
                                </div>
                            <% } %>
                        </div>
                    </div>

                    <!-- 2. TABLA DE RESERVAS ACTIVAS Y ACCIONES DE BARRERA EN 1 CLIC -->
                    <div class="card border-0 shadow-sm rounded-4">
                        <div class="card-header bg-white py-3 border-bottom">
                            <h5 class="mb-0 fw-bold text-dark">
                                <i class="bi bi-list-check me-2 text-info"></i>Reservas del Día y Vehículos en Cochera (Acción Rápida)
                            </h5>
                        </div>
                        <div class="card-body p-0 table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light small text-muted">
                                    <tr>
                                        <th class="ps-3">Patente / Cliente</th>
                                        <th>Box</th>
                                        <th>Horario Reserva</th>
                                        <th>Estado</th>
                                        <th class="text-center">Operar Barrera (1 Clic)</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if (reservasActivas != null && !reservasActivas.isEmpty()) {
                                        for (Reserva r : reservasActivas) {
                                            String badgeEst = "bg-warning text-dark";
                                            if (r.getEstado() == EstadoReserva.EN_CURSO) badgeEst = "bg-danger text-white";
                                            else if (r.getEstado() == EstadoReserva.SALIDA_PARCIAL) badgeEst = "bg-primary text-white";

                                            String cadenaQR = r.getId().getPatenteVehiculo() + "|" + 
                                                              r.getId().getNumeroUsuario() + "|" + 
                                                              r.getId().getNumeroTipoEstadia() + "|" + 
                                                              r.getId().getFechaDesde();
                                    %>
                                        <tr>
                                            <td class="ps-3">
                                                <span class="fw-bold font-monospace fs-6"><%= r.getVehiculo().getPatente() %></span>
                                                <div class="small text-muted"><%= r.getUsuario().getApellido() %>, <%= r.getUsuario().getNombre() %></div>
                                            </td>
                                            <td>
                                                <span class="badge bg-light text-dark border">Box #<%= r.getLugar() != null ? r.getLugar().getCodigo() : "-" %></span>
                                            </td>
                                            <td class="small">
                                                <div><strong>De:</strong> <%= r.getId().getFechaDesde().format(dtf) %></div>
                                                <div><strong>A:</strong> <%= r.getFechaHastaTentativa().format(dtf) %> (<%= r.getTipoEstadia().getDescripcion() %>)</div>
                                            </td>
                                            <td>
                                                <span class="badge <%= badgeEst %> rounded-pill"><%= r.getEstado().name() %></span>
                                            </td>
                                            <td class="text-center">
                                                <div class="d-flex justify-content-center gap-1 flex-wrap">
                                                    <% if (r.getEstado() == EstadoReserva.PENDIENTE || r.getEstado() == EstadoReserva.SALIDA_PARCIAL) { %>
                                                        <form action="<%= request.getContextPath() %>/panel-encargado" method="POST" class="m-0">
                                                            <input type="hidden" name="accion" value="registrarIngreso">
                                                            <input type="hidden" name="codigoCochera" value="<%= cocheraActual.getCodigo() %>">
                                                            <input type="hidden" name="codigoQR" value="<%= cadenaQR %>">
                                                            <button type="submit" class="btn btn-success btn-sm fw-bold">
                                                                <i class="bi bi-box-arrow-in-right me-1"></i>Dar Ingreso
                                                            </button>
                                                        </form>
                                                    <% } %>

                                                    <% if (r.getEstado() == EstadoReserva.EN_CURSO) { %>
                                                        <form action="<%= request.getContextPath() %>/panel-encargado" method="POST" class="m-0">
                                                            <input type="hidden" name="accion" value="procesarSalida">
                                                            <input type="hidden" name="codigoCochera" value="<%= cocheraActual.getCodigo() %>">
                                                            <input type="hidden" name="codigoQR" value="<%= cadenaQR %>">
                                                            <input type="hidden" name="esDefinitiva" value="true">
                                                            <button type="submit" class="btn btn-danger btn-sm fw-bold" title="Registrar salida definitiva">
                                                                <i class="bi bi-box-arrow-right me-1"></i>Salida Final
                                                            </button>
                                                        </form>

                                                        <% if (!r.getTipoEstadia().getDescripcion().equalsIgnoreCase("Por Hora")) { %>
                                                            <form action="<%= request.getContextPath() %>/panel-encargado" method="POST" class="m-0">
                                                                <input type="hidden" name="accion" value="procesarSalida">
                                                                <input type="hidden" name="codigoCochera" value="<%= cocheraActual.getCodigo() %>">
                                                                <input type="hidden" name="codigoQR" value="<%= cadenaQR %>">
                                                                <input type="hidden" name="esDefinitiva" value="false">
                                                                <button type="submit" class="btn btn-outline-primary btn-sm" title="Salida temporal (retiene el box)">
                                                                    <i class="bi bi-arrow-left-right"></i> Parcial
                                                                </button>
                                                            </form>
                                                        <% } %>
                                                    <% } %>
                                                </div>
                                            </td>
                                        </tr>
                                    <%  }
                                       } else { %>
                                        <tr>
                                            <td colspan="5" class="text-center py-4 text-muted">
                                                No hay reservas activas ni vehículos pendientes de ingreso en esta cochera.
                                            </td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>

                </div><!-- Fin Columna Derecha -->
            </div><!-- Fin Row Principal -->
        <% } %>
    </div>

    <script>
        function toggleOpcionesSalida() {
            const acc = document.getElementById('selectAccionBarrera').value;
            const divSalida = document.getElementById('divTipoSalida');
            if (acc === 'procesarSalida') {
                divSalida.classList.remove('d-none');
            } else {
                divSalida.classList.add('d-none');
            }
        }
        
        function cargarEdicionLugar(codigo, piso, descripcionActual) {
            document.getElementById('accionLugar').value = 'editarLugar';
            document.getElementById('inputCodigoLugar').value = codigo;
            document.getElementById('inputPisoLugar').value = piso;
            
            // Si estaba "Inactivo", le limpiamos el input para obligarlo a escribir la descripción real y reactivarlo
            const inputDesc = document.getElementById('inputDescLugar');
            if (descripcionActual.toLowerCase() === 'inactivo') {
                inputDesc.value = '';
                inputDesc.placeholder = 'Escriba nueva descripción para reactivar...';
            } else {
                inputDesc.value = descripcionActual;
            }

            // Cambiamos el aspecto de la tarjeta a modo Edición (Amarillo)
            const header = document.getElementById('headerFormLugar');
            header.className = 'card-header bg-warning text-dark py-3 rounded-top-4';
            document.getElementById('tituloFormLugar').innerHTML = '<i class="bi bi-pencil-square me-2"></i>Editar / Reactivar Box #' + codigo;
            
            const btnSubmit = document.getElementById('btnSubmitLugar');
            btnSubmit.className = 'btn btn-warning text-dark w-100 fw-bold';
            btnSubmit.innerHTML = '<i class="bi bi-check2-circle me-1"></i>Guardar Cambios del Box #' + codigo;

            document.getElementById('ayudaReactivar').classList.remove('d-none');
            document.getElementById('btnCancelarLugar').classList.remove('d-none');

            // Hacemos foco suave en el campo descripción
            inputDesc.focus();
        }

        function cancelarEdicionLugar() {
            document.getElementById('accionLugar').value = 'crearLugar';
            document.getElementById('inputCodigoLugar').value = '';
            document.getElementById('inputPisoLugar').value = '0';
            
            const inputDesc = document.getElementById('inputDescLugar');
            inputDesc.value = '';
            inputDesc.placeholder = 'Ej: Techado PB - Box A';

            const header = document.getElementById('headerFormLugar');
            header.className = 'card-header bg-secondary text-white py-3 rounded-top-4';
            document.getElementById('tituloFormLugar').innerHTML = '<i class="bi bi-plus-square-dotted me-2"></i>Agregar Lugar / Box a la Cochera';

            const btnSubmit = document.getElementById('btnSubmitLugar');
            btnSubmit.className = 'btn btn-outline-secondary w-100 fw-bold';
            btnSubmit.innerHTML = '<i class="bi bi-plus-circle me-1"></i>Dar de Alta Lugar';

            document.getElementById('ayudaReactivar').classList.add('d-none');
            document.getElementById('btnCancelarLugar').classList.add('d-none');
        }

        <% if (error != null && !error.isEmpty()) { %>
            Swal.fire({ icon: 'error', title: 'Atención en Barrera', text: `<%= error %>`, confirmButtonColor: '#d33' });
        <% } %>

        <% if (exito != null && !exito.isEmpty()) { %>
            Swal.fire({ icon: 'success', title: 'Operación Exitosa', text: `<%= exito %>`, confirmButtonColor: '#198754' });
        <% } %>
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>