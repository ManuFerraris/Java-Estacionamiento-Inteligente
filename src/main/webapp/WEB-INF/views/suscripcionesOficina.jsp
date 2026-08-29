<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="estacionamiento.domain.Suscripcion" %>
<%@ page import="estacionamiento.domain.Usuario" %>
<%@ page import="estacionamiento.domain.TipoPlan" %>
<%@ page import="estacionamiento.domain.EstadoSuscripcion" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Suscripciones</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-light">

	<%@ include file="navbar.jsp" %>
	
    <div class="container-fluid mt-5 px-4">
        <h1 class="mb-4 text-secondary">Administración de Suscripciones</h1>

        <div class="row">
            <!-- COLUMNA IZQUIERDA: Formulario de Alta / Upgrade -->
            <div class="col-lg-4 col-md-12">
                <div class="card shadow-sm mb-4 border-0">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0"><i class="bi bi-person-vcard me-2"></i>Asignar / Mejorar Plan</h5>
                    </div>
                    <div class="card-body">
                        <form action="<%= request.getContextPath() %>/suscripciones-oficina" method="POST" id="formSuscripcion">
                            <input type="hidden" name="accion" value="crear">
                            
                            <div class="mb-3">
                                <label for="numeroUsuario" class="form-label text-muted fw-bold">Usuario (Cliente)</label>
                                <select class="form-select" id="numeroUsuario" name="numeroUsuario" required>
                                    <option value="" disabled selected>Seleccione un usuario...</option>
                                    <% 
                                        List<Usuario> listaUsuarios = (List<Usuario>) request.getAttribute("listaUsuarios");
                                        if (listaUsuarios != null) {
                                            for (Usuario u : listaUsuarios) {
                                                // Asumimos que no asignamos suscripciones a usuarios dados de baja
                                                if(u.getFechaBaja() == null) {
                                    %>
                                                <option value="<%= u.getNumero() %>"><%= u.getNombre() %> <%= u.getApellido() %> (User: <%= u.getNombreUsuario() %>)</option>
                                    <% 
                                                }
                                            }
                                        } 
                                    %>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="codigoPlan" class="form-label text-muted fw-bold">Plan a Contratar</label>
                                <select class="form-select" id="codigoPlan" name="codigoPlan" required>
                                    <option value="" disabled selected>Seleccione el nuevo plan...</option>
                                    <% 
                                        List<TipoPlan> listaPlanes = (List<TipoPlan>) request.getAttribute("listaTiposPlan");
                                        if (listaPlanes != null) {
                                            for (TipoPlan plan : listaPlanes) {
                                                if (plan.getFechaBaja() == null) {
                                    %>
                                                <option value="<%= plan.getCodigo() %>"><%= plan.getNombre() %></option>
                                    <% 
                                                }
                                            }
                                        } 
                                    %>
                                </select>
                                <div class="form-text mt-2">
                                    <i class="bi bi-info-circle me-1"></i>Si el usuario ya tiene un plan activo, este será reemplazado (Upgrade).
                                </div>
                            </div>
                            
                            <button type="submit" class="btn btn-success w-100 mt-3" id="btnGuardar">
                                <i class="bi bi-check2-circle me-2"></i>Confirmar Suscripción
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- COLUMNA DERECHA: Tabla con el listado -->
            <div class="col-lg-8 col-md-12">
                <div class="card shadow-sm border-0">
                    <div class="card-header bg-white border-bottom">
                        <h5 class="mb-0 text-dark"><i class="bi bi-card-checklist me-2"></i>Historial de Suscripciones</h5>
                    </div>
                    <div class="card-body p-0 table-responsive">
                        <table class="table table-striped table-hover mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-3">Usuario</th>
                                    <th>Plan</th>
                                    <th>Periodo de Vigencia</th>
                                    <th>Estado</th>
                                    <th class="text-center">Acciones</th>
                                </tr>
                            </thead>
                            <tbody class="align-middle">
                                <%
                                    List<Suscripcion> lista = (List<Suscripcion>) request.getAttribute("listaSuscripciones");
                                    DateTimeFormatter formatoVisual = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                                    
                                    if (lista != null && !lista.isEmpty()) {
                                        for (Suscripcion s : lista) {
                                            // Asumiendo los getters de tu clase Suscripcion
                                            int numUsuario = s.getId().getNumero();
                                            int codPlan = s.getId().getCodigo();
                                            String fechaIso = s.getId().getFechaDesde().toString();
                                            
                                            String fechaDesdeVisual = s.getId().getFechaDesde().format(formatoVisual);
                                            String fechaHastaVisual = s.getFechaHasta() != null ? s.getFechaHasta().format(formatoVisual) : "-";
                                %>
                                            <tr>
                                                <td class="ps-3 fw-semibold text-dark">
                                                    <%= s.getUsuario().getNombre() %> <%= s.getUsuario().getApellido() %>
                                                </td>
                                                <td class="text-primary fw-bold"><%= s.getTipoPlan().getNombre() %></td>
                                                <td>
                                                    <small class="d-block text-muted">Desde: <%= fechaDesdeVisual %></small>
                                                    <small class="d-block text-muted">Hasta: <%= fechaHastaVisual %></small>
                                                </td>
                                                <td>
                                                    <% if (s.getEstado() == EstadoSuscripcion.ACTIVA) { %>
                                                        <span class="badge bg-success"><i class="bi bi-check-circle me-1"></i>Activa</span>
                                                    <% } else if (s.getEstado() == EstadoSuscripcion.PAUSADA) { %>
                                                        <span class="badge bg-warning text-dark"><i class="bi bi-pause-circle me-1"></i>Pausada</span>
                                                    <% } else { %>
                                                        <span class="badge bg-danger"><i class="bi bi-x-circle me-1"></i>Cancelada</span>
                                                    <% } %>
                                                </td>
                                                <td>
                                                    <div class="d-flex justify-content-center gap-2">
                                                        <% if (s.getEstado() != EstadoSuscripcion.CANCELADA) { %>
                                                            <!-- Botón para Cancelar Manualmente -->
                                                            <form action="<%= request.getContextPath() %>/suscripciones-oficina" method="POST" class="m-0">
                                                                <input type="hidden" name="accion" value="cancelar">
                                                                <!-- Las TRES partes de tu clave compuesta -->
                                                                <input type="hidden" name="numeroUsuario" value="<%= numUsuario %>">
                                                                <input type="hidden" name="codigoPlan" value="<%= codPlan %>">
                                                                <input type="hidden" name="fechaDesde" value="<%= fechaIso %>">
                                                                
                                                                <button type="submit" class="btn btn-outline-danger btn-sm" 
                                                                        title="Cancelar Suscripción"
                                                                        onclick="return confirm('¿Seguro que deseas cancelar esta suscripción de forma manual?');">
                                                                    <i class="bi bi-x-octagon-fill"></i>
                                                                </button>
                                                            </form>
                                                        <% } else { %>
                                                            <span class="text-muted"><small>Sin acciones</small></span>
                                                        <% } %>
                                                    </div>
                                                </td>
                                            </tr>
                                <%
                                        }
                                    } else {
                                %>
                                        <tr>
                                            <td colspan="5" class="text-center text-muted py-4">
                                                <i class="bi bi-inbox fs-2 d-block mb-2"></i>
                                                No hay suscripciones registradas en el sistema.
                                            </td>
                                        </tr>
                                <%
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <% 
        String error = (String) request.getAttribute("error");
        String exito = (String) request.getAttribute("exito");
    %>
    
    <script>
        <% if (error != null && !error.isEmpty()) { %>
            Swal.fire({
                icon: 'error',
                title: 'Operación denegada',
                text: `<%= error %>`,
                confirmButtonColor: '#d33'
            });
        <% } %>

        <% if (exito != null && !exito.isEmpty()) { %>
            Swal.fire({
                icon: 'success',
                title: '¡Operación Exitosa!',
                text: `<%= exito %>`,
                confirmButtonColor: '#198754'
            });
        <% } %>
    </script>
</body>
</html>