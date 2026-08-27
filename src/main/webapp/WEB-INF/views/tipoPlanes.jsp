<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="estacionamiento.domain.TipoPlan" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Tipos de Plan</title>
    <!-- CSS Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-light">

    <div class="container mt-5">
        <h1 class="mb-4 text-secondary">Administración de Tipos de Plan</h1>

        <div class="row">
            <!-- COLUMNA IZQUIERDA: Formulario de Alta/Edición -->
            <div class="col-md-4">
                <div class="card shadow-sm mb-4 border-0">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0" id="tituloFormulario"><i class="bi bi-tag-fill me-2"></i>Registrar Nuevo Plan</h5>
                    </div>
                    <div class="card-body">
                        <form action="<%= request.getContextPath() %>/tipoPlanes" method="POST" id="formTipoPlan">
                            <input type="hidden" name="accion" id="accionForm" value="crear">
                            <input type="hidden" name="codigo" id="codigoTipoPlan" value="">
                            
                            <div class="mb-3">
                                <label for="nombre" class="form-label text-muted fw-bold">Nombre del Plan</label>
                                <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Ej. Básico, De Lujo" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="detalle" class="form-label text-muted fw-bold">Detalle / Descripción</label>
                                <textarea class="form-control" id="detalle" name="detalle" rows="3" placeholder="Ej. Cubre estadía por hora sin beneficios extra..."></textarea>
                            </div>
                            
                            <!-- Botones con íconos -->
                            <button type="submit" class="btn btn-success w-100 mb-2 mt-2" id="btnGuardar">
                                <i class="bi bi-save me-2"></i>Guardar Plan
                            </button>
                            <button type="button" class="btn btn-secondary w-100 d-none" id="btnCancelar" onclick="cancelarEdicion()">
                                <i class="bi bi-x-circle me-2"></i>Cancelar Edición
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- COLUMNA DERECHA: Tabla con el listado -->
            <div class="col-md-8">
                <div class="card shadow-sm border-0">
                    <div class="card-header bg-white border-bottom">
                        <h5 class="mb-0 text-dark"><i class="bi bi-tags-fill me-2"></i>Listado de Planes Disponibles</h5>
                    </div>
                    <div class="card-body p-0 table-responsive">
                        <table class="table table-striped table-hover mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-3">Código</th>
                                    <th>Nombre</th>
                                    <th>Detalle</th>
                                    <th>Estado</th>
                                    <th class="text-center">Acciones</th>
                                </tr>
                            </thead>
                            <tbody class="align-middle">
                                <%
                                    List<TipoPlan> lista = (List<TipoPlan>) request.getAttribute("listaTiposPlan");
                                    if (lista != null && !lista.isEmpty()) {
                                        for (TipoPlan plan : lista) {
                                %>
                                            <tr>
                                                <td class="ps-3 fw-bold text-secondary"><%= plan.getCodigo() %></td>
                                                <td class="fw-semibold text-primary"><%= plan.getNombre() %></td>
                                                <td>
                                                    <!-- Mostramos un extracto si el detalle es muy largo (UX) -->
                                                    <span title="<%= plan.getDetalle() != null ? plan.getDetalle() : "" %>">
                                                        <%= plan.getDetalle() != null && plan.getDetalle().length() > 50 ? plan.getDetalle().substring(0, 47) + "..." : (plan.getDetalle() != null ? plan.getDetalle() : "-") %>
                                                    </span>
                                                </td>
                                                <td>
                                                    <% if (plan.getFechaBaja() != null) { %>
                                                        <span class="badge bg-danger"><i class="bi bi-dash-circle me-1"></i>Inactivo</span>
                                                    <% } else { %>
                                                        <span class="badge bg-success"><i class="bi bi-check-circle me-1"></i>Activo</span>
                                                    <% } %>
                                                </td>
                                                <td>
                                                    <div class="d-flex justify-content-center gap-2">
                                                        <!-- Botón Editar -->
                                                        <button type="button" class="btn btn-warning btn-sm text-dark" 
                                                                title="Editar Plan"
                                                                onclick="cargarDatosEdicion('<%= plan.getCodigo() %>', '<%= plan.getNombre() %>', '<%= plan.getDetalle() != null ? plan.getDetalle().replace("'", "\\'") : "" %>')">
                                                            <i class="bi bi-pencil-square"></i>
                                                        </button>
                            
                                                        <% if (plan.getFechaBaja() != null) { %>
                                                            <!-- Botón Alta Lógica -->
                                                            <form action="<%= request.getContextPath() %>/tipoPlanes" method="POST" class="m-0">
                                                                <input type="hidden" name="accion" value="altaLogica">
                                                                <input type="hidden" name="codigo" value="<%= plan.getCodigo() %>">
                                                                <button type="submit" class="btn btn-outline-success btn-sm" 
                                                                        title="Reactivar Plan"
                                                                        onclick="return confirm('¿Seguro que deseas volver a habilitar este plan?');">
                                                                    <i class="bi bi-check-circle"></i>
                                                                </button>
                                                            </form>
                                                        <% } else { %>
                                                            <!-- Botón Baja Lógica -->
                                                            <form action="<%= request.getContextPath() %>/tipoPlanes" method="POST" class="m-0">
                                                                <input type="hidden" name="accion" value="bajaLogica">
                                                                <input type="hidden" name="codigo" value="<%= plan.getCodigo() %>">
                                                                <button type="submit" class="btn btn-outline-danger btn-sm" 
                                                                        title="Dar de Baja"
                                                                        onclick="return confirm('¿Seguro que deseas dar de baja este plan?');">
                                                                    <i class="bi bi-ban"></i>
                                                                </button>
                                                            </form>
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
                                                No hay tipos de plan registrados.
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
        function cargarDatosEdicion(codigo, nombre, detalle) {
            document.getElementById('codigoTipoPlan').value = codigo;
            document.getElementById('accionForm').value = 'editar';
            
            document.getElementById('nombre').value = nombre;
            document.getElementById('detalle').value = detalle;
            
            // Actualizar Título
            document.getElementById('tituloFormulario').innerHTML = '<i class="bi bi-pencil-square me-2"></i>Editar Plan';
            
            // Actualizar diseño del botón
            let btn = document.getElementById('btnGuardar');
            btn.innerHTML = '<i class="bi bi-arrow-clockwise me-2"></i>Actualizar Plan';
            btn.className = 'btn btn-warning w-100 mb-2 mt-2 fw-bold text-dark';
            
            document.getElementById('btnCancelar').classList.remove('d-none');
        }
        
        function cancelarEdicion() {
            document.getElementById('accionForm').value = 'crear';
            document.getElementById('codigoTipoPlan').value = '';
            
            document.getElementById('nombre').value = '';
            document.getElementById('detalle').value = '';
            
            document.getElementById('tituloFormulario').innerHTML = '<i class="bi bi-tag-fill me-2"></i>Registrar Nuevo Plan';
            
            let btn = document.getElementById('btnGuardar');
            btn.innerHTML = '<i class="bi bi-save me-2"></i>Guardar Plan';
            btn.className = 'btn btn-success w-100 mb-2 mt-2';
            
            document.getElementById('btnCancelar').classList.add('d-none');
        }
    
        <% if (error != null && !error.isEmpty()) { %>
            Swal.fire({
                icon: 'error',
                title: 'Hubo un problema',
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