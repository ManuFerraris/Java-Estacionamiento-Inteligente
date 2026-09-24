<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="estacionamiento.domain.Lugar" %>
<%@ page import="estacionamiento.domain.Cochera" %>
<%@ page import="estacionamiento.domain.TipoEstadia" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Lugares</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-light">

    <%@ include file="navbar.jsp" %>

    <div class="container mt-3">
    <a href="<%= request.getContextPath() %>/dashboard-oficina" class="btn btn-sm btn-outline-secondary mb-3 shadow-sm">
            <i class="bi bi-arrow-left-circle me-1"></i>Volver al Menú Principal
        </a>
        <h1 class="mb-4 text-secondary">Control de Espacios Físicos</h1>

        <div class="row">
            <!-- Formulario Reutilizable (Alta/Edición) -->
            <div class="col-md-4">
                <div class="card shadow-sm mb-4 border-0">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0" id="tituloFormulario"><i class="bi bi-geo-alt me-2"></i>Nuevo Lugar</h5>
                    </div>
                    <div class="card-body">
                        <form action="LugarServlet" method="POST">
                            <input type="hidden" name="accion" id="accionForm" value="crear">
                            <input type="hidden" name="codigoLugarEdit" id="codigoLugarEdit" value="">
                            
                            <div class="mb-3">
                                <label class="form-label text-muted fw-bold">Número de Piso</label>
                                <input type="number" class="form-control" name="numeroPiso" id="numeroPiso" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label text-muted fw-bold">Descripción</label>
                                <input type="text" class="form-control" name="descripcion" id="descripcion" placeholder="Ej: Techado, Discapacitados" required>
                            </div>
                            
                            <div class="mb-4">
                                <label class="form-label text-muted fw-bold">Pertenece a Cochera</label>
                                <select class="form-select" name="codigoCochera" id="codigoCochera" required>
                                    <option value="" disabled selected>Seleccione la cochera...</option>
                                    <% 
                                        List<Cochera> cocheras = (List<Cochera>) request.getAttribute("listaCocheras");
                                        if (cocheras != null && !cocheras.isEmpty()) {
                                            for (Cochera c : cocheras) { 
                                    %>
                                                <option value="<%= c.getCodigo() %>"><%= c.getNombre() %> - <%= c.getDireccion() %></option>
                                    <% 
                                            }
                                        } 
                                    %>
                                </select>
                            </div>
                            
                            <button type="submit" class="btn btn-success w-100 mb-2" id="btnGuardar">Guardar Lugar</button>
                            <button type="button" class="btn btn-secondary w-100 d-none" id="btnCancelar" onclick="cancelarEdicion()">Cancelar Edición</button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Tabla del Mapa de Lugares -->
            <div class="col-md-8">
                <div class="card shadow-sm border-0">
                    <div class="card-header bg-white border-bottom">
                        <h5 class="mb-0 text-dark"><i class="bi bi-grid-3x3 me-2"></i>Mapa de Lugares</h5>
                    </div>
                    <div class="card-body p-0">
                        <table class="table table-striped table-hover mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-3">Cód</th>
                                    <th>Piso</th>
                                    <th>Cochera</th>
                                    <th>Descripción</th>
                                    <th class="text-center">Acciones</th>
                                </tr>
                            </thead>
                            <tbody class="align-middle">
                                <%
                                    List<Lugar> lugares = (List<Lugar>) request.getAttribute("listaLugares");
                                    if (lugares != null && !lugares.isEmpty()) {
                                        for (Lugar l : lugares) {
                                %>
                                            <tr>
                                                <td class="ps-3 fw-bold text-primary">#<%= l.getCodigo() %></td>
                                                <td><span class="badge bg-secondary">Piso <%= l.getNumeroPiso() %></span></td>
                                                <td><%= (l.getCochera() != null) ? l.getCochera().getNombre() : "N/A" %></td>
                                                <td>
                                                    <% if ("Inactivo".equalsIgnoreCase(l.getDescripcion())) { %>
                                                        <span class="badge bg-danger">Inactivo</span>
                                                    <% } else { %>
                                                        <%= l.getDescripcion() %>
                                                    <% } %>
                                                </td>
                                                <td>
                                                    <div class="d-flex justify-content-center gap-2">
                                                        <!-- BOTÓN ASIGNAR ESTADÍA -->
                                                        <button class="btn btn-sm btn-outline-primary" title="Asignar Estadía"
                                                                onclick="abrirModalAsignacion('<%= l.getCodigo() %>')">
                                                            <i class="bi bi-tags-fill"></i>
                                                        </button>
                                                        
                                                        <!-- BOTÓN EDITAR -->
                                                        <button class="btn btn-sm btn-warning text-dark" title="Editar"
                                                                onclick="cargarEdicion('<%= l.getCodigo() %>', '<%= l.getNumeroPiso() %>', '<%= l.getDescripcion() %>', '<%= (l.getCochera() != null) ? l.getCochera().getCodigo() : "" %>')">
                                                            <i class="bi bi-pencil-square"></i>
                                                        </button>
                                                        
                                                        <!-- BOTÓN BAJA LÓGICA -->
                                                        <form action="LugarServlet" method="POST" class="m-0">
                                                            <input type="hidden" name="accion" value="bajaLogica">
                                                            <input type="hidden" name="codigoLugar" value="<%= l.getCodigo() %>">
                                                            <button type="submit" class="btn btn-outline-danger btn-sm" title="Dar de Baja"
                                                                    onclick="return confirm('¿Marcar este lugar como inactivo?');">
                                                                <i class="bi bi-ban"></i>
                                                            </button>
                                                        </form>
                                                    </div>
                                                </td>
                                            </tr>
                                <%      }
                                    } else { %>
                                        <tr><td colspan="5" class="text-center text-muted py-4">No hay lugares registrados.</td></tr>
                                <%  } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- MODAL (Lugar - Tipo Estadía) se mantiene igual -->
    <div class="modal fade" id="modalEstadia" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title">Asignar Tipo de Estadía</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form action="LugarTipoEstadiaServlet" method="POST">
                    <input type="hidden" name="accion" value="asignar">
                    <div class="modal-body">
                        <input type="hidden" name="codigoLugar" id="modalCodigoLugar">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Tipo de Estadía</label>
                            <select class="form-select" name="numeroTipoEstadia" required>
                                <% 
                                    List<TipoEstadia> tipos = (List<TipoEstadia>) request.getAttribute("listaTiposEstadia");
                                    if (tipos != null) {
                                        for (TipoEstadia t : tipos) { 
                                %>
                                            <option value="<%= t.getNumero() %>"><%= t.getDescripcion() %></option>
                                <%      } } %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">Fecha Desde</label>
                            <input type="datetime-local" class="form-control" name="fechaDesde" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-primary">Guardar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        <% if (request.getAttribute("error") != null) { %>
            Swal.fire({ icon: 'error', title: 'Error', text: '<%= request.getAttribute("error") %>' });
        <% } %>
        <% if (request.getAttribute("exito") != null) { %>
            Swal.fire({ icon: 'success', title: '¡Éxito!', text: '<%= request.getAttribute("exito") %>' });
        <% } %>
        
        function abrirModalAsignacion(codigo) {
            document.getElementById('modalCodigoLugar').value = codigo;
            new bootstrap.Modal(document.getElementById('modalEstadia')).show();
        }

        function cargarEdicion(codigo, piso, descripcion, cocheraId) {
            document.getElementById('codigoLugarEdit').value = codigo;
            document.getElementById('numeroPiso').value = piso;
            document.getElementById('descripcion').value = descripcion;
            document.getElementById('codigoCochera').value = cocheraId;
            
            document.getElementById('accionForm').value = 'editar';
            document.getElementById('tituloFormulario').innerHTML = '<i class="bi bi-pencil-square me-2"></i>Editar Lugar';
            
            let btn = document.getElementById('btnGuardar');
            btn.innerHTML = 'Actualizar Lugar';
            btn.className = 'btn btn-warning w-100 mb-2 fw-bold text-dark';
            document.getElementById('btnCancelar').classList.remove('d-none');
        }

        function cancelarEdicion() {
            document.getElementById('codigoLugarEdit').value = '';
            document.getElementById('numeroPiso').value = '';
            document.getElementById('descripcion').value = '';
            document.getElementById('codigoCochera').value = '';
            
            document.getElementById('accionForm').value = 'crear';
            document.getElementById('tituloFormulario').innerHTML = '<i class="bi bi-geo-alt me-2"></i>Nuevo Lugar';
            
            let btn = document.getElementById('btnGuardar');
            btn.innerHTML = 'Guardar Lugar';
            btn.className = 'btn btn-success w-100 mb-2';
            document.getElementById('btnCancelar').classList.add('d-none');
        }
    </script>
</body>
</html>