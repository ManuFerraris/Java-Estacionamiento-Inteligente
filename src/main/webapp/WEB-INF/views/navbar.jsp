<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="estacionamiento.domain.Usuario" %>
<%@ page import="estacionamiento.domain.RolesUsuario" %>

<%
    // Rescatamos al usuario de la sesión
    Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogueado");
    
    // Obtenemos la URL original solicitada por el navegador (antes del forward al JSP)
    String uriActual = (String) request.getAttribute("jakarta.servlet.forward.request_uri");
    
    // Fallback por si en algún momento se accede directo sin forward
    if (uriActual == null) {
        uriActual = request.getRequestURI();
    }
    
    // Solo mostramos el navbar si hay alguien logueado
    if (usuarioSesion != null) {
%>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm mb-4">
    <div class="container-fluid px-4">
        
        <!-- Logo y Nombre Inteligente -->
        <% 
            String linkInicio = (usuarioSesion.getRol() == RolesUsuario.ADMIN || usuarioSesion.getRol() == RolesUsuario.TRABAJADOR) 
                                ? request.getContextPath() + "/dashboard-oficina" 
                                : request.getContextPath() + "/inicio-user";
        %>
        <a class="navbar-brand fw-bold" href="<%= linkInicio %>">
            <i class="bi bi-buildings-fill me-2 text-primary"></i>MR - Estacionamiento
        </a>
        
        <!-- Botón hamburguesa para celulares -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menuPrincipal">
            <span class="navbar-toggler-icon"></span>
        </button>
        
        <!-- Enlaces del Menú -->
        <div class="collapse navbar-collapse" id="menuPrincipal">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                
                <%-- ENLACES PARA EMPLEADOS MUNICIPALES --%>
                <% if (usuarioSesion.getRol() == RolesUsuario.ADMIN || usuarioSesion.getRol() == RolesUsuario.TRABAJADOR) { %>
                    <li class="nav-item">
                        <a class="nav-link <%= uriActual.endsWith("/usuarios") ? "active text-white fw-bold" : "" %>" 
                           href="<%= request.getContextPath() %>/usuarios">
                            <i class="bi bi-people me-1"></i>Usuarios
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= uriActual.endsWith("/suscripciones-oficina") ? "active text-white fw-bold" : "" %>" 
                           href="<%= request.getContextPath() %>/suscripciones-oficina">
                            <i class="bi bi-card-checklist me-1"></i>Suscripciones
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= uriActual.endsWith("/pagos-suscripciones-oficina") ? "active text-white fw-bold" : "" %>" 
                           href="<%= request.getContextPath() %>/pagos-suscripciones-oficina">
                            <i class="bi bi-cash-coin me-1"></i>Caja / Pagos
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= uriActual.endsWith("/cocheras-oficina") ? "active text-white fw-bold" : "" %>" 
                           href="<%= request.getContextPath() %>/cocheras-oficina">
                            <i class="bi bi-building me-1"></i>Cocheras
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= uriActual.endsWith("/preciosHistoricos-oficina") ? "active text-white fw-bold" : "" %>" 
                           href="<%= request.getContextPath() %>/preciosHistoricos-oficina">
                            <i class="bi bi-cash-stack me-1"></i>Precios Beneficios
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= uriActual.endsWith("/tipoPlanes-oficina") ? "active text-white fw-bold" : "" %>" 
                           href="<%= request.getContextPath() %>/tipoPlanes-oficina">
                            <i class="bi bi-file-earmark-check-fill me-1"></i>Gestion de Planes
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= uriActual.endsWith("/beneficios-oficina") ? "active text-white fw-bold" : "" %>" 
                           href="<%= request.getContextPath() %>/beneficios-oficina">
                            <i class="bi bi-emoji-grin me-1"></i>Gestion Beneficios
                        </a>
                    </li>
                <% } %>
                
                <%-- ENLACES PARA EL CIUDADANO (CLIENTE) --%>
                <% if (usuarioSesion.getRol() == RolesUsuario.CLIENTE) { %>
                    <li class="nav-item">
                        <a class="nav-link <%= uriActual.endsWith("/inicio-user") ? "active text-white fw-bold" : "" %>" 
                           href="<%= request.getContextPath() %>/inicio-user">
                            <i class="bi bi-house me-1"></i>Mi Portal
                        </a>
                    </li>
                <% } %>
                
            </ul>
            
            <!-- Datos del Usuario y Botón de Salir -->
            <div class="d-flex align-items-center">
                <span class="text-light me-3 small">
                    <i class="bi bi-person-circle me-1"></i>
                    <%= usuarioSesion.getNombreUsuario() %> 
                    <span class="badge bg-secondary ms-1"><%= usuarioSesion.getRol().name() %></span>
                </span>
                <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline-danger btn-sm">
                    <i class="bi bi-box-arrow-right me-1"></i>Salir
                </a>
            </div>
        </div>
        
    </div>
</nav>
<% } %>