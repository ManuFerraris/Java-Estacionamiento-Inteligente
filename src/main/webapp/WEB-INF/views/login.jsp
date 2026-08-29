<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Municipalidad de Rosario - Ingreso</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</head>
<body class="bg-light d-flex align-items-center" style="height: 100vh;">

	<%@ include file="navbar.jsp" %>
	
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5 col-lg-4">
                <div class="card shadow border-0 rounded-4">
                    <div class="card-body p-5">
                        
                        <div class="text-center mb-4">
                            <i class="bi bi-buildings-fill text-primary" style="font-size: 3rem;"></i>
                            <h4 class="mt-2 text-secondary fw-bold">Estacionamiento Inteligente</h4>
                            <p class="text-muted small">Municipalidad de Rosario</p>
                        </div>

                        <% 
                            // Capturamos errores de login
                            String error = (String) request.getAttribute("error");
                            if (error != null) { 
                        %>
                            <div class="alert alert-danger py-2 text-center small" role="alert">
                                <i class="bi bi-exclamation-triangle-fill me-1"></i> <%= error %>
                            </div>
                        <% } %>

                        <% 
                            // Capturamos el éxito al venir del Sign Up
                            String exitoRegistro = (String) request.getAttribute("exitoRegistro");
                            if (exitoRegistro != null) { 
                        %>
                            <div class="alert alert-success py-2 text-center small" role="alert">
                                <i class="bi bi-check-circle-fill me-1"></i> <%= exitoRegistro %>
                            </div>
                        <% } %>

                        <form action="<%= request.getContextPath() %>/login" method="POST">
                            <div class="form-floating mb-3">
                                <input type="text" class="form-control" id="nombreUsuario" name="nombreUsuario" placeholder="Usuario" required autofocus>
                                <label for="nombreUsuario"><i class="bi bi-person me-2"></i>Usuario</label>
                            </div>
                            
                            <div class="form-floating mb-4">
                                <input type="password" class="form-control" id="contrasenia" name="contrasenia" placeholder="Contraseña" required>
                                <label for="contrasenia"><i class="bi bi-key me-2"></i>Contraseña</label>
                            </div>
                            
                            <button type="submit" class="btn btn-primary w-100 py-2 fw-bold text-uppercase">
                                Ingresar
                            </button>
                            
                            <!-- Enlace al Registro -->
                            <div class="text-center mt-4">
                                <span class="text-muted small">¿No tienes una cuenta?</span>
                                <a href="<%= request.getContextPath() %>/registro" class="text-decoration-none fw-bold ms-1">Regístrate aquí</a>
                            </div>
                        </form>
                        
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>