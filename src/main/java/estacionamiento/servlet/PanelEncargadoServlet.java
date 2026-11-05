package estacionamiento.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import estacionamiento.domain.Cochera;
import estacionamiento.domain.EstadoReserva;
import estacionamiento.domain.Lugar;
import estacionamiento.domain.Reserva;
import estacionamiento.domain.RolesUsuario;
import estacionamiento.domain.Usuario;
import estacionamiento.domain.claves.ReservaId;
import estacionamiento.dto.SalidaDTO;
import estacionamiento.repository.mysql.AsignacionCocheraRepositoryMySQL;
import estacionamiento.repository.mysql.CocheraRepositoryMySQL;
import estacionamiento.repository.mysql.LugarRepositoryMySQL;
import estacionamiento.repository.mysql.PagoRepositoryMySQL;
import estacionamiento.repository.mysql.PagoSuscripcionRepositoryMySQL;
import estacionamiento.repository.mysql.PrecioHistoricoTPRepositoryMySQL;
import estacionamiento.repository.mysql.PrecioHistoricoTVRepositoryMySQL;
import estacionamiento.repository.mysql.ReservaRepositoryMySQL;
import estacionamiento.repository.mysql.SuscripcionRepositoryMySQL;
import estacionamiento.repository.mysql.TipoEstadiaRepositoryMySQL;
import estacionamiento.repository.mysql.TipoPlanRepositoryMySQL;
import estacionamiento.repository.mysql.UsuarioRepositoryMySQL;
import estacionamiento.repository.mysql.VehiculoRepositoryMySQL;
import estacionamiento.service.*;
import estacionamiento.util.AppLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/panel-encargado")
public class PanelEncargadoServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private AsignacionCocheraService asignacionService;
    private LugarService lugarService;
    private ReservaService reservaService;
    private CocheraRepositoryMySQL cocheraRepo;

    @Override
    public void init() throws ServletException {
        AsignacionCocheraRepositoryMySQL asignacionRepo = new AsignacionCocheraRepositoryMySQL();
        UsuarioRepositoryMySQL usuarioRepo = new UsuarioRepositoryMySQL();
        this.cocheraRepo = new CocheraRepositoryMySQL();
        
        ReservaRepositoryMySQL reservaRepo = new ReservaRepositoryMySQL();
        LugarRepositoryMySQL lugarRepo = new LugarRepositoryMySQL();
        VehiculoRepositoryMySQL vehiculoRepo = new VehiculoRepositoryMySQL();
        TipoEstadiaRepositoryMySQL tipoEstadiaRepo = new TipoEstadiaRepositoryMySQL();
        PrecioHistoricoTVRepositoryMySQL precioRepo = new PrecioHistoricoTVRepositoryMySQL();
        PagoRepositoryMySQL pagoRepo = new PagoRepositoryMySQL();
        
        // Inicializamos también los repos de suscripción para que no vayan en null
        SuscripcionRepositoryMySQL suscripcionRepo = new SuscripcionRepositoryMySQL();
        TipoPlanRepositoryMySQL tipoPlanRepo = new TipoPlanRepositoryMySQL();
        PrecioHistoricoTPRepositoryMySQL precioHistRepo = new PrecioHistoricoTPRepositoryMySQL();
        PagoSuscripcionRepositoryMySQL pagoSuscRepo = new PagoSuscripcionRepositoryMySQL();
        
        NotificacionService notifService = new NotificacionService();
        SuscripcionService suscripcionService = new SuscripcionService(
            suscripcionRepo, usuarioRepo, tipoPlanRepo, precioHistRepo, pagoSuscRepo
        ); 

        this.asignacionService = new AsignacionCocheraService(asignacionRepo, usuarioRepo, this.cocheraRepo);
        this.lugarService = new LugarService(lugarRepo);
        this.reservaService = new ReservaService(
            reservaRepo,
            lugarRepo,
            vehiculoRepo,
            usuarioRepo,
            tipoEstadiaRepo,
            precioRepo,
            suscripcionService,
            pagoRepo,
            notifService
        );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        // Permitimos ENCARGADO (y ADMIN por si quiere supervisar)
        if (usuario == null || (usuario.getRol() != RolesUsuario.ENCARGADO && usuario.getRol() != RolesUsuario.ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            List<Cochera> misCocheras = asignacionService.obtenerCocherasDeEncargado(usuario.getNumero());
            if (misCocheras.isEmpty()) {
                request.setAttribute("sinAsignacion", true);
                request.getRequestDispatcher("/WEB-INF/views/panelEncargado.jsp").forward(request, response);
                return;
            }

            String idCocheraParam = request.getParameter("idCochera");
            Cochera cocheraActual = misCocheras.get(0);
            if (idCocheraParam != null && !idCocheraParam.isEmpty()) {
                int idSel = Integer.parseInt(idCocheraParam);
                for (Cochera c : misCocheras) {
                    if (c.getCodigo().equals(idSel)) {
                        cocheraActual = c;
                        break;
                    }
                }
            }

            List<Lugar> lugaresCochera = lugarService.obtenerLugaresPorCochera(cocheraActual.getCodigo());
            List<Reserva> reservasActivas = reservaService.obtenerActivasPorCochera(cocheraActual.getCodigo());

            // 3. Armamos un Mapa (idLugar -> Reserva que está ocupando físicamente el lugar ahora)
            // Un lugar está físicamente ocupado (ROJO) si la reserva está EN_CURSO o SALIDA_PARCIAL
            Map<Integer, Reserva> mapaOcupacionFisica = new HashMap<>();
            for (Reserva r : reservasActivas) {
                if (r.getLugar() != null && 
                   (r.getEstado() == EstadoReserva.EN_CURSO || r.getEstado() == EstadoReserva.SALIDA_PARCIAL)) {
                    mapaOcupacionFisica.put(r.getLugar().getCodigo(), r);
                }
            }

            request.setAttribute("cocheraActual", cocheraActual);
            request.setAttribute("misCocheras", misCocheras);
            request.setAttribute("lugaresCochera", lugaresCochera);
            request.setAttribute("reservasActivas", reservasActivas);
            request.setAttribute("mapaOcupacionFisica", mapaOcupacionFisica);

            request.getRequestDispatcher("/WEB-INF/views/panelEncargado.jsp").forward(request, response);

        } catch (Exception e) {
        	AppLogger.error("Error al cargar el panel operativo del encargado", e);
            request.setAttribute("error", "Error al cargar el panel operativo: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/panelEncargado.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        String idCocheraActual = request.getParameter("codigoCochera");
        String sufijoRedirect = (idCocheraActual != null && !idCocheraActual.isEmpty()) ? "?idCochera=" + idCocheraActual : "";
        
        try {
            if ("registrarIngreso".equals(accion)) {
                ReservaId idReserva = construirReservaIdDesdeRequest(request);
                reservaService.registrarIngreso(idReserva);
                request.getSession().setAttribute("exito", "¡Ingreso registrado! Barrera abierta para la patente " + idReserva.getPatenteVehiculo());
                
            } else if ("procesarSalida".equals(accion)) {
                ReservaId idReserva = construirReservaIdDesdeRequest(request);
                boolean esDefinitiva = Boolean.parseBoolean(request.getParameter("esDefinitiva"));
                
                SalidaDTO resultado = reservaService.procesarIntentoSalida(idReserva, esDefinitiva);
                
                if (resultado.isBarreraAbierta()) {
                    request.getSession().setAttribute("exito", resultado.getMensaje());
                } else {
                    request.getSession().setAttribute("error", resultado.getMensaje() + " Monto a cobrar: $" + resultado.getMontoDeuda());
                    request.getSession().setAttribute("reservaPendientePago", idReserva);
                    request.getSession().setAttribute("montoPendientePago", resultado.getMontoDeuda());
                }
                
            } else if ("cobrarEfectivo".equals(accion)) {
                ReservaId idReserva = construirReservaIdDesdeRequest(request);
                SalidaDTO cobro = reservaService.cobrarYLiberarBarrera(idReserva);
                
                request.getSession().setAttribute("exito", cobro.getMensaje());
                request.getSession().removeAttribute("reservaPendientePago");
                request.getSession().removeAttribute("montoPendientePago");
                
            } else if ("crearLugar".equals(accion)) {
                int codigoCochera = Integer.parseInt(request.getParameter("codigoCochera"));
                int piso = Integer.parseInt(request.getParameter("numeroPiso"));
                String descripcion = request.getParameter("descripcion");

                Cochera cochera = cocheraRepo.buscarPorClave(codigoCochera);
                if (cochera == null) {
                    throw new IllegalArgumentException("La cochera seleccionada no es válida.");
                }

                Lugar nuevoLugar = new Lugar();
                nuevoLugar.setNumeroPiso(piso);
                nuevoLugar.setDescripcion(descripcion);
                nuevoLugar.setCochera(cochera);
                
                lugarService.registrarLugar(nuevoLugar);
                request.getSession().setAttribute("exito", "Nuevo lugar físico (Piso " + piso + ") registrado en la cochera.");
                
            } else if ("editarLugar".equals(accion)) {
                int codigoLugar = Integer.parseInt(request.getParameter("codigoLugar"));
                int codigoCochera = Integer.parseInt(request.getParameter("codigoCochera"));
                int piso = Integer.parseInt(request.getParameter("numeroPiso"));
                String descripcion = request.getParameter("descripcion");

                if (descripcion == null || descripcion.trim().isEmpty()) {
                    throw new IllegalArgumentException("La descripción del lugar no puede estar vacía.");
                }

                Cochera cochera = cocheraRepo.buscarPorClave(codigoCochera);
                if (cochera == null) {
                    throw new IllegalArgumentException("La cochera seleccionada no es válida.");
                }

                Lugar datosActualizados = new Lugar();
                datosActualizados.setNumeroPiso(piso);
                datosActualizados.setDescripcion(descripcion.trim());
                datosActualizados.setCochera(cochera);

                lugarService.actualizarLugar(codigoLugar, datosActualizados);
                request.getSession().setAttribute("exito", "El Box #" + codigoLugar + " fue actualizado/reactivado correctamente.");
                
            } else if ("bajaLugar".equals(accion)) {
                int codigoLugar = Integer.parseInt(request.getParameter("codigoLugar"));
                lugarService.darDeBajaLugar(codigoLugar);
                request.getSession().setAttribute("exito", "El lugar #" + codigoLugar + " fue marcado como Inactivo.");
            }
            
            response.sendRedirect(request.getContextPath() + "/panel-encargado" + sufijoRedirect);
            
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/panel-encargado" + sufijoRedirect);
        } catch (Exception e) {
        	AppLogger.error("Error en la operación de cochera (acción: " + accion + ")", e);
            request.getSession().setAttribute("error", "Error en la operación de cochera: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/panel-encargado" + sufijoRedirect);
        }
    }

    /**
     * Permite armar el ReservaId tanto si el encargado pega el código QR completo 
     * ("PATENTE|ID_USUARIO|ID_TIPO|FECHA_DESDE") como si viene por campos separados.
     */
    private ReservaId construirReservaIdDesdeRequest(HttpServletRequest request) {
        String codigoQR = request.getParameter("codigoQR");
        if (codigoQR != null && !codigoQR.trim().isEmpty()) {
            String[] partes = codigoQR.trim().split("\\|");
            if (partes.length != 4) {
                throw new IllegalArgumentException("El código QR escaneado no tiene un formato válido (Patente|Usuario|Tipo|Fecha).");
            }
            return new ReservaId(
                partes[0].trim(),
                Integer.parseInt(partes[1].trim()),
                Integer.parseInt(partes[2].trim()),
                LocalDateTime.parse(partes[3].trim())
            );
        }

        // Si no vino por string QR unificado, leemos los inputs individuales
        String patente = request.getParameter("patente");
        int numUsuario = Integer.parseInt(request.getParameter("numeroUsuario"));
        int numTipoEstadia = Integer.parseInt(request.getParameter("numeroTipoEstadia"));
        LocalDateTime fechaDesde = LocalDateTime.parse(request.getParameter("fechaDesde"));
        
        return new ReservaId(patente, numUsuario, numTipoEstadia, fechaDesde);
    }
}