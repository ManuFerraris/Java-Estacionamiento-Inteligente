<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="estacionamiento.domain.PagoSuscripcion" %>
<%@ page import="estacionamiento.domain.EstadoPago" %>
<%@ page import="estacionamiento.domain.TipoPago" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Caja y Facturación de Suscripciones</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-light">

	<%@ include file="navbar.jsp" %>
	
    <div class="container-fluid mt-5 px-4">
        <h1 class="mb-4 text-secondary">Caja Municipal - Pagos de Suscripciones</h1>

        <div class="row">
            <!-- COLUMNA IZQUIERDA: Formulario de Cobro -->
            <div class="col-lg-4 col-md-12">
                <div class="card shadow-sm mb-4 border-0">
                    <div class="card-header bg-success text-white">
                        <h5 class="mb-0" id="tituloFormulario"><i class="bi bi-cash-coin me-2"></i>Registrar Cobro</h5>
                    </div>
                    <div class="card-body">
                        <form action="<%= request.getContextPath() %>/pagos-suscripciones-oficina" method="POST" id="formCobro">
                            <input type="hidden" name="accion" value="cobrar">
                            
                            <!-- La cuádruple clave compuesta -->
                            <input type="hidden" name="numeroUsuario" id="numeroUsuarioHidden">
                            <input type="hidden" name="codigoPlan" id="codigoPlanHidden">
                            <input type="hidden" name="fechaDesdeSuscripcion" id="fechaDesdeSuscripcionHidden">
                            <input type="hidden" name="fechaHoraEmision" id="fechaHoraEmisionHidden">
                            
                            <div class="alert alert-info d-none" id="infoCobroBox">
                                <strong>Cobrando a:</strong> <span id="lblCliente"></span><br>
                                <strong>Plan:</strong> <span id="lblPlan"></span><br>
                                <strong class="fs-5 mt-2 d-block text-success">Monto: $<span id="lblMonto"></span></strong>
                            </div>

                            <div class="mb-3">
                                <label for="tipoPago" class="form-label text-muted fw-bold">Medio de Pago</label>
                                <select class="form-select" id="tipoPago" name="tipoPago" required disabled>
								    <option value="" disabled selected>Seleccione el medio de pago...</option>
								    <% for (TipoPago tp : TipoPago.values()) { 
								          if (tp != TipoPago.A_DEFINIR) { 
								    %>
								            <option value="<%= tp.name() %>"><%= tp.name() %></option>
								    <%    }
								       } 
								    %>
								</select>
                            </div>
                            
                            <button type="submit" class="btn btn-success w-100 mt-2" id="btnGuardar" disabled>
                                <i class="bi bi-check2-square me-2"></i>Confirmar Pago
                            </button>
                            <button type="button" class="btn btn-secondary w-100 d-none mt-2" id="btnCancelar" onclick="cancelarCobro()">
                                <i class="bi bi-x-circle me-2"></i>Cancelar Selección
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- COLUMNA DERECHA: Tabla con el listado de pagos -->
            <div class="col-lg-8 col-md-12">
                <div class="card shadow-sm border-0">
                    <div class="card-header bg-white border-bottom d-flex justify-content-between align-items-center">
                        <h5 class="mb-0 text-dark"><i class="bi bi-receipt me-2"></i>Historial de Comprobantes</h5>
                        <button class="btn btn-outline-primary btn-sm" id="btnFiltroPendientes" onclick="togglePendientes()">
                            <i class="bi bi-funnel me-1"></i>Ver Solo Pendientes
                        </button>
                    </div>
                    <div class="card-body p-0 table-responsive">
                        <table class="table table-striped table-hover mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-3">Cliente / Plan</th>
                                    <th>Emisión</th>
                                    <th>Monto</th>
                                    <th>Estado</th>
                                    <th class="text-center">Acciones</th>
                                </tr>
                            </thead>
                            <tbody class="align-middle">
                                <%
                                    List<PagoSuscripcion> lista = (List<PagoSuscripcion>) request.getAttribute("listaPagos");
                                    DateTimeFormatter formatoISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                                    DateTimeFormatter formatoVisual = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                                    
                                    if (lista != null && !lista.isEmpty()) {
                                        for (PagoSuscripcion p : lista) {
                                            // Extraemos las 4 claves para los botones
                                            int numUsuario = p.getSuscripcion().getId().getNumero();
                                            int codPlan = p.getSuscripcion().getId().getCodigo();
                                            String fechaSubISO = p.getSuscripcion().getId().getFechaDesde().format(formatoISO);
                                            String fechaEmiISO = p.getId().getFechaHoraEmision().format(formatoISO);
                                            
                                            // Datos visuales
                                            String nombreCliente = p.getSuscripcion().getUsuario().getNombre() + " " + p.getSuscripcion().getUsuario().getApellido();
                                            String nombrePlan = p.getSuscripcion().getTipoPlan().getNombre();
                                            String fechaEmiVisual = p.getId().getFechaHoraEmision().format(formatoVisual);
                                            
                                            // Variable para el filtro de JS
                                            String claseEstado = p.getEstado() == EstadoPago.PENDIENTE ? "fila-pendiente" : "fila-historico";
                                %>
                                            <tr class="fila-pago <%= claseEstado %>">
                                                <td class="ps-3">
                                                    <span class="fw-semibold text-dark d-block"><%= nombreCliente %></span>
                                                    <small class="text-primary fw-bold"><%= nombrePlan %></small>
                                                </td>
                                                <td><small class="text-muted"><%= fechaEmiVisual %></small></td>
                                                <td class="fw-bold text-success">$<%= p.getMonto() %></td>
                                                <td>
                                                    <% if (p.getEstado() == EstadoPago.PENDIENTE) { %>
                                                        <span class="badge bg-warning text-dark"><i class="bi bi-clock-history me-1"></i>Pendiente</span>
                                                    <% } else if (p.getEstado() == EstadoPago.PAGADO) { %>
                                                        <span class="badge bg-success"><i class="bi bi-check-circle me-1"></i>Pagado</span>
                                                        <small class="d-block text-muted mt-1" style="font-size: 0.70rem;">(<%= p.getTipoPago() %>)</small>
                                                    <% } else if (p.getEstado() == EstadoPago.VENCIDO) { %>
                                                        <span class="badge bg-danger"><i class="bi bi-exclamation-triangle me-1"></i>Vencido</span>
                                                    <% } else { %>
                                                        <span class="badge bg-secondary"><i class="bi bi-x-circle me-1"></i>Cancelado</span>
                                                    <% } %>
                                                </td>
                                                <td>
                                                    <div class="d-flex justify-content-center gap-2">
                                                        <% if (p.getEstado() == EstadoPago.PENDIENTE) { %>
                                                            <!-- Botón Iniciar Cobro (Pasa datos a la caja izquierda) -->
                                                            <button type="button" class="btn btn-success btn-sm" 
                                                                    title="Registrar Cobro"
                                                                    onclick="prepararCobro('<%= numUsuario %>', '<%= codPlan %>', '<%= fechaSubISO %>', '<%= fechaEmiISO %>', '<%= nombreCliente.replace("'", "\\'") %>', '<%= nombrePlan.replace("'", "\\'") %>', '<%= p.getMonto() %>')">
                                                                <i class="bi bi-currency-dollar"></i> Cobrar
                                                            </button>
                                                            
                                                            <!-- Botón Anular (Baja lógica del comprobante) -->
                                                            <form action="<%= request.getContextPath() %>/pagos-suscripciones-oficina" method="POST" class="m-0">
                                                                <input type="hidden" name="accion" value="anular">
                                                                <input type="hidden" name="numeroUsuario" value="<%= numUsuario %>">
                                                                <input type="hidden" name="codigoPlan" value="<%= codPlan %>">
                                                                <input type="hidden" name="fechaDesdeSuscripcion" value="<%= fechaSubISO %>">
                                                                <input type="hidden" name="fechaHoraEmision" value="<%= fechaEmiISO %>">
                                                                
                                                                <button type="submit" class="btn btn-outline-danger btn-sm" 
                                                                        title="Anular Comprobante"
                                                                        onclick="return confirm('¿Seguro que deseas anular este comprobante de pago?');">
                                                                    <i class="bi bi-x-octagon"></i>
                                                                </button>
                                                            </form>
                                                        <% } else { %>
                                                            <span class="text-muted"><small>Procesado</small></span>
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
                                                No hay comprobantes de pago registrados.
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
        function prepararCobro(numUsu, codPlan, fechaSub, fechaEmi, cliente, plan, monto) {
            // Llenar inputs ocultos con la clave cuádruple
            document.getElementById('numeroUsuarioHidden').value = numUsu;
            document.getElementById('codigoPlanHidden').value = codPlan;
            document.getElementById('fechaDesdeSuscripcionHidden').value = fechaSub;
            document.getElementById('fechaHoraEmisionHidden').value = fechaEmi;
            
            // Mostrar info en la caja azul
            document.getElementById('lblCliente').innerText = cliente;
            document.getElementById('lblPlan').innerText = plan;
            document.getElementById('lblMonto').innerText = monto;
            document.getElementById('infoCobroBox').classList.remove('d-none');
            
            // Habilitar controles del formulario
            document.getElementById('tipoPago').disabled = false;
            document.getElementById('tipoPago').options[0].selected = true; // reset
            document.getElementById('btnGuardar').disabled = false;
            document.getElementById('btnCancelar').classList.remove('d-none');
            
            // Scroll suave hacia el formulario (útil en móviles)
            document.getElementById('tituloFormulario').scrollIntoView({ behavior: 'smooth' });
        }
        
        function cancelarCobro() {
            document.getElementById('numeroUsuarioHidden').value = '';
            document.getElementById('codigoPlanHidden').value = '';
            document.getElementById('fechaDesdeSuscripcionHidden').value = '';
            document.getElementById('fechaHoraEmisionHidden').value = '';
            
            document.getElementById('infoCobroBox').classList.add('d-none');
            
            document.getElementById('tipoPago').disabled = true;
            document.getElementById('btnGuardar').disabled = true;
            document.getElementById('btnCancelar').classList.add('d-none');
        }

        // Script para filtrar solo los pagos PENDIENTES
        let mostrandoSoloPendientes = false;
        function togglePendientes() {
            mostrandoSoloPendientes = !mostrandoSoloPendientes;
            let btn = document.getElementById('btnFiltroPendientes');
            let filasHistoricas = document.querySelectorAll('.fila-historico');
            
            if (mostrandoSoloPendientes) {
                btn.innerHTML = '<i class="bi bi-list-ul me-1"></i>Mostrar Todos';
                btn.classList.replace('btn-outline-primary', 'btn-primary');
                filasHistoricas.forEach(fila => fila.style.display = 'none');
            } else {
                btn.innerHTML = '<i class="bi bi-funnel me-1"></i>Ver Solo Pendientes';
                btn.classList.replace('btn-primary', 'btn-outline-primary');
                filasHistoricas.forEach(fila => fila.style.display = '');
            }
        }

        <% if (error != null && !error.isEmpty()) { %>
            Swal.fire({
                icon: 'error',
                title: 'Error en el cobro',
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