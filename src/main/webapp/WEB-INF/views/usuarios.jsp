<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="estacionamiento.domain.Usuario" %>
<%@ page import="estacionamiento.domain.RolesUsuario" %>
<%@ page import="estacionamiento.domain.Cochera" %>
<%@ page import="estacionamiento.domain.AsignacionCochera" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Usuarios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-light">

	<%@ include file="navbar.jsp" %>
	
    <% 
        List<Usuario> lista = (List<Usuario>) request.getAttribute("listaUsuarios");
        List<Cochera> listaCocheras = (List<Cochera>) request.getAttribute("listaCocheras");
        List<AsignacionCochera> asignacionesActivas = (List<AsignacionCochera>) request.getAttribute("asignacionesActivas");
    %>

    <div class="container-fluid mt-5 px-4 mb-5">
        <h1 class="mb-4 text-secondary">Administración de Usuarios y Personal</h1>

        <div class="row">
            <!-- COLUMNA IZQUIERDA: Formulario de Alta/Edición + Gestión de Encargados -->
            <div class="col-lg-4 col-md-12">
                
                <!-- 1. TARJETA ALTA/EDICIÓN DE USUARIO -->
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
                                    <label for="rol" class="form-label text-muted fw-bold">Rol</label>
                                    <select class="form-select" id="rol" name="rol" required onchange="verificarRolEncargado()">
                                        <option value="" disabled selected>Seleccione un rol...</option>
                                        <% for (RolesUsuario r : RolesUsuario.values()) { %>
                                            <option value="<%= r.name() %>"><%= r.name() %></option>
                                        <% } %>
                                    </select>
                                </div>
                            </div>

                            <!-- Desplegable dinámico: Solo visible si eligen rol ENCARGADO al crear -->
                            <div class="mb-3 d-none p-3 bg-info bg-opacity-10 border border-info rounded-3" id="divCocheraInicial">
                                <label for="codigoCocheraInicial" class="form-label text-dark fw-bold mb-1">
                                    <i class="bi bi-building-check me-1 text-primary"></i>Asignar Cochera de Trabajo (Opcional)
                                </label>
                                <select class="form-select" id="codigoCocheraInicial" name="codigoCocheraInicial">
                                    <option value="">-- Asignar más tarde --</option>
                                    <% if (listaCocheras != null) {
                                        for (Cochera c : listaCocheras) { %>
                                            <option value="<%= c.getCodigo() %>"><%= c.getNombre() %> (<%= c.getDireccion() %>)</option>
                                    <%  }
                                       } %>
                                </select>
                                <div class="form-text small">Se registrará con fecha de inicio de hoy.</div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="contrasenia" class="form-label text-muted fw-bold">Contraseña</label>
                                    <input type="password" class="form-control" id="contrasenia" name="contrasenia" required>
                                    <div class="form-text" id="helpContrasenia" style="display:none; font-size: 0.75rem;">Dejar en blanco para no cambiar.</div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="confirmarContrasenia" class="form-label text-muted fw-bold">Confirmar contraseña</label>
                                    <input type="password" class="form-control" id="confirmarContrasenia" name="confirmarContrasenia" required>
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
                                    <input type="tel" class="form-control" id="numeroTelefono" name="numeroTelefono" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="direccion" class="form-label text-muted fw-bold">Dirección</label>
                                    <input type="text" class="form-control" id="direccion" name="direccion" required>
                                </div>
                            </div>
                            
                            <button type="submit" class="btn btn-success w-100 mb-2 mt-2" id="btnGuardar">
                                <i class="bi bi-save me-2"></i>Guardar Usuario
                            </button>
                            <button type="button" class="btn btn-secondary w-100 d-none" id="btnCancelar" onclick="cancelarEdicion()">
                                <i class="bi bi-x-circle me-2"></i>Cancelar Edición
                            </button>
                        </form>
                    </div>
                </div>

                <!-- 2. NUEVA TARJETA: ASIGNACIÓN DE COCHERAS A ENCARGADOS -->
                <div class="card shadow-sm border-0">
                    <div class="card-header bg-dark text-white">
                        <h5 class="mb-0"><i class="bi bi-building-gear me-2 text-warning"></i>Asignar Encargado a Cochera</h5>
                    </div>
                    <div class="card-body">
                        <form action="<%= request.getContextPath() %>/usuarios" method="POST">
                            <input type="hidden" name="accion" value="asignarCochera">
                            <div class="mb-3">
                                <label class="form-label text-muted small fw-bold">Seleccionar Encargado Activo</label>
                                <select class="form-select" name="numeroUsuario" required>
                                    <option value="" disabled selected>Elija un encargado...</option>
                                    <% if (lista != null) {
                                        for (Usuario u : lista) {
                                            if (u.getRol() == RolesUsuario.ENCARGADO && u.getFechaBaja() == null) { %>
                                                <option value="<%= u.getNumero() %>"><%= u.getApellido() %>, <%= u.getNombre() %> (<%= u.getNombreUsuario() %>)</option>
                                    <%      }
                                        }
                                       } %>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label text-muted small fw-bold">Cochera Municipal Destino</label>
                                <select class="form-select" name="codigoCochera" required>
                                    <option value="" disabled selected>Elija una cochera...</option>
                                    <% if (listaCocheras != null) {
                                        for (Cochera c : listaCocheras) { %>
                                            <option value="<%= c.getCodigo() %>"><%= c.getNombre() %> - <%= c.getDireccion() %></option>
                                    <%  }
                                       } %>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-warning w-100 fw-bold text-dark">
                                <i class="bi bi-link-45deg me-1"></i>Vincular a Cochera
                            </button>
                        </form>
                    </div>
                </div>

            </div>

            <!-- COLUMNA DERECHA: Tabla de Usuarios + Tabla de Asignaciones Activas -->
            <div class="col-lg-8 col-md-12">
                
                <!-- Tabla 1: Listado de Usuarios -->
                <div class="card shadow-sm border-0 mb-4">
                    <div class="card-header bg-white border-bottom">
                        <h5 class="mb-0 text-dark"><i class="bi bi-people-fill me-2"></i>Listado de Usuarios</h5>
                    </div>
                    <div class="card-body p-0 table-responsive">
                        <table class="table table-striped table-hover mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-3">#</th>
                                    <th>Usuario / Rol</th>
                                    <th>Nombre Completo</th>
                                    <th>Contacto</th>
                                    <th>Estado</th>
                                    <th class="text-center">Acciones</th>
                                </tr>
                            </thead>
                            <tbody class="align-middle">
                                <%
                                    if (lista != null && !lista.isEmpty()) {
                                        for (Usuario u : lista) {
                                            String badgeRol = "bg-info text-dark";
                                            if (u.getRol() == RolesUsuario.ADMIN) badgeRol = "bg-dark text-white";
                                            else if (u.getRol() == RolesUsuario.TRABAJADOR) badgeRol = "bg-primary text-white";
                                            else if (u.getRol() == RolesUsuario.ENCARGADO) badgeRol = "bg-warning text-dark";
                                %>
                                            <tr>
                                                <td class="ps-3 fw-bold text-secondary"><%= u.getNumero() %></td>
                                                <td>
                                                    <span class="fw-semibold text-primary d-block"><%= u.getNombreUsuario() %></span>
                                                    <span class="badge <%= badgeRol %> mt-1"><%= u.getRol() != null ? u.getRol().name() : "CLIENTE" %></span>
                                                </td>
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
                                                        <button type="button" class="btn btn-warning btn-sm text-dark" 
                                                                title="Editar Usuario"
                                                                onclick="cargarDatosEdicion('<%= u.getNumero() %>', '<%= u.getNombre() %>', '<%= u.getApellido() %>', '<%= u.getNombreUsuario() %>', '<%= u.getRol() != null ? u.getRol().name() : "" %>', '<%= u.getMail() %>', '<%= u.getMailRecuperacion() != null ? u.getMailRecuperacion() : "" %>', '<%= u.getNumeroTelefono() != null ? u.getNumeroTelefono() : "" %>', '<%= u.getDireccion() != null ? u.getDireccion() : "" %>')">
                                                            <i class="bi bi-pencil-square"></i>
                                                        </button>
                                                    
                                                        <% if (u.getFechaBaja() != null) { %>
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

                <!-- Tabla 2: Encargados en Cocheras (Asignaciones Activas N a M) -->
                <div class="card shadow-sm border-0">
                    <div class="card-header bg-white border-bottom d-flex justify-content-between align-items-center">
                        <h5 class="mb-0 text-dark"><i class="bi bi-pin-map-fill me-2 text-warning"></i>Plantel de Encargados por Cochera (Activos)</h5>
                    </div>
                    <div class="card-body p-0 table-responsive">
                        <table class="table table-hover mb-0 align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-3">Cochera Municipal</th>
                                    <th>Encargado Asignado</th>
                                    <th>Desde Fecha</th>
                                    <th class="text-center">Acción</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (asignacionesActivas != null && !asignacionesActivas.isEmpty()) {
                                    for (AsignacionCochera asig : asignacionesActivas) { %>
                                        <tr>
                                            <td class="ps-3">
                                                <div class="fw-bold text-dark"><%= asig.getCochera().getNombre() %></div>
                                                <div class="small text-muted"><%= asig.getCochera().getDireccion() %></div>
                                            </td>
                                            <td>
                                                <span class="fw-semibold"><%= asig.getUsuario().getApellido() %>, <%= asig.getUsuario().getNombre() %></span>
                                                <span class="badge bg-light text-secondary border ms-1"><%= asig.getUsuario().getNombreUsuario() %></span>
                                            </td>
                                            <td>
                                                <span class="badge bg-success bg-opacity-10 text-success border border-success">
                                                    <i class="bi bi-calendar-check me-1"></i><%= asig.getId().getFechaDesde() %>
                                                </span>
                                            </td>
                                            <td class="text-center">
                                                <form action="<%= request.getContextPath() %>/usuarios" method="POST" class="m-0">
                                                    <input type="hidden" name="accion" value="finalizarAsignacion">
                                                    <input type="hidden" name="numeroUsuario" value="<%= asig.getId().getNumeroUsuario() %>">
                                                    <input type="hidden" name="codigoCochera" value="<%= asig.getId().getCodigoCochera() %>">
                                                    <button type="submit" class="btn btn-outline-danger btn-sm" 
                                                            title="Finalizar asignación en esta cochera"
                                                            onclick="return confirm('¿Finalizar la asignación de este encargado en la cochera?');">
                                                        <i class="bi bi-box-arrow-right me-1"></i>Desvincular
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                <%  }
                                   } else { %>
                                    <tr>
                                        <td colspan="4" class="text-center text-muted py-4">
                                            No hay encargados asignados a cocheras actualmente.
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

    <% 
        String error = (String) session.getAttribute("error");
        String exito = (String) session.getAttribute("exito");
        if (error != null) session.removeAttribute("error");
        if (exito != null) session.removeAttribute("exito");
    %>
    
    <script>
        function verificarRolEncargado() {
            const rolSelect = document.getElementById('rol');
            const accionForm = document.getElementById('accionForm').value;
            const divCochera = document.getElementById('divCocheraInicial');
            
            // Mostramos el selector de cochera inicial si están creando un ENCARGADO
            if (accionForm === 'crear' && rolSelect.value === 'ENCARGADO') {
                divCochera.classList.remove('d-none');
            } else {
                divCochera.classList.add('d-none');
                document.getElementById('codigoCocheraInicial').value = '';
            }
        }

        document.getElementById('formUsuario').addEventListener('submit', function(event) {
            let pass = document.getElementById('contrasenia').value;
            let conf = document.getElementById('confirmarContrasenia').value;
            let accion = document.getElementById('accionForm').value;

            if (accion === 'crear' || pass.trim() !== '') {
                if (pass !== conf) {
                    event.preventDefault();
                    Swal.fire({
                        icon: 'warning',
                        title: 'Contraseñas no coinciden',
                        text: 'Por favor, verifica que la contraseña y su confirmación sean idénticas.',
                        confirmButtonColor: '#ffc107'
                    });
                }
            }
        });

        function cargarDatosEdicion(numero, nombre, apellido, nombreUsuario, rol, mail, mailRecup, telefono, direccion) {
            document.getElementById('numeroUsuario').value = numero;
            document.getElementById('accionForm').value = 'editar';
            
            document.getElementById('nombre').value = nombre;
            document.getElementById('apellido').value = apellido;
            document.getElementById('nombreUsuario').value = nombreUsuario;
            document.getElementById('rol').value = rol;
            document.getElementById('mail').value = mail;
            document.getElementById('mailRecuperacion').value = mailRecup;
            document.getElementById('numeroTelefono').value = telefono;
            document.getElementById('direccion').value = direccion;
            
            verificarRolEncargado();
            
            document.getElementById('contrasenia').required = false;
            document.getElementById('confirmarContrasenia').required = false;
            document.getElementById('helpContrasenia').style.display = 'block';
            
            document.getElementById('tituloFormulario').innerHTML = '<i class="bi bi-pencil-square me-2"></i>Editar Usuario';
            
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
            document.getElementById('rol').value = '';
            document.getElementById('mail').value = '';
            document.getElementById('mailRecuperacion').value = '';
            document.getElementById('numeroTelefono').value = '';
            document.getElementById('direccion').value = '';
            document.getElementById('contrasenia').value = '';
            document.getElementById('confirmarContrasenia').value = '';
            
            verificarRolEncargado();
            
            document.getElementById('contrasenia').required = true;
            document.getElementById('confirmarContrasenia').required = true;
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