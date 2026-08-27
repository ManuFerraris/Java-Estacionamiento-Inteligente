<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="estacionamiento.domain.Usuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Usuarios</title>
    <!-- CSS Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-light">

    <div class="container-fluid mt-5 px-4">
        <h1 class="mb-4 text-secondary">Administración de Usuarios</h1>

        <div class="row">
            <!-- COLUMNA IZQUIERDA: Formulario de Alta/Edición -->
            <div class="col-lg-4 col-md-12">
                <div class="card shadow-sm mb-4 border-0">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0" id="tituloFormulario"><i class="bi bi-person-plus-fill me-2"></i>Registrar Nuevo Usuario</h5>
                    </div>
                    <div class="card-body">
                        <form action="<%= request.getContextPath() %>/usuarios" method="POST" id="formUsuario">
                            <input type="hidden" name="accion" id="accionForm" value="crear">
                            <input type="hidden" name="numero" id="numeroUsuario" value="">
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="nombre" class="form-label text-muted fw-bold">Nombre</label>
                                    <input type="text" class="form-control" id="nombre" name="nombre" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="apellido" class="form-label text-muted fw-bold">Apellido</label>
                                    <input type="text" class="form-control" id="apellido" name="apellido" required>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="nombreUsuario" class="form-label text-muted fw-bold">Usuario</label>
                                    <input type="text" class="form-control" id="nombreUsuario" name="nombreUsuario" placeholder="Ej. mferraris" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="contrasenia" class="form-label text-muted fw-bold">Contraseña</label>
                                    <input type="password" class="form-control" id="contrasenia" name="contrasenia">
                                    <div class="form-text" id="helpContrasenia" style="display:none; font-size: 0.75rem;">Dejar en blanco para no cambiar.</div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="mail" class="form-label text-muted fw-bold">Correo Principal</label>
                                <input type="email" class="form-control" id="mail" name="mail" placeholder="ejemplo@correo.com" required>
                            </div>

                            <div class="mb-3">
                                <label for="mailRecuperacion" class="form-label text-muted fw-bold">Correo de Recuperación</label>
                                <input type="email" class="form-control" id="mailRecuperacion" name="mailRecuperacion">
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="numeroTelefono" class="form-label text-muted fw-bold">Teléfono</label>
                                    <input type="tel" class="form-control" id="numeroTelefono" name="numeroTelefono">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="direccion" class="form-label text-muted fw-bold">Dirección</label>
                                    <input type="text" class="form-control" id="direccion" name="direccion">
                                </div>
                            </div>
                            
                            <!-- Botones con íconos -->
                            <button type="submit" class="btn btn-success w-100 mb-2 mt-2" id="btnGuardar">
                                <i class="bi bi-save me-2"></i>Guardar Usuario
                            </button>
                            <button type="button" class="btn btn-secondary w-100 d-none" id="btnCancelar" onclick="cancelarEdicion()">
                                <i class="bi bi-x-circle me-2"></i>Cancelar Edición
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- COLUMNA DERECHA: Tabla con el listado -->
            <div class="col-lg-8 col-md-12">
                <div class="card shadow-sm border-0">
                    <div class="card-header bg-white border-bottom">
                        <h5 class="mb-0 text-dark"><i class="bi bi-people-fill me-2"></i>Listado de Usuarios</h5>
                    </div>
                    <div class="card-body p-0 table-responsive">
                        <table class="table table-striped table-hover mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-3">#</th>
                                    <th>Usuario</th>
                                    <th>Nombre Completo</th>
                                    <th>Contacto</th>
                                    <th>Estado</th>
                                    <th class="text-center">Acciones</th>
                                </tr>
                            </thead>
                            <tbody class="align-middle">
                                <%
                                    List<Usuario> lista = (List<Usuario>) request.getAttribute("listaUsuarios");
                                    if (lista != null && !lista.isEmpty()) {
                                        for (Usuario u : lista) {
                                %>
                                            <tr>
                                                <td class="ps-3 fw-bold text-secondary"><%= u.getNumero() %></td>
                                                <td class="fw-semibold text-primary"><%= u.getNombreUsuario() %></td>
                                                <td><%= u.getApellido() %>, <%= u.getNombre() %></td>
                                                <td>
                                                    <div class="small"><i class="bi bi-envelope me-1"></i><%= u.getMail() %></div>
                                                    <div class="small text-muted"><i class="bi bi-telephone me-1"></i><%= u.getNumeroTelefono() != null ? u.getNumeroTelefono() : "-" %></div>
                                                </td>
                                                <td>
                                                    <% if (u.getFechaBaja() != null) { %>
                                                        <span class="badge bg-danger"><i class="bi bi-person-x-fill me-1"></i>Inactivo</span>
                                                    <% } else { %>
                                                        <span class="badge bg-success"><i class="bi bi-person-check-fill me-1"></i>Activo</span>
                                                    <% } %>
                                                </td>
                                                <td>
                                                    <div class="d-flex justify-content-center gap-2">
													    <!-- Botón Editar -->
													    <button type="button" class="btn btn-warning btn-sm text-dark" 
													            title="Editar Usuario"
													            onclick="cargarDatosEdicion('<%= u.getNumero() %>', '<%= u.getNombre() %>', '<%= u.getApellido() %>', '<%= u.getNombreUsuario() %>', '<%= u.getMail() %>', '<%= u.getMailRecuperacion() != null ? u.getMailRecuperacion() : "" %>', '<%= u.getNumeroTelefono() != null ? u.getNumeroTelefono() : "" %>', '<%= u.getDireccion() != null ? u.getDireccion() : "" %>')">
													        <i class="bi bi-pencil-square"></i>
													    </button>
													
													    <% if (u.getFechaBaja() != null) { %>
													        <!-- Botón Alta Lógica (Para usuarios inactivos) -->
													        <form action="<%= request.getContextPath() %>/usuarios" method="POST" class="m-0">
													            <input type="hidden" name="accion" value="altaLogica">
													            <input type="hidden" name="numero" value="<%= u.getNumero() %>">
													            <button type="submit" class="btn btn-outline-success btn-sm" 
													                    title="Reactivar Usuario"
													                    onclick="return confirm('¿Seguro que deseas volver a dar de alta a este usuario?');">
													                <i class="bi bi-check-circle"></i>
													            </button>
													        </form>
													    <% } else { %>
													        <!-- Botón Baja Lógica (Para usuarios activos) -->
													        <form action="<%= request.getContextPath() %>/usuarios" method="POST" class="m-0">
													            <input type="hidden" name="accion" value="bajaLogica">
													            <input type="hidden" name="numero" value="<%= u.getNumero() %>">
													            <button type="submit" class="btn btn-outline-danger btn-sm" 
													                    title="Dar de Baja"
													                    onclick="return confirm('¿Seguro que deseas dar de baja a este usuario?');">
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
                                            <td colspan="6" class="text-center text-muted py-4">
                                                <i class="bi bi-person-vcard fs-2 d-block mb-2"></i>
                                                No hay usuarios registrados en el sistema.
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
        function cargarDatosEdicion(numero, nombre, apellido, nombreUsuario, mail, mailRecup, telefono, direccion) {
            document.getElementById('numeroUsuario').value = numero;
            document.getElementById('accionForm').value = 'editar';
            
            document.getElementById('nombre').value = nombre;
            document.getElementById('apellido').value = apellido;
            document.getElementById('nombreUsuario').value = nombreUsuario;
            document.getElementById('mail').value = mail;
            document.getElementById('mailRecuperacion').value = mailRecup;
            document.getElementById('numeroTelefono').value = telefono;
            document.getElementById('direccion').value = direccion;
            
            // La contraseña no se carga por seguridad. Hacemos visible el texto de ayuda.
            document.getElementById('contrasenia').required = false;
            document.getElementById('helpContrasenia').style.display = 'block';
            
            // Actualizar Título
            document.getElementById('tituloFormulario').innerHTML = '<i class="bi bi-pencil-square me-2"></i>Editar Usuario';
            
            // Actualizar diseño del botón
            let btn = document.getElementById('btnGuardar');
            btn.innerHTML = '<i class="bi bi-arrow-clockwise me-2"></i>Actualizar Usuario';
            btn.className = 'btn btn-warning w-100 mb-2 mt-2 fw-bold text-dark';
            
            document.getElementById('btnCancelar').classList.remove('d-none');
        }
        
        function cancelarEdicion() {
            document.getElementById('accionForm').value = 'crear';
            document.getElementById('numeroUsuario').value = '';
            
            document.getElementById('nombre').value = '';
            document.getElementById('apellido').value = '';
            document.getElementById('nombreUsuario').value = '';
            document.getElementById('mail').value = '';
            document.getElementById('mailRecuperacion').value = '';
            document.getElementById('numeroTelefono').value = '';
            document.getElementById('direccion').value = '';
            document.getElementById('contrasenia').value = '';
            
            document.getElementById('contrasenia').required = true;
            document.getElementById('helpContrasenia').style.display = 'none';
            
            document.getElementById('tituloFormulario').innerHTML = '<i class="bi bi-person-plus-fill me-2"></i>Registrar Nuevo Usuario';
            
            let btn = document.getElementById('btnGuardar');
            btn.innerHTML = '<i class="bi bi-save me-2"></i>Guardar Usuario';
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