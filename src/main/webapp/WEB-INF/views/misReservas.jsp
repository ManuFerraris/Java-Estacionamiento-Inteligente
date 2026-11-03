<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="estacionamiento.domain.Usuario" %>
<%@ page import="estacionamiento.domain.Vehiculo" %>
<%@ page import="estacionamiento.domain.TipoEstadia" %>
<%@ page import="estacionamiento.domain.Reserva" %>
<%@ page import="estacionamiento.domain.EstadoReserva" %>
<%@ page import="estacionamiento.domain.Cochera" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Reservas - MR Estacionamiento</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="https://sdk.mercadopago.com/js/v2"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/qrious/4.0.2/qrious.min.js"></script>
    <script>
	  const mp = new MercadoPago("APP_USR-ccf26b17-befc-49e1-8564-9c3e6d9711ea");
	</script>
    <style>
        .estado-badge { width: 110px; text-align: center; }
    </style>
</head>
<body class="bg-light">

    <%@ include file="navbar.jsp" %>

    <div class="container px-4 mb-5">
        
        <div class="d-flex justify-content-between align-items-center mb-4 mt-3">
            <h2 class="text-secondary fw-bold"><i class="bi bi-car-front-fill me-2 text-info"></i>Gestión de Reservas</h2>
        </div>

        <% 
            String error = (String) session.getAttribute("error");
            String exito = (String) session.getAttribute("exito");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        %>

        <div class="row g-4">
            
            <!-- COLUMNA IZQUIERDA: Formulario de Cotización y Reserva -->
            <div class="col-lg-4">
                <div class="card border-0 shadow-sm rounded-4" style="top: 20px;">
                    <div class="card-header bg-info text-white py-3 rounded-top-4">
                        <h5 class="mb-0 fw-bold"><i class="bi bi-calendar-plus me-2"></i>Nueva Reserva</h5>
                    </div>
                    <div class="card-body p-4">
                        <form action="<%= request.getContextPath() %>/mis-reservas-user" method="POST" id="formReserva">
                            
                            <!-- Selección de Cochera -->
							<div class="mb-3">
							    <label class="form-label text-muted small fw-bold">Cochera Municipal</label>
							    <select class="form-select" name="idCochera" required>
							        <option value="" disabled selected>¿A dónde deseas ir?</option>
							        <% 
							            List<Cochera> cocheras = (List<Cochera>) request.getAttribute("cocheras");
							            if (cocheras != null) {
							                for(Cochera c : cocheras) {
							        %>
							            <!-- Si el getter de dirección se llama distinto en tu entidad, ajusta .getDireccion() -->
							            <option value="<%= c.getCodigo() %>"><%= c.getDireccion() %></option>
							        <% 
							                }
							            } 
							        %>
							    </select>
							</div>

                            <!-- Selección de Vehículo -->
							<div class="mb-3">
							    <label class="form-label text-muted small fw-bold">Vehículo</label>
							    <% 
							        List<Vehiculo> misVehiculos = (List<Vehiculo>) request.getAttribute("misVehiculos");
							        if (misVehiculos != null && !misVehiculos.isEmpty()) {
							    %>
							        <select class="form-select" name="patente" required>
							            <option value="" disabled selected>Selecciona tu vehículo...</option>
							            <% for(Vehiculo v : misVehiculos) { %>
							                <!-- Asegurate de que getDescripcion() exista, sino usá getMarca() + getModelo() -->
							                <option value="<%= v.getPatente() %>"><%= v.getPatente() %> - <%= v.getDescripcion() %></option>
							            <% } %>
							        </select>
							    <% } else { %>
							        <!-- Mensaje cuando el usuario no tiene autos cargados -->
							        <div class="alert alert-warning py-2 mb-0 border-0">
							            <i class="bi bi-exclamation-circle me-1"></i> No tienes vehículos cargados. 
							            <a href="${pageContext.request.contextPath}/mis-vehiculos-user" class="alert-link">Ve a Mis Vehículos y carga uno</a> para poder reservar.
							        </div>
							        <!-- Deshabilitamos el botón de reservar para evitar errores 500 -->
							        <script>
							            document.addEventListener("DOMContentLoaded", function() {
							                let btnReservar = document.querySelector("button[type='submit']");
							                if(btnReservar) {
							                    btnReservar.disabled = true;
							                    btnReservar.innerText = "Falta cargar un vehículo";
							                }
							            });
							        </script>
							    <% } %>
							</div>

                            <!-- Selección de Estadía -->
                            <div class="mb-3">
                                <label class="form-label text-muted small fw-bold">Tipo de Estadía</label>
                                <select class="form-select" name="idTipoEstadia" id="selectTipoEstadia" required>
                                    <option value="" disabled selected>¿Cuánto tiempo te quedas?</option>
                                    <% 
                                        List<TipoEstadia> tipos = (List<TipoEstadia>) request.getAttribute("tiposEstadia");
                                        if (tipos != null) {
                                            for(TipoEstadia t : tipos) {
                                    %>
                                        <option value="<%= t.getNumero() %>"><%= t.getDescripcion() %></option>
                                    <% 
                                            }
                                        } 
                                    %>
                                </select>
                            </div>

                            <!-- Fechas y Slots de 1 Hora -->
							<div class="mb-3">
							    <label class="form-label text-muted small fw-bold">Ingreso (Slots de 1 hora)</label>
							    <div class="row g-2">
							        <div class="col-7">
							            <input type="date" class="form-control" id="diaDesde" required>
							        </div>
							        <div class="col-5">
							            <select class="form-select" id="horaDesde" required>
							                <% for(int h = 0; h < 24; h++) { 
							                    String horaStr = String.format("%02d:00", h);
							                %>
							                    <option value="<%= horaStr %>"><%= horaStr %> hs</option>
							                <% } %>
							            </select>
							        </div>
							    </div>
							    <!-- Este input oculto le manda al Servlet el formato exacto que ya espera -->
							    <input type="hidden" id="fechaDesde" name="fechaDesde">
							</div>
							
							<div class="mb-3">
							    <label class="form-label text-muted small fw-bold">Salida (Tentativa)</label>
							    <div class="row g-2">
							        <div class="col-7">
							            <input type="date" class="form-control" id="diaHasta" required>
							        </div>
							        <div class="col-5">
							            <select class="form-select" id="horaHasta" required>
							                <% for(int h = 0; h < 24; h++) { 
							                    String horaStr = String.format("%02d:00", h);
							                %>
							                    <option value="<%= horaStr %>"><%= horaStr %> hs</option>
							                <% } %>
							            </select>
							        </div>
							    </div>
							    <input type="hidden" id="fechaHasta" name="fechaHasta">
							</div>
							
							<!-- Indicador de cupo en tiempo real -->
							<div id="badgeDisponibilidad" class="alert d-none py-2 px-3 small fw-bold mb-3 border-0"></div>

                            <button type="submit" class="btn btn-info w-100 py-2 fw-bold text-white fs-5">
                                <i class="bi bi-check-circle-fill me-2"></i>Reservar Lugar
                            </button>
                            
                            <div class="text-center mt-3 small text-muted">
                                *Si cuentas con un plan <strong>Premium</strong> o <strong>Medium</strong>, los descuentos se aplicarán automáticamente.
                            </div>
                        </form>
                    </div>
                </div>
                <!-- NUEVA TARJETA: Pagos Pendientes de Reservas (Señas, Saldos o Multas) -->
                <div class="card border-0 shadow-sm rounded-4 border-start border-warning border-4 mt-4">
                    <div class="card-body p-4">
                        <h5 class="fw-bold text-dark mb-3">
                            <i class="bi bi-receipt me-2 text-warning"></i>Pagos Pendientes
                        </h5>
                        
                        <% 
                            boolean tienePagosPendientes = false;
                            List<Reserva> listaReservas = (List<Reserva>) request.getAttribute("misReservas");
                            
                            if (listaReservas != null) {
                                for (Reserva res : listaReservas) {
                                    // Solo revisamos reservas activas (no canceladas ni ausentes)
                                    if (res.getEstado() != EstadoReserva.CANCELADA && res.getEstado() != EstadoReserva.NO_PRESENTADO) {
                                        
                                        // 1. ¿Tiene la SEÑA pendiente? (Ej: cerró MP sin pagar al reservar)
                                        if (res.getPagoSenia() != null && res.getPagoSenia().getEstado() == estacionamiento.domain.EstadoPago.PENDIENTE) {
                                            tienePagosPendientes = true;
                                            String conceptoSenia = "Seña Reserva - " + res.getVehiculo().getPatente();
                        %>
                                            <div class="d-flex justify-content-between align-items-center border-bottom pb-3 mb-3">
                                                <div>
                                                    <span class="badge bg-warning text-dark mb-1">Seña Pendiente</span>
                                                    <h6 class="mb-0 fw-bold"><%= res.getVehiculo().getPatente() %> <small class="text-muted fw-normal">(<%= res.getId().getFechaDesde().format(dtf) %>)</small></h6>
                                                    <small class="text-muted fw-bold">$ <%= res.getPagoSenia().getMonto() %></small>
                                                </div>
                                                <form action="<%= request.getContextPath() %>/mis-reservas-user" method="POST" class="m-0">
                                                    <input type="hidden" name="accion" value="pagarPendiente">
                                                    <input type="hidden" name="idPago" value="<%= res.getPagoSenia().getNumero() %>">
                                                    <input type="hidden" name="concepto" value="<%= conceptoSenia %>">
                                                    <button type="submit" class="btn btn-warning btn-sm fw-bold text-dark">
                                                        <i class="bi bi-wallet2 me-1"></i>Pagar
                                                    </button>
                                                </form>
                                            </div>
                        <%              } 
                                        
                                        // 2. ¿Tiene el SALDO DE ESTADÍA o MULTA pendiente?
                                        if (res.getPagoSaldo() != null && res.getPagoSaldo().getEstado() == estacionamiento.domain.EstadoPago.PENDIENTE) {
                                            tienePagosPendientes = true;
                                            String conceptoSaldo = "Saldo Estadía - " + res.getVehiculo().getPatente();
                        %>
                                            <div class="d-flex justify-content-between align-items-center border-bottom pb-3 mb-3">
                                                <div>
                                                    <span class="badge bg-info text-dark mb-1">Saldo Restante / Estadía</span>
                                                    <h6 class="mb-0 fw-bold"><%= res.getVehiculo().getPatente() %> <small class="text-muted fw-normal">(<%= res.getTipoEstadia().getDescripcion() %>)</small></h6>
                                                    <small class="text-muted fw-bold">$ <%= res.getPagoSaldo().getMonto() %></small>
                                                </div>
                                                <form action="<%= request.getContextPath() %>/mis-reservas-user" method="POST" class="m-0">
                                                    <input type="hidden" name="accion" value="pagarPendiente">
                                                    <input type="hidden" name="idPago" value="<%= res.getPagoSaldo().getNumero() %>">
                                                    <input type="hidden" name="concepto" value="<%= conceptoSaldo %>">
                                                    <button type="submit" class="btn btn-warning btn-sm fw-bold text-dark">
                                                        <i class="bi bi-wallet2 me-1"></i>Pagar
                                                    </button>
                                                </form>
                                            </div>
                        <%              }
                                    }
                                }
                            }
                            
                            if (!tienePagosPendientes) { 
                        %>
                            <p class="text-muted small mb-0"><i class="bi bi-check2-circle me-1 text-success"></i>Estás al día con tus pagos de cocheras.</p>
                        <% } %>
                    </div>
                </div>
            </div>

            <!-- COLUMNA DERECHA: Historial de Reservas -->
            <div class="col-lg-8">
                <div class="card border-0 shadow-sm rounded-4">
                    <div class="card-body p-4">
                        <h5 class="fw-bold text-dark mb-4"><i class="bi bi-card-list me-2 text-primary"></i>Mis Reservas Activas e Historial</h5>
                        
                        <div class="table-responsive">
                            <table class="table table-hover align-middle">
                                <thead class="table-light text-muted small">
                                    <tr>
                                        <th>Fecha Ingreso</th>
                                        <th>Patente</th>
                                        <th>Lugar Asignado</th>
                                        <th>Estado</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% 
                                        List<Reserva> misReservas = (List<Reserva>) request.getAttribute("misReservas");
                                        if (misReservas != null && !misReservas.isEmpty()) {
                                            for(Reserva r : misReservas) {
                                                
                                                // Definimos el color del badge según tu máquina de estados
                                                String colorBadge = "bg-secondary";
                                                if (r.getEstado() == EstadoReserva.PENDIENTE) colorBadge = "bg-warning text-dark";
                                                else if (r.getEstado() == EstadoReserva.EN_CURSO) colorBadge = "bg-primary";
                                                else if (r.getEstado() == EstadoReserva.SALIDA_PARCIAL) colorBadge = "bg-info text-dark";
                                                else if (r.getEstado() == EstadoReserva.CANCELADA) colorBadge = "bg-danger";
                                                else if (r.getEstado() == EstadoReserva.NO_PRESENTADO) colorBadge = "bg-dark text-white";
                                    %>
                                    <tr>
                                        <!-- 1. Columna: Fecha Ingreso -->
                                        <td>
                                            <div class="fw-bold"><%= r.getId().getFechaDesde().format(dtf) %></div>
                                            <div class="small text-muted"><%= r.getTipoEstadia().getDescripcion() %></div>
                                        </td>
                                        
                                        <!-- 2. Columna: Patente -->
                                        <td class="fw-bold"><%= r.getVehiculo().getPatente() %></td>
                                        
                                        <!-- 3. Columna: Lugar Asignado -->
                                        <td>
                                            <% if (r.getLugar() != null) { %>
                                                <span class="badge bg-light text-dark border"><i class="bi bi-p-square-fill me-1 text-primary"></i>Lugar <%= r.getLugar().getCodigo() %></span>
                                            <% } else { %>
                                                <span class="text-muted small">N/A</span>
                                            <% } %>
                                        </td>
                                        
                                        <!-- 4. Columna: Estado -->
                                        <td>
                                            <span class="badge <%= colorBadge %> estado-badge rounded-pill"><%= r.getEstado().name() %></span>
                                        </td>
                                        
                                        <!-- 5. Columna: Acciones (ACÁ ESTÁN LOS BOTONES REPARADOS) -->
                                        <td>
                                            <!-- BOTÓN QR: Visible si tiene que entrar, salir o reingresar -->
                                            <% if (r.getEstado() == EstadoReserva.PENDIENTE || r.getEstado() == EstadoReserva.EN_CURSO || r.getEstado() == EstadoReserva.SALIDA_PARCIAL) { %>
                                                <button class="btn btn-primary btn-sm fw-bold shadow-sm me-1 mb-1" 
                                                        onclick="generarPaseQR('<%= r.getId().getPatenteVehiculo() %>', '<%= r.getId().getNumeroUsuario() %>', '<%= r.getId().getNumeroTipoEstadia() %>', '<%= r.getId().getFechaDesde() %>')">
                                                    <i class="bi bi-qr-code"></i> Pase
                                                </button>
                                            <% } %>

                                            <!-- BOTÓN CANCELAR: Solo visible si aún no ingresó -->
                                            <% if (r.getEstado() == EstadoReserva.PENDIENTE) { %>
                                                <button class="btn btn-outline-danger btn-sm mb-1" onclick="cancelarReserva('<%= r.getVehiculo().getPatente() %>')"><i class="bi bi-x-circle"></i></button>
                                            <% } else if (r.getEstado() == EstadoReserva.EN_CURSO) { %>
                                                <span class="small text-muted fst-italic d-block">Vehículo en cochera</span>
                                            <% } else if (r.getEstado() == EstadoReserva.SALIDA_PARCIAL) { %>
                                                <span class="small text-muted fst-italic d-block">Debe regresar</span>
                                            <% } else { %>
                                                <span class="small text-muted">-</span>
                                            <% } %>
                                        </td>
                                    </tr>
                                    <% 
                                            }
                                        } else {
                                    %>
                                    <tr>
                                        <td colspan="5" class="text-center py-5">
                                            <i class="bi bi-cone-striped fs-1 text-muted d-block mb-2"></i>
                                            <span class="text-muted">Aún no tienes reservas registradas.</span>
                                        </td>
                                    </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            
        </div>
    </div>

    <!-- Script de validaciones -->
	<script>
	        // 1. Declaración de TODAS las referencias al DOM arriba de todo
	        const selectCochera = document.querySelector('select[name="idCochera"]');
	        const selectTipoEstadia = document.getElementById('selectTipoEstadia');
	        const diaDesde = document.getElementById('diaDesde');
	        const horaDesde = document.getElementById('horaDesde');
	        const diaHasta = document.getElementById('diaHasta');
	        const horaHasta = document.getElementById('horaHasta');
	        const inputFechaDesde = document.getElementById('fechaDesde');
	        const inputFechaHasta = document.getElementById('fechaHasta');
	        const badgeDisp = document.getElementById('badgeDisponibilidad');
	        const btnSubmit = document.querySelector('#formReserva button[type="submit"]');
	
	        // 2. Inicializar fechas mínimas (Hoy) y próximo slot horario disponible
	        const ahora = new Date();
	        // Ajuste local para que no salte al día siguiente después de las 21:00 hs por UTC
	        const offsetMs = ahora.getTimezoneOffset() * 60000;
	        const hoyStr = new Date(ahora.getTime() - offsetMs).toISOString().split('T')[0];
	        
	        diaDesde.min = hoyStr;
	        diaHasta.min = hoyStr;
	        diaDesde.value = hoyStr;
	        diaHasta.value = hoyStr;
	
	        // Preseleccionar la próxima hora en punto (ej. si son 15:20, pone 16:00 a 17:00)
	        let proximaHora = ahora.getHours() + 1;
	        if (proximaHora >= 23) proximaHora = 22; 
	        horaDesde.value = String(proximaHora).padStart(2, '0') + ":00";
	        horaHasta.value = String(proximaHora + 1).padStart(2, '0') + ":00";
	
	        // 3. Función que calcula +12hs / +24hs según el Tipo de Estadía
	        function aplicarReglaTipoEstadia() {
	            if (!diaDesde.value || !horaDesde.value) return;
	
	            // Si todavía no eligió tipo de estadía, igual verificamos slots normales
	            if (!selectTipoEstadia || !selectTipoEstadia.value) {
	                actualizarYVerificarSlots();
	                return;
	            }
	
	            const textoTipo = selectTipoEstadia.options[selectTipoEstadia.selectedIndex].text.toLowerCase();
	            
	            let horasASumar = 0;
	            if (textoTipo.includes("media") || textoTipo.includes("12")) {
	                horasASumar = 12;
	            } else if (textoTipo.includes("completa") || textoTipo.includes("24")) {
	                horasASumar = 24;
	            }
	
	            if (horasASumar > 0) {
	                // Creamos la fecha base con el ingreso seleccionado
	                const [anio, mes, dia] = diaDesde.value.split('-').map(Number);
	                const [hora, min] = horaDesde.value.split(':').map(Number);
	                const fechaCalculada = new Date(anio, mes - 1, dia, hora, min);
	
	                // Sumamos las 12 o 24 horas (Date cambia de día/mes/año automáticamente si cruza las 00:00)
	                fechaCalculada.setHours(fechaCalculada.getHours() + horasASumar);
	
	                // Formateamos de vuelta a YYYY-MM-DD y HH:00
	                const anioSalida = fechaCalculada.getFullYear();
	                const mesSalida = String(fechaCalculada.getMonth() + 1).padStart(2, '0');
	                const diaSalida = String(fechaCalculada.getDate()).padStart(2, '0');
	                const horaSalida = String(fechaCalculada.getHours()).padStart(2, '0') + ":00";
	
	                diaHasta.value = anioSalida + "-" + mesSalida + "-" + diaSalida;
	                horaHasta.value = horaSalida;
	
	                // Bloqueamos visualmente los selectores de salida para que no los modifique
	                diaHasta.readOnly = true;
	                diaHasta.style.pointerEvents = 'none';
	                diaHasta.classList.add('bg-light');
	                horaHasta.style.pointerEvents = 'none';
	                horaHasta.classList.add('bg-light');
	            } else {
	                // Es "Por Hora": liberamos los campos para que elija libremente
	                diaHasta.readOnly = false;
	                diaHasta.style.pointerEvents = 'auto';
	                diaHasta.classList.remove('bg-light');
	                horaHasta.style.pointerEvents = 'auto';
	                horaHasta.classList.remove('bg-light');
	            }
	
	            // Disparamos la verificación de cupo con los valores ya actualizados
	            actualizarYVerificarSlots();
	        }
	
	        // 4. Función que une fecha+hora y consulta el cupo por AJAX
	        function actualizarYVerificarSlots() {
	            if (!diaDesde.value || !horaDesde.value || !diaHasta.value || !horaHasta.value) return;
	
	            // Unimos fecha + hora en los inputs ocultos (Formato: YYYY-MM-DDTHH:00)
	            inputFechaDesde.value = diaDesde.value + "T" + horaDesde.value;
	            inputFechaHasta.value = diaHasta.value + "T" + horaHasta.value;
	            diaHasta.min = diaDesde.value;
	
	            const desdeDate = new Date(inputFechaDesde.value);
	            const hastaDate = new Date(inputFechaHasta.value);
	
	            // Validar que no elija un slot en el pasado ni salida menor a entrada
	            if (desdeDate < new Date()) {
	                mostrarEstadoCupo('warning', '<i class="bi bi-clock-history me-1"></i>El horario de ingreso ya pasó.', false);
	                return;
	            }
	            if (hastaDate <= desdeDate) {
	                mostrarEstadoCupo('warning', '<i class="bi bi-exclamation-triangle me-1"></i>La salida debe ser al menos 1 hora después del ingreso.', false);
	                return;
	            }
	
	            // Si ya eligió cochera, consultamos disponibilidad en vivo
	            if (selectCochera.value) {
	                const url = "<%= request.getContextPath() %>/mis-reservas-user?accion=verificarCupo" +
	                    "&idCochera=" + selectCochera.value +
	                    "&fechaDesde=" + inputFechaDesde.value +
	                    "&fechaHasta=" + inputFechaHasta.value;
	
	                fetch(url)
	                    .then(res => res.json())
	                    .then(data => {
	                        if (data.disponibles > 0) {
	                            mostrarEstadoCupo('success', '<i class="bi bi-check-circle-fill me-1"></i>¡Hay ' + data.disponibles + ' lugar(es) disponible(s) en este horario!', true);
	                        } else {
	                            mostrarEstadoCupo('danger', '<i class="bi bi-x-circle-fill me-1"></i>Cochera completa para este rango horario. Probá otro slot.', false);
	                        }
	                    })
	                    .catch(() => mostrarEstadoCupo('secondary', 'Error al verificar cupo.', true));
	            }
	        }
	
	        function mostrarEstadoCupo(tipoAlerta, mensajeHTML, habilitarBoton) {
	            badgeDisp.className = "alert alert-" + tipoAlerta + " py-2 px-3 small fw-bold mb-3 border-0";
	            badgeDisp.innerHTML = mensajeHTML;
	            if (btnSubmit && btnSubmit.innerText !== "Falta cargar un vehículo") {
	                btnSubmit.disabled = !habilitarBoton;
	            }
	        }
	
	        // 5. Asignación de Eventos (Listeners)
	        // Si cambia tipo de estadía o la fecha/hora de entrada -> recalcula salida (+12/+24) y luego verifica cupo
	        if (selectTipoEstadia) {
	            selectTipoEstadia.addEventListener('change', aplicarReglaTipoEstadia);
	        }
	        diaDesde.addEventListener('change', aplicarReglaTipoEstadia);
	        horaDesde.addEventListener('change', aplicarReglaTipoEstadia);
	
	        // Si cambia la cochera o la fecha/hora de salida manualmente (en modo "Por Hora") -> solo verifica cupo
	        selectCochera.addEventListener('change', actualizarYVerificarSlots);
	        diaHasta.addEventListener('change', actualizarYVerificarSlots);
	        horaHasta.addEventListener('change', actualizarYVerificarSlots);
	
	        // Ejecutar una vez al cargar la página
	        actualizarYVerificarSlots();
	
	        // Alertas Flash
	        <% if (error != null) { %>
	            Swal.fire({ icon: 'error', title: 'Operación denegada', text: `<%= error %>` });
	            <% session.removeAttribute("error"); %>
	        <% } %>
	        
	        <% if (exito != null) { %>
	            Swal.fire({ icon: 'success', title: '¡Reserva Exitosa!', text: `<%= exito %>` });
	            <% session.removeAttribute("exito"); %>
	        <% } %>
	        
	        // FUNCIÓN PARA DIBUJAR EL QR
	        function generarPaseQR(patente, usuario, tipoEstadia, fechaDesde) {
	            const cadenaQR = patente + "|" + usuario + "|" + tipoEstadia + "|" + fechaDesde;
	
	            new QRious({
	                element: document.getElementById('canvasQR'),
	                value: cadenaQR,
	                size: 220,      
	                level: 'H'      
	            });
	
	            const modal = new bootstrap.Modal(document.getElementById('modalQR'));
	            modal.show();
	        }
	    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

	<!-- Modal para el QR De entrada/salida/entradaParcial-->
	<div class="modal fade" id="modalQR" tabindex="-1" aria-hidden="true">
	    <div class="modal-dialog modal-dialog-centered modal-sm">
	        <div class="modal-content rounded-4 shadow">
	            <div class="modal-header border-0 pb-0">
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
	            </div>
	            <div class="modal-body text-center pb-4">
	                <h5 class="fw-bold text-dark mb-3">Tu Pase de Acceso</h5>
	                <!-- QRious dibuja el código dinámicamente -->
	                <canvas id="canvasQR" class="mb-3 shadow-sm rounded-3 border p-2"></canvas>
	                <p class="small text-muted mb-0">Acercá este código al lector de la barrera al ingresar y al salir.</p>
	            </div>
	        </div>
	    </div>
	</div>
</body>
</html>