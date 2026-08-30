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
                <div class="card border-0 shadow-sm rounded-4 sticky-top" style="top: 20px;">
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
                                <select class="form-select" name="patente" required>
                                    <option value="" disabled selected>Selecciona tu vehículo...</option>
                                    <% 
                                        List<Vehiculo> misVehiculos = (List<Vehiculo>) request.getAttribute("misVehiculos");
                                        if (misVehiculos != null && !misVehiculos.isEmpty()) {
                                            for(Vehiculo v : misVehiculos) {
                                    %>
                                        <option value="<%= v.getPatente() %>"><%= v.getPatente() %> - <%= v.getDescripcion() %></option>
                                    <% 
                                            }
                                        } else { 
                                    %>
                                        <!-- Mockup manual por si la lista viene vacía mientras conectan la BD -->
                                        <option value="AD456TR">AD456TR - Ford Taunus (Auto)</option>
                                        <option value="A012BCD">A012BCD - Honda XR (Moto)</option>
                                    <% } %>
                                </select>
                            </div>

                            <!-- Selección de Estadía -->
                            <div class="mb-3">
                                <label class="form-label text-muted small fw-bold">Tipo de Estadía</label>
                                <select class="form-select" name="idTipoEstadia" required>
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

                            <!-- Fechas -->
                            <div class="mb-3">
                                <label class="form-label text-muted small fw-bold">Ingreso (Estimado)</label>
                                <input type="datetime-local" class="form-control" id="fechaDesde" name="fechaDesde" required>
                            </div>

                            <div class="mb-4">
                                <label class="form-label text-muted small fw-bold">Salida (Tentativa)</label>
                                <input type="datetime-local" class="form-control" id="fechaHasta" name="fechaHasta" required>
                            </div>

                            <button type="submit" class="btn btn-info w-100 py-2 fw-bold text-white fs-5">
                                <i class="bi bi-check-circle-fill me-2"></i>Reservar Lugar
                            </button>
                            
                            <div class="text-center mt-3 small text-muted">
                                *Si cuentas con un plan <strong>Premium</strong> o <strong>Medium</strong>, los descuentos se aplicarán automáticamente.
                            </div>
                        </form>
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
                                    %>
                                    <tr>
                                        <td>
                                            <div class="fw-bold"><%= r.getId().getFechaDesde().format(dtf) %></div>
                                            <div class="small text-muted"><%= r.getTipoEstadia().getDescripcion() %></div>
                                        </td>
                                        <td class="fw-bold"><%= r.getVehiculo().getPatente() %></td>
                                        <td>
                                            <% if (r.getLugar() != null) { %>
                                                <span class="badge bg-light text-dark border"><i class="bi bi-p-square-fill me-1 text-primary"></i>Lugar <%= r.getLugar().getCodigo() %></span>
                                            <% } else { %>
                                                <span class="text-muted small">N/A</span>
                                            <% } %>
                                        </td>
                                        <td>
                                            <span class="badge <%= colorBadge %> estado-badge rounded-pill"><%= r.getEstado().name() %></span>
                                        </td>
                                        <td>
                                            <% if (r.getEstado() == EstadoReserva.PENDIENTE) { %>
                                                <button class="btn btn-outline-danger btn-sm" onclick="cancelarReserva('<%= r.getVehiculo().getPatente() %>')"><i class="bi bi-x-circle"></i> Cancelar</button>
                                            <% } else if (r.getEstado() == EstadoReserva.EN_CURSO) { %>
                                                <span class="small text-muted fst-italic">Vehículo en cochera</span>
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
        // 1. Evitar que el usuario seleccione fechas anteriores a la hora actual
        const now = new Date();
        now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
        const minDatetime = now.toISOString().slice(0,16);
        
        document.getElementById('fechaDesde').min = minDatetime;
        
        // 2. Ajustar el 'min' de fechaHasta basado en lo que elija en fechaDesde
        document.getElementById('fechaDesde').addEventListener('change', function() {
            document.getElementById('fechaHasta').min = this.value;
            // Opcional: auto-setear fechaHasta a 1 hora después del ingreso por defecto
        });

        // 3. Validación al enviar el formulario
        document.getElementById('formReserva').addEventListener('submit', function(e) {
            const desde = new Date(document.getElementById('fechaDesde').value);
            const hasta = new Date(document.getElementById('fechaHasta').value);
            
            if (hasta <= desde) {
                e.preventDefault();
                Swal.fire({ icon: 'warning', title: 'Fechas inválidas', text: 'La fecha de salida debe ser posterior a la de ingreso.' });
            }
        });

        // Alertas Flash
        <% if (error != null) { %>
            Swal.fire({ icon: 'error', title: 'Operación denegada', text: `<%= error %>` });
            <% session.removeAttribute("error"); %>
        <% } %>
        
        <% if (exito != null) { %>
            Swal.fire({ icon: 'success', title: '¡Reserva Exitosa!', text: `<%= exito %>` });
            <% session.removeAttribute("exito"); %>
        <% } %>
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>