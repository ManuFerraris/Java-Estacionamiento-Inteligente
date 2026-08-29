<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="estacionamiento.domain.Cochera" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Cocheras</title>
    <!-- CSS Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-light">

	<%@ include file="navbar.jsp" %>
	
    <div class="container mt-5">
        <h1 class="mb-4 text-secondary">Administración de Cocheras</h1>

        <div class="row">
            <!-- COLUMNA IZQUIERDA: Formulario de Alta/Edición -->
            <div class="col-md-4">
                <div class="card shadow-sm mb-4 border-0">
                    <div class="card-header bg-primary text-white">
                        <!-- ID agregado para cambiar el título dinámicamente -->
                        <h5 class="mb-0" id="tituloFormulario"><i class="bi bi-plus-circle me-2"></i>Registrar Nueva Cochera</h5>
                    </div>
                    <div class="card-body">
                        <form action="<%= request.getContextPath() %>/cocheras" method="POST" id="formCochera">
                            <input type="hidden" name="accion" id="accionForm" value="crear">
                            <input type="hidden" name="codigo" id="codigoCochera" value="">
                            
                            <div class="mb-3">
                                <label for="nombre" class="form-label text-muted fw-bold">Nombre de la Cochera</label>
                                <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Ej. Estacionamiento Centro" required>
                            </div>
                            <div class="mb-3">
                                <label for="descripcion" class="form-label text-muted fw-bold">Descripción</label>
                                <input type="text" class="form-control" id="descripcion" name="descripcion" placeholder="Ej. Abierto 24hs, solo autos">
                            </div>
                            <div class="mb-3">
                                <label for="direccion" class="form-label text-muted fw-bold">Dirección</label>
                                <input type="text" class="form-control" id="direccion" name="direccion" placeholder="Ej. San Martín 1234" required>
                            </div>
                            
                            <!-- Botones con íconos -->
                            <button type="submit" class="btn btn-success w-100 mb-2" id="btnGuardar">
                                <i class="bi bi-save me-2"></i>Guardar Cochera
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
                    <!-- Cabecera más limpia -->
                    <div class="card-header bg-white border-bottom">
                        <h5 class="mb-0 text-dark"><i class="bi bi-list-ul me-2"></i>Listado de Cocheras</h5>
                    </div>
                    <div class="card-body p-0">
                        <table class="table table-striped table-hover mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-3">Código</th>
                                    <th>Nombre</th>
                                    <th>Descripción</th>
                                    <th>Dirección</th>
                                    <th class="text-center">Acciones</th>
                                </tr>
                            </thead>
                            <!-- align-middle centra todo verticalmente -->
                            <tbody class="align-middle">
                                <%
                                    List<Cochera> lista = (List<Cochera>) request.getAttribute("listaCocheras");
                                    if (lista != null && !lista.isEmpty()) {
                                        for (Cochera cochera : lista) {
                                %>
                                            <tr>
                                                <td class="ps-3 fw-bold text-secondary"><%= cochera.getCodigo() %></td>
                                                <td class="fw-semibold"><%= cochera.getNombre() %></td>
                                                <td>
                                                    <% if ("Inactiva".equalsIgnoreCase(cochera.getDescripcion())) { %>
                                                        <span class="badge bg-danger"><i class="bi bi-dash-circle me-1"></i>Inactiva</span>
                                                    <% } else { %>
                                                        <%= cochera.getDescripcion() %>
                                                    <% } %>
                                                </td>
                                                <td><%= cochera.getDireccion() %></td>
                                                <td>
                                                    <!-- Contenedor flex para agrupar botones -->
                                                    <div class="d-flex justify-content-center gap-2">
                                                        <!-- Botón Editar con ícono -->
                                                        <button type="button" class="btn btn-warning btn-sm text-dark" 
                                                                title="Editar Cochera"
                                                                onclick="cargarDatosEdicion('<%= cochera.getCodigo() %>', '<%= cochera.getNombre() %>', '<%= cochera.getDescripcion() %>', '<%= cochera.getDireccion() %>')">
                                                            <i class="bi bi-pencil-square"></i>
                                                        </button>
                            
                                                        <!-- Botón Baja Lógica con ícono outline -->
                                                        <form action="<%= request.getContextPath() %>/cocheras" method="POST" class="m-0">
                                                            <input type="hidden" name="accion" value="bajaLogica">
                                                            <input type="hidden" name="codigo" value="<%= cochera.getCodigo() %>">
                                                            <button type="submit" class="btn btn-outline-danger btn-sm" 
                                                                    title="Dar de Baja"
                                                                    onclick="return confirm('¿Seguro que deseas marcar esta cochera como inactiva?');">
                                                                <i class="bi bi-ban"></i>
                                                            </button>
                                                        </form>
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
                                                No hay cocheras registradas en el sistema.
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
        function cargarDatosEdicion(codigo, nombre, descripcion, direccion) {
            document.getElementById('nombre').value = nombre;
            document.getElementById('descripcion').value = descripcion;
            document.getElementById('direccion').value = direccion;
            
            document.getElementById('codigoCochera').value = codigo;
            document.getElementById('accionForm').value = 'editar';
            
            // Actualizar Título del Formulario
            document.getElementById('tituloFormulario').innerHTML = '<i class="bi bi-pencil-square me-2"></i>Editar Cochera';
            
            // Actualizar diseño del botón
            let btn = document.getElementById('btnGuardar');
            btn.innerHTML = '<i class="bi bi-arrow-clockwise me-2"></i>Actualizar Cochera';
            btn.className = 'btn btn-warning w-100 mb-2 fw-bold text-dark';
            
            document.getElementById('btnCancelar').classList.remove('d-none');
        }
        
        function cancelarEdicion() {
            document.getElementById('nombre').value = '';
            document.getElementById('descripcion').value = '';
            document.getElementById('direccion').value = '';
            
            document.getElementById('codigoCochera').value = '';
            document.getElementById('accionForm').value = 'crear';
            
            // Restaurar Título del Formulario
            document.getElementById('tituloFormulario').innerHTML = '<i class="bi bi-plus-circle me-2"></i>Registrar Nueva Cochera';
            
            // Restaurar diseño del botón
            let btn = document.getElementById('btnGuardar');
            btn.innerHTML = '<i class="bi bi-save me-2"></i>Guardar Cochera';
            btn.className = 'btn btn-success w-100 mb-2';
            
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