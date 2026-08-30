<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="estacionamiento.domain.Usuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mi Perfil - MR Estacionamiento</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-light">

    <%@ include file="navbar.jsp" %>

    <div class="container px-4 mb-5">
        
        <div class="d-flex justify-content-between align-items-center mb-4 mt-3">
            <h2 class="text-secondary fw-bold"><i class="bi bi-person-vcard me-2 text-secondary"></i>Mi Perfil</h2>
        </div>

        <% 
            String error = (String) session.getAttribute("error");
            String exito = (String) session.getAttribute("exito");
            Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        %>

        <form action="<%= request.getContextPath() %>/perfil-user" method="POST" id="formPerfil">
            <div class="row g-4">
                
                <!-- COLUMNA IZQUIERDA: Datos Personales -->
                <div class="col-lg-8">
                    <div class="card border-0 shadow-sm rounded-4 h-100">
                        <div class="card-header bg-white border-bottom py-3">
                            <h5 class="mb-0 fw-bold text-dark"><i class="bi bi-person-lines-fill me-2 text-primary"></i>Información Personal</h5>
                        </div>
                        <div class="card-body p-4">
                            
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label text-muted small fw-bold">Nombre</label>
                                    <input type="text" class="form-control" name="nombre" value="<%= u.getNombre() %>" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label text-muted small fw-bold">Apellido</label>
                                    <input type="text" class="form-control" name="apellido" value="<%= u.getApellido() %>" required>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label text-muted small fw-bold">Correo Electrónico</label>
                                    <input type="email" class="form-control" name="mail" value="<%= u.getMail() %>" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label text-muted small fw-bold">Teléfono</label>
                                    <input type="tel" class="form-control" name="numeroTelefono" value="<%= u.getNumeroTelefono() %>" required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label text-muted small fw-bold">Dirección</label>
                                <input type="text" class="form-control" name="direccion" value="<%= u.getDireccion() %>" required>
                            </div>

                        </div>
                    </div>
                </div>

                <!-- COLUMNA DERECHA: Seguridad y Cuenta -->
                <div class="col-lg-4">
                    <div class="card border-0 shadow-sm rounded-4 mb-4">
                        <div class="card-header bg-white border-bottom py-3">
                            <h5 class="mb-0 fw-bold text-dark"><i class="bi bi-shield-lock me-2 text-warning"></i>Seguridad</h5>
                        </div>
                        <div class="card-body p-4">
                            
                            <div class="mb-3">
                                <label class="form-label text-muted small fw-bold">Nombre de Usuario</label>
                                <input type="text" class="form-control bg-light" value="<%= u.getNombreUsuario() %>" readonly disabled>
                                <div class="form-text">El nombre de usuario no puede ser modificado.</div>
                            </div>

                            <hr class="my-4">

                            <h6 class="fw-bold mb-3">Cambiar Contraseña</h6>
                            <p class="small text-muted mb-3">Deja estos campos en blanco si deseas conservar tu contraseña actual.</p>

                            <div class="mb-3">
                                <label class="form-label text-muted small fw-bold">Nueva Contraseña</label>
                                <input type="password" class="form-control" id="contrasenia" name="contrasenia" minlength="6">
                            </div>

                            <div class="mb-4">
                                <label class="form-label text-muted small fw-bold">Confirmar Contraseña</label>
                                <input type="password" class="form-control" id="confirmarContrasenia" name="confirmarContrasenia" minlength="6">
                            </div>

                            <button type="submit" class="btn btn-success w-100 py-2 fw-bold">
                                <i class="bi bi-save me-2"></i>Guardar Cambios
                            </button>

                        </div>
                    </div>
                </div>

            </div>
        </form>
    </div>

    <!-- Script de validación Frontend y Alertas Flash -->
    <script>
        document.getElementById('formPerfil').addEventListener('submit', function(event) {
            let pass = document.getElementById('contrasenia').value;
            let conf = document.getElementById('confirmarContrasenia').value;

            if (pass !== "" && pass !== conf) {
                event.preventDefault();
                Swal.fire({ icon: 'warning', title: 'Verifica tu contraseña', text: 'Las nuevas contraseñas no coinciden.' });
            }
        });

        <% if (error != null) { %>
            Swal.fire({ icon: 'error', title: 'Oops...', text: '<%= error %>' });
            <% session.removeAttribute("error"); %>
        <% } %>
        
        <% if (exito != null) { %>
            Swal.fire({ icon: 'success', title: '¡Actualizado!', text: '<%= exito %>' });
            <% session.removeAttribute("exito"); %>
        <% } %>
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>