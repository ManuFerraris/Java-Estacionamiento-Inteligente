<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registro - Municipalidad de Rosario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-light d-flex align-items-center py-5">

	<%@ include file="navbar.jsp" %>
	
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-6">
                <div class="card shadow border-0 rounded-4">
                    <div class="card-body p-5">
                        
                        <div class="text-center mb-4">
                            <i class="bi bi-person-badge-fill text-primary" style="font-size: 3rem;"></i>
                            <h4 class="mt-2 text-secondary fw-bold">Crear Cuenta</h4>
                            <p class="text-muted small">Portal del Ciudadano - Estacionamiento Inteligente</p>
                        </div>

                        <% 
                            String error = (String) request.getAttribute("error");
                            if (error != null) { 
                        %>
                            <div class="alert alert-danger py-2 text-center small" role="alert">
                                <i class="bi bi-exclamation-triangle-fill me-1"></i> <%= error %>
                            </div>
                        <% } %>

                        <form action="<%= request.getContextPath() %>/registro" method="POST" id="formRegistro">
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="nombre" class="form-label text-muted fw-bold small">Nombre *</label>
                                    <input type="text" class="form-control" id="nombre" name="nombre" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="apellido" class="form-label text-muted fw-bold small">Apellido *</label>
                                    <input type="text" class="form-control" id="apellido" name="apellido" required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="nombreUsuario" class="form-label text-muted fw-bold small">Nombre de Usuario *</label>
                                <input type="text" class="form-control" id="nombreUsuario" name="nombreUsuario" required>
                            </div>

                            <div class="mb-3">
                                <label for="mail" class="form-label text-muted fw-bold small">Correo Electrónico *</label>
                                <input type="email" class="form-control" id="mail" name="mail" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="mailRecuperacion" class="form-label text-muted fw-bold small">Correo de Recuperacion *</label>
                                <input type="email" class="form-control" id="mailRecuperacion" name="mailRecuperacion" required>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="numeroTelefono" class="form-label text-muted fw-bold small">Teléfono *</label>
                                    <input type="tel" class="form-control" id="numeroTelefono" name="numeroTelefono" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="direccion" class="form-label text-muted fw-bold small">Dirección *</label>
                                    <input type="text" class="form-control" id="direccion" name="direccion" required>
                                </div>
                            </div>

                            <div class="row mt-2">
                                <div class="col-md-6 mb-4">
                                    <label for="contrasenia" class="form-label text-muted fw-bold small">Contraseña *</label>
                                    <input type="password" class="form-control" id="contrasenia" name="contrasenia" required minlength="6">
                                </div>
                                <div class="col-md-6 mb-4">
                                    <label for="confirmarContrasenia" class="form-label text-muted fw-bold small">Confirmar Contraseña *</label>
                                    <input type="password" class="form-control" id="confirmarContrasenia" name="confirmarContrasenia" required minlength="6">
                                </div>
                            </div>
                            
                            <button type="submit" class="btn btn-success w-100 py-2 fw-bold text-uppercase mb-3">
                                <i class="bi bi-person-plus me-2"></i>Registrarme
                            </button>

                            <div class="text-center mt-3">
                                <span class="text-muted small">¿Ya tienes una cuenta?</span>
                                <a href="<%= request.getContextPath() %>/login" class="text-decoration-none fw-bold ms-1">Iniciar Sesión</a>
                            </div>
                        </form>
                        
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        document.getElementById('formRegistro').addEventListener('submit', function(event) {
            let pass = document.getElementById('contrasenia').value;
            let conf = document.getElementById('confirmarContrasenia').value;

            if (pass !== conf) {
                event.preventDefault();
                Swal.fire({
                    icon: 'warning',
                    title: 'Verifica tu contraseña',
                    text: 'Las contraseñas ingresadas no coinciden.',
                    confirmButtonColor: '#ffc107'
                });
            }
        });
    </script>
</body>
</html>