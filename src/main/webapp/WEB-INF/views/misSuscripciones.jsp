<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="estacionamiento.domain.Usuario" %>
<%@ page import="estacionamiento.domain.Suscripcion" %>
<%@ page import="estacionamiento.domain.PagoSuscripcion" %>
<%@ page import="estacionamiento.domain.TipoPlan" %>
<%@ page import="estacionamiento.domain.TipoPago" %>
<%@ page import="estacionamiento.domain.Beneficio" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Suscripciones - MR Estacionamiento</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        .hover-card {
            transition: all 0.3s ease-in-out;
            cursor: pointer;
        }
        .hover-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.15) !important;
            border-color: #0d6efd !important;
        }
    </style>
</head>
<body class="bg-light">

    <%@ include file="navbar.jsp" %>

    <div class="container px-4 mb-5">
        
        <div class="d-flex justify-content-between align-items-center mb-4 mt-3">
            <h2 class="text-secondary fw-bold"><i class="bi bi-card-checklist me-2 text-success"></i>Mis Suscripciones</h2>
        </div>

        <% 
            String error = (String) session.getAttribute("error");
            String exito = (String) session.getAttribute("exito");
        %>

        <div class="row g-4">
            <!-- COLUMNA IZQUIERDA: Estado Actual y Pagos Pendientes -->
            <div class="col-lg-4">
                
                <!-- Tarjeta de Plan Actual -->
                <div class="card border-0 shadow-sm rounded-4 mb-4">
                    <div class="card-header bg-success text-white py-3 rounded-top-4">
                        <h5 class="mb-0"><i class="bi bi-star-fill me-2"></i>Plan Actual</h5>
                    </div>
                    <div class="card-body p-4 text-center">
                        <% 
                            Suscripcion activa = (Suscripcion) request.getAttribute("suscripcionActiva");
                            if (activa != null) { 
                        %>
                            <h3 class="fw-bold text-dark mb-1"><%= activa.getTipoPlan().getNombre() %></h3>
                            <p class="text-muted mb-3">Vence el: <%= activa.getFechaHasta().toLocalDate() %></p>
                            <span class="badge bg-success bg-opacity-10 text-success border border-success px-3 py-2 rounded-pill">Activa</span>
                        <% } else { %>
                            <i class="bi bi-slash-circle text-muted fs-1 mb-2 d-block"></i>
                            <h5 class="text-muted">Sin plan activo</h5>
                            <p class="small text-muted mb-0">Selecciona un plan del catálogo para comenzar.</p>
                        <% } %>
                    </div>
                </div>

                <!-- Tarjeta de Pagos Pendientes -->
                <div class="card border-0 shadow-sm rounded-4 border-start border-warning border-4">
                    <div class="card-body p-4">
                        <h5 class="fw-bold text-dark mb-3"><i class="bi bi-receipt me-2 text-warning"></i>Facturas Pendientes</h5>
                        
                        <% 
                            List<PagoSuscripcion> pendientes = (List<PagoSuscripcion>) request.getAttribute("pagosPendientes");
                            if (pendientes != null && !pendientes.isEmpty()) {
                                for(PagoSuscripcion p : pendientes) {
                        %>
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-3 mb-3">
                                <div>
                                    <h6 class="mb-0 fw-bold">Plan <%= p.getId().getSuscripcionId().getCodigo() %></h6>
                                    <small class="text-muted">$ <%= p.getMonto() %></small>
                                </div>
                                <button type="button" class="btn btn-warning btn-sm fw-bold text-dark" 
                                        data-codplan="<%= p.getId().getSuscripcionId().getCodigo() %>"
                                        data-fechasub="<%= p.getId().getSuscripcionId().getFechaDesde() %>"
                                        data-fechaemi="<%= p.getId().getFechaHoraEmision() %>"
                                        onclick="prepararPago(this)">
                                    Pagar
                                </button>
                            </div>
                        <% 
                                }
                            } else { 
                        %>
                            <p class="text-muted small mb-0"><i class="bi bi-check2-circle me-1"></i>Estás al día con tus pagos.</p>
                        <% } %>
                        
                    </div>
                </div>
            </div>

            <!-- COLUMNA DERECHA: Catálogo de Planes -->
            <div class="col-lg-8">
                <div class="card border-0 shadow-sm rounded-4">
                    <div class="card-body p-4">
                        <h5 class="fw-bold text-dark mb-4"><i class="bi bi-shop me-2 text-primary"></i>Catálogo de Planes</h5>
                        
                        <div class="row g-3">
                            <% 
                                List<TipoPlan> planes = (List<TipoPlan>) request.getAttribute("listaPlanes");
                                if (planes != null && !planes.isEmpty()) {
                                    for(TipoPlan plan : planes) {
                            %>
                            <div class="col-md-6">
                                <div class="card h-100 border bg-white shadow-sm hover-card">
                                    <div class="card-body p-4 d-flex flex-column">
                                        
                                        <!-- Cabecera del Plan -->
                                        <div class="text-center mb-3">
                                            <h5 class="fw-bold text-primary mb-1"><%= plan.getNombre() %></h5>
                                            <p class="text-muted small mb-0"><%= plan.getDetalle() %></p>
                                        </div>
                                        
                                        <!-- Caja de Beneficios / Características -->
                                        <div class="bg-light rounded-3 p-3 flex-grow-1 mb-4">
                                            <span class="d-block small fw-bold text-secondary mb-2">Beneficios incluidos:</span>
                                            <ul class="list-unstyled small mb-0 text-start">
                                                <%
                                                    if (plan.getBeneficios() != null && !plan.getBeneficios().isEmpty()) {
                                                        for(Beneficio beneficio : plan.getBeneficios()) {
                                                %>
                                                    <li class="mb-2 text-muted d-flex align-items-start">
                                                        <i class="bi bi-check-circle-fill text-success me-2 mt-1"></i>
                                                        <span><%= beneficio.getDescripcion() %></span>
                                                    </li>
                                                <%
                                                        }
                                                    } else {
                                                %>
                                                    <li class="text-muted small fst-italic">Beneficios estándar de cochera.</li>
                                                <%
                                                    }
                                                %>
                                            </ul>
                                        </div>
                                        
                                        <!-- Botón de Contratación -->
                                        <form action="<%= request.getContextPath() %>/mis-suscripciones-user" method="POST" class="mt-auto">
                                            <input type="hidden" name="accion" value="contratar">
                                            <input type="hidden" name="codigoPlan" value="<%= plan.getCodigo() %>">
                                            <button type="submit" class="btn btn-outline-primary w-100 fw-bold" onclick="return confirm('¿Deseas dar de alta este plan? Generará una factura pendiente para hoy.');">
                                                <i class="bi bi-cart-plus me-2"></i>Seleccionar Plan
                                            </button>
                                        </form>
                                        
                                    </div>
                                </div>
                            </div>
                            <% 
                                    }
                                } else {
                            %>
                                <div class="col-12 text-center py-5">
                                    <i class="bi bi-inbox text-muted fs-1 d-block mb-3"></i>
                                    <h6 class="text-muted">No hay planes disponibles en este momento.</h6>
                                </div>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- MODAL DE PAGO -->
    <div class="modal fade" id="modalPago" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content rounded-4 border-0 shadow">
                <div class="modal-header bg-light border-0">
                    <h5 class="modal-title fw-bold"><i class="bi bi-credit-card-fill me-2 text-primary"></i>Checkout Seguro</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <form action="<%= request.getContextPath() %>/mis-suscripciones-user" method="POST">
                        <input type="hidden" name="accion" value="pagar">
                        
                        <input type="hidden" name="codPlan" id="modalCodPlan" value="">
                        <input type="hidden" name="fechaSub" id="modalFechaSub" value="">
                        <input type="hidden" name="fechaEmi" id="modalFechaEmi" value="">
                        
                        <div class="mb-3">
                            <label class="form-label text-muted small fw-bold">Método de Pago</label>
                            <select class="form-select" name="tipoPago" required>
                                <option value="" disabled selected>Selecciona tu tarjeta...</option>
                                <% for (TipoPago tp : TipoPago.values()) { 
                                      if (tp == TipoPago.CREDITO || tp == TipoPago.DEBITO) { 
                                %>
                                    <option value="<%= tp.name() %>"><%= tp.name() %></option>
                                <% }} %>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label text-muted small fw-bold">Número de Tarjeta (Simulado)</label>
                            <input type="text" class="form-control" placeholder="**** **** **** ****" required>
                        </div>
                        
                        <div class="row">
                            <div class="col-6 mb-4">
                                <label class="form-label text-muted small fw-bold">Vencimiento</label>
                                <input type="text" class="form-control" placeholder="MM/AA" required>
                            </div>
                            <div class="col-6 mb-4">
                                <label class="form-label text-muted small fw-bold">CVV</label>
                                <input type="text" class="form-control" placeholder="123" required>
                            </div>
                        </div>
                        
                        <button type="submit" class="btn btn-success w-100 py-2 fw-bold fs-5">
                            <i class="bi bi-lock-fill me-2"></i>Pagar Ahora
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
        function prepararPago(btn) {
            document.getElementById('modalCodPlan').value = btn.getAttribute('data-codplan');
            document.getElementById('modalFechaSub').value = btn.getAttribute('data-fechasub');
            document.getElementById('modalFechaEmi').value = btn.getAttribute('data-fechaemi');
            
            var myModal = new bootstrap.Modal(document.getElementById('modalPago'));
            myModal.show();
        }

        <% if (error != null) { %>
            Swal.fire({ icon: 'error', title: 'Oops...', text: '<%= error %>' });
            <% session.removeAttribute("error"); %>
        <% } %>
        
        <% if (exito != null) { %>
            Swal.fire({ icon: 'success', title: '¡Éxito!', text: '<%= exito %>' });
            <% session.removeAttribute("exito"); %>
        <% } %>
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>