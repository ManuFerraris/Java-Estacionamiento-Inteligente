<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="estacionamiento.domain.PrecioHistoricoTV" %>
<%@ page import="estacionamiento.domain.TipoVehiculo" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Historial de Precios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-light">

	<%@ include file="navbar.jsp" %>
	
    <div class="container-fluid mt-5 px-4">
        <h1 class="mb-4 text-secondary">Historial de Precios por Tipo de Vehículo</h1>

        <div class="row">
            <!-- COLUMNA IZQUIERDA: Formulario de Alta/Edición -->
            <div class="col-lg-4 col-md-12">
                <div class="card shadow-sm mb-4 border-0">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0" id="tituloFormulario"><i class="bi bi-currency-dollar me-2"></i>Registrar Nuevo Precio</h5>
                    </div>
                    <div class="card-body">
                       <form action="<%= request.getContextPath() %>/preciosHistoricosTV-oficina" method="POST" id="formPrecio">
                            <input type="hidden" name="accion" id="accionForm" value="crear">
                            
                            <!-- Claves compuestas para edición -->
                            <input type="hidden" name="numeroTipoVehiculoHidden" id="numeroTipoVehiculoHidden" value="">
                            <input type="hidden" name="fechaDesdeHidden" id="fechaDesdeHidden" value="">
                            
                            <div class="mb-3">
                                <label for="numeroTipoVehiculo" class="form-label text-muted fw-bold">Tipo de Vehículo</label>
								<select class="form-select" id="numeroTipoVehiculo" name="numeroTipoVehiculo" required>
   									 <option value="" disabled selected>Seleccione un tipo de vehículo...</option>
                                    <% 
                                    List<TipoVehiculo> listaTiposVehiculo =
                                    (List<TipoVehiculo>) request.getAttribute("listaTiposVehiculo");
                           				 if (listaTiposVehiculo != null) {
                           					 for (TipoVehiculo tipoVehiculo : listaTiposVehiculo) {
                                    %>
                                               <option value="<%= tipoVehiculo.getNumero() %>"><%= tipoVehiculo.getNombre() %></option>
                                    <% 
                                            }
                                        } 
                                    %>
                                </select>
                            </div>
                            
                            <div class="mb-3">
    							<label for="precio" class="form-label text-muted fw-bold">
       								 Precio ($)
   								 </label>

    							<input type="number"
          						 class="form-control"
           						id="precio"
           						name="precio"
           						step="0.01"
           						min="0"
           						placeholder="Ej. 1500.50"
          						 required>

    						<div class="form-text">
        						La fecha de vigencia se establecerá automáticamente al momento de guardar.
   							 </div>
						</div>

				<button type="submit"
       				 class="btn btn-success w-100 mb-2 mt-2"
					id="btnGuardar">
    				<i class="bi bi-save me-2"></i>Guardar Precio
				</button>

				<button type="button"
        			class="btn btn-secondary w-100 d-none"
        			id="btnCancelar"
        			onclick="cancelarEdicion()">
   					 <i class="bi bi-x-circle me-2"></i>Cancelar Edición
				</button>

				</form>
                    </div>
                </div>
            </div>

            <!-- COLUMNA DERECHA: Tabla con el listado -->
            <div class="col-lg-8 col-md-12">
                <div class="card shadow-sm border-0">
                    <div class="card-header bg-white border-bottom d-flex justify-content-between align-items-center">
					    <h5 class="mb-0 text-dark"><i class="bi bi-clock-history me-2"></i>Registro Histórico</h5>
					    <!-- Botón de filtro -->
					    <button class="btn btn-outline-primary btn-sm" id="btnFiltroVigente" onclick="togglePreciosVigentes()">
					        <i class="bi bi-funnel me-1"></i>Mostrar Precio Vigente
					    </button>
					</div>
                    <div class="card-body p-0 table-responsive">
                        <table class="table table-striped table-hover mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-3">Tipo de Vehículo</th>
                                    <th>Fecha de Vigencia (Desde)</th>
                                    <th>Precio ($)</th>
                                    <th class="text-center">Acciones</th>
                                </tr>
                            </thead>
                            <tbody class="align-middle">
                                <%
                                List<PrecioHistoricoTV> lista = (List<PrecioHistoricoTV>) request.getAttribute("listaPrecios");
                                    DateTimeFormatter formatoVisual = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                                    
                                    if (lista != null && !lista.isEmpty()) {
                                    	for (PrecioHistoricoTV ph : lista) {
                                            // Extraemos los datos de la clave compuesta
                                            Integer numeroTipoVehiculo = ph.getId().getNumeroTipoVehiculo();
                                            String fechaIso = ph.getId().getFechaDesde().toString(); // Formato para el Servlet
                                            String fechaVisual = ph.getId().getFechaDesde().format(formatoVisual); // Formato entendible para el usuario
                                %>
                                            <tr class="fila-precio" data-tipo-vehiculo="<%= numeroTipoVehiculo %>">
                                                <td class="ps-3 fw-bold text-primary">
                                                   <%= ph.getTipoVehiculo() != null
      										  ? ph.getTipoVehiculo().getNombre(): "Sin Tipo de Vehículo" %>
                                                </td>
                                                <td><i class="bi bi-calendar-event me-2 text-muted"></i><%= fechaVisual %></td>
                                                <td class="fw-semibold text-success">$<%= ph.getPrecio() %></td>
                                                <td>
                                                    <div class="d-flex justify-content-center gap-2">
                                                        <!-- Botón Editar -->
                                                        <button type="button" class="btn btn-warning btn-sm text-dark"
        														title="Corregir Precio"
       															onclick="cargarDatosEdicion('<%= numeroTipoVehiculo %>', '<%= fechaIso %>', '<%= ph.getPrecio() %>')">
																<i class="bi bi-pencil-square"></i>

														</button>
                            
                                                        <!-- Botón Eliminar Físico (No hay baja lógica aquí) -->
                                                        <form action="<%= request.getContextPath() %>/preciosHistoricosTV-oficina"
																method="POST"
																class="m-0">

    													<input type="hidden" name="accion" value="eliminar">

													    <input type="hidden"
													           name="numeroTipoVehiculo"
													           value="<%= numeroTipoVehiculo %>">

													    <input type="hidden"
													           name="fechaDesde"
													           value="<%= fechaIso %>">
													
													    <button type="submit"
													            class="btn btn-outline-danger btn-sm"
													            title="Eliminar Registro"
													            onclick="return confirm('ATENCIÓN: Esto eliminará el registro físicamente de la base de datos. ¿Deseas continuar?')">
													
													        <i class="bi bi-trash"></i>
													
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
                                            <td colspan="4" class="text-center text-muted py-4">
                                                <i class="bi bi-inbox fs-2 d-block mb-2"></i>
                                                No hay registros históricos de precios.
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
        function cargarDatosEdicion(numeroTipoVehiculo, fechaIso, precio) {
            document.getElementById('accionForm').value = 'editar';
            
            // Llenamos las claves compuestas ocultas
            document.getElementById('numeroTipoVehiculoHidden').value = numeroTipoVehiculo;
            document.getElementById('fechaDesdeHidden').value = fechaIso;
            
            document.getElementById('precio').value = precio;
            
         // Bloqueamos el tipo de vehículo, ya que no se puede alterar la identidad de un registro histórico
            let selectTipoVehiculo = document.getElementById('numeroTipoVehiculo');
			selectTipoVehiculo.value = numeroTipoVehiculo;
            selectTipoVehiculo.disabled = true;
            
            document.getElementById('tituloFormulario').innerHTML = '<i class="bi bi-pencil-square me-2"></i>Corregir Precio Histórico';
            
            let btn = document.getElementById('btnGuardar');
            btn.innerHTML = '<i class="bi bi-arrow-clockwise me-2"></i>Actualizar Precio';
            btn.className = 'btn btn-warning w-100 mb-2 mt-2 fw-bold text-dark';
            
            document.getElementById('btnCancelar').classList.remove('d-none');
        }
        
        function cancelarEdicion() {
            document.getElementById('accionForm').value = 'crear';
            document.getElementById('numeroTipoVehiculoHidden').value = '';
            document.getElementById('fechaDesdeHidden').value = '';
            document.getElementById('precio').value = '';
            
            let selectTipoVehiculo = document.getElementById('numeroTipoVehiculo');
			selectTipoVehiculo.value = '';
			selectTipoVehiculo.disabled = false;
            
            document.getElementById('tituloFormulario').innerHTML = '<i class="bi bi-currency-dollar me-2"></i>Registrar Nuevo Precio';
            
            let btn = document.getElementById('btnGuardar');
            btn.innerHTML = '<i class="bi bi-save me-2"></i>Guardar Precio';
            btn.className = 'btn btn-success w-100 mb-2 mt-2';
            
            document.getElementById('btnCancelar').classList.add('d-none');
        }
        
        let mostrandoSoloVigentes = false;
        
        function togglePreciosVigentes() {
            mostrandoSoloVigentes = !mostrandoSoloVigentes;
            let btn = document.getElementById('btnFiltroVigente');
            let filas = document.querySelectorAll('.fila-precio');
            let tiposVehiculoVistos = new Set();

            if (mostrandoSoloVigentes) {
                // MODO: Solo Vigentes
                btn.innerHTML = '<i class="bi bi-list-ul me-1"></i>Mostrar Todos';
                btn.classList.replace('btn-outline-primary', 'btn-primary');

                filas.forEach(fila => {
                    let numeroTipoVehiculo = fila.getAttribute('data-tipo-vehiculo');
                    
                    // Como vienen ordenados de más nuevo a más viejo, el primero que leemos es el vigente
                    if (!tiposVehiculoVistos.has(numeroTipoVehiculo)) {
    				tiposVehiculoVistos.add(numeroTipoVehiculo);
                        fila.style.display = ''; // Lo mantenemos visible
                    } else {
                        fila.style.display = 'none'; // Ocultamos los históricos viejos
                    }
                });
            } else {
                // MODO: Mostrar Todos
                btn.innerHTML = '<i class="bi bi-funnel me-1"></i>Mostrar Precio Vigente';
                btn.classList.replace('btn-primary', 'btn-outline-primary');

                filas.forEach(fila => {
                    fila.style.display = ''; // Volvemos a mostrar todas las filas
                });
            }
        }

        // Habilitar el select justo antes de hacer submit si estaba bloqueado
        document.getElementById('formPrecio').addEventListener('submit', function() {
    document.getElementById('numeroTipoVehiculo').disabled = false;
		});
    
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