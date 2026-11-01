<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="estacionamiento.domain.Usuario" %>
<%@ page import="estacionamiento.domain.RolesUsuario" %>

<%
    Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogueado");
    String uriActual = (String) request.getAttribute("jakarta.servlet.forward.request_uri");
    if (uriActual == null) {
        uriActual = request.getRequestURI();
    }
    
    if (usuarioSesion != null) {
%>
<!-- 1. BARRA SUPERIOR (TOPBAR) -->
<nav class="navbar navbar-dark bg-dark shadow-sm sticky-top">
    <div class="container-fluid px-4">
        
        <div class="d-flex align-items-center">
            <!-- Botón Hamburguesa que dispara el Offcanvas -->
            <button class="navbar-toggler border-0 shadow-none me-3" type="button" data-bs-toggle="offcanvas" data-bs-target="#menuLateralOficina" aria-controls="menuLateralOficina">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <!-- Logo y Marca -->
            <% 
                String linkInicio = (usuarioSesion.getRol() == RolesUsuario.ADMIN || usuarioSesion.getRol() == RolesUsuario.TRABAJADOR) 
                                    ? request.getContextPath() + "/dashboard-oficina" 
                                    : request.getContextPath() + "/inicio-user";
            %>
            <a class="navbar-brand fw-bold mb-0" href="<%= linkInicio %>">
                <i class="bi bi-buildings-fill me-2 text-primary"></i>MR - Estacionamiento
            </a>
        </div>
        
        <!-- Datos del Usuario y Salir (Siempre visibles a la derecha) -->
        <div class="d-flex align-items-center">
            <span class="text-light me-3 small d-none d-md-inline">
                <i class="bi bi-person-circle me-1"></i>
                <%= usuarioSesion.getNombreUsuario() %> 
                <span class="badge bg-secondary ms-1"><%= usuarioSesion.getRol().name() %></span>
            </span>
            <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline-danger btn-sm">
                <i class="bi bi-box-arrow-right me-1"></i><span class="d-none d-md-inline">Salir</span>
            </a>
        </div>
        
    </div>
</nav>

<!-- 2. MENÚ LATERAL DESLIZANTE (OFFCANVAS) -->
<div class="offcanvas offcanvas-start bg-dark text-white" tabindex="-1" id="menuLateralOficina" aria-labelledby="menuLateralLabel">
    
    <div class="offcanvas-header border-bottom border-secondary pb-3 mt-2 px-4">
        <h5 class="offcanvas-title fw-bold" id="menuLateralLabel">
            <i class="bi bi-grid-1x2-fill me-2 text-primary"></i>Menú de Gestión
        </h5>
        <button type="button" class="btn-close btn-close-white shadow-none" data-bs-dismiss="offcanvas" aria-label="Cerrar"></button>
    </div>
    
    <div class="offcanvas-body px-0">
        <!-- flex-column hace que los enlaces se apilen verticalmente -->
        <ul class="navbar-nav flex-column w-100 mb-2">
            
            <%-- ENLACES PARA EMPLEADOS MUNICIPALES --%>
            <% if (usuarioSesion.getRol() == RolesUsuario.ADMIN || usuarioSesion.getRol() == RolesUsuario.TRABAJADOR) { %>
                
                <li class="nav-item px-3 mb-1">
                    <a class="nav-link px-3 py-2 rounded-3 <%= uriActual.endsWith("/usuarios") ? "active bg-primary text-white fw-bold" : "text-light" %>" 
                       href="<%= request.getContextPath() %>/usuarios">
                        <i class="bi bi-people me-2"></i>Usuarios
                    </a>
                </li>
                <li class="nav-item px-3 mb-1">
                    <a class="nav-link px-3 py-2 rounded-3 <%= uriActual.endsWith("/suscripciones-oficina") ? "active bg-primary text-white fw-bold" : "text-light" %>" 
                       href="<%= request.getContextPath() %>/suscripciones-oficina">
                        <i class="bi bi-card-checklist me-2"></i>Suscripciones
                    </a>
                </li>
                <li class="nav-item px-3 mb-1">
                    <a class="nav-link px-3 py-2 rounded-3 <%= uriActual.endsWith("/pagos-suscripciones-oficina") ? "active bg-primary text-white fw-bold" : "text-light" %>" 
                       href="<%= request.getContextPath() %>/pagos-suscripciones-oficina">
                        <i class="bi bi-cash-coin me-2"></i>Caja / Pagos
                    </a>
                </li>
                <li class="nav-item px-3 mb-1">
                    <a class="nav-link px-3 py-2 rounded-3 <%= uriActual.endsWith("/LugarServlet") ? "active bg-primary text-white fw-bold" : "text-light" %>" 
                       href="<%= request.getContextPath() %>/LugarServlet">
                        <i class="bi bi-grid-3x3 me-2"></i>Lugares
                    </a>
                </li>
                <li class="nav-item px-3 mb-1">
                    <a class="nav-link px-3 py-2 rounded-3 <%= uriActual.endsWith("/tipoEstadia") ? "active bg-primary text-white fw-bold" : "text-light" %>" 
                       href="<%= request.getContextPath() %>/tipoEstadia">
                        <i class="bi bi-clock-history me-2"></i>Tipo Estadía
                    </a>
                </li>                   
                <li class="nav-item px-3 mb-1">
                    <a class="nav-link px-3 py-2 rounded-3 <%= uriActual.endsWith("/cocheras-oficina") ? "active bg-primary text-white fw-bold" : "text-light" %>" 
                       href="<%= request.getContextPath() %>/cocheras-oficina">
                        <i class="bi bi-building me-2"></i>Cocheras
                    </a>
                </li>
                <li class="nav-item px-3 mb-1">
                    <a class="nav-link px-3 py-2 rounded-3 <%= uriActual.endsWith("/preciosHistoricos-oficina") ? "active bg-primary text-white fw-bold" : "text-light" %>" 
                       href="<%= request.getContextPath() %>/preciosHistoricos-oficina">
                        <i class="bi bi-cash-stack me-2"></i>Precios Beneficios
                    </a>
                </li>
                <li class="nav-item px-3 mb-1">
                    <a class="nav-link px-3 py-2 rounded-3 <%= uriActual.endsWith("/tipoPlanes-oficina") ? "active bg-primary text-white fw-bold" : "text-light" %>" 
                       href="<%= request.getContextPath() %>/tipoPlanes-oficina">
                        <i class="bi bi-file-earmark-check-fill me-2"></i>Gestión de Planes
                    </a>
                </li>
                <li class="nav-item px-3 mb-1">
                    <a class="nav-link px-3 py-2 rounded-3 <%= uriActual.endsWith("/tiposVehiculo-oficina") ? "active bg-primary text-white fw-bold" : "text-light" %>" 
                       href="<%= request.getContextPath() %>/tiposVehiculo-oficina">
                        <i class="bi bi-car-front-fill me-2"></i>Tipos de Vehículo
                    </a>
                </li>
                <li class="nav-item px-3 mb-4">
                    <a class="nav-link px-3 py-2 rounded-3 <%= uriActual.endsWith("/beneficios-oficina") ? "active bg-primary text-white fw-bold" : "text-light" %>" 
                       href="<%= request.getContextPath() %>/beneficios-oficina">
                        <i class="bi bi-emoji-grin me-2"></i>Gestión Beneficios
                    </a>
                </li>
                
                <!-- Botón de Barrera Destacado en el lateral -->
                <li class="nav-item px-4 mt-2 border-top border-secondary pt-4">
                    <a href="<%= request.getContextPath() %>/terminal-barrera" class="btn btn-primary w-100 text-start fw-bold shadow-sm rounded-3">
                        <i class="bi bi-upc-scan me-2"></i> Terminal Barrera
                    </a>
                </li>
            <% } %>
            
            <%-- ENLACES EXCLUSIVOS PARA ADMINISTRADOR --%>
            <% if (usuarioSesion.getRol() == RolesUsuario.ADMIN) { %>
                <li class="nav-item px-3 mt-3">
                    <h6 class="text-secondary text-uppercase small fw-bold px-3 mb-2">Administración</h6>
                </li>
                <li class="nav-item px-3 mb-1">
                    <a class="nav-link px-3 py-2 rounded-3 text-warning <%= uriActual.endsWith("/reportes-oficina") ? "active bg-warning text-dark fw-bold" : "" %>" 
                       href="<%= request.getContextPath() %>/reportes-oficina">
                        <i class="bi bi-bar-chart-line-fill me-2"></i>Centro de Reportes
                    </a>
                </li>
            <% } %>
            
            <%-- ENLACES PARA EL CIUDADANO (CLIENTE) --%>
            <% if (usuarioSesion.getRol() == RolesUsuario.CLIENTE) { %>
                <li class="nav-item px-3 mb-1">
                    <a class="nav-link px-3 py-2 rounded-3 <%= uriActual.endsWith("/inicio-user") ? "active bg-primary text-white fw-bold" : "text-light" %>" 
                       href="<%= request.getContextPath() %>/inicio-user">
                        <i class="bi bi-house me-2"></i>Mi Portal
                    </a>
                </li>
            <% } %>
            
        </ul>
    </div>
</div>
<% } %>