package estacionamiento.servlet;

import java.io.IOException;
import java.time.LocalDateTime;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import estacionamiento.domain.Lugar;
import estacionamiento.domain.TipoEstadia;
import estacionamiento.domain.LugarTipoEstadia;
import estacionamiento.repository.mysql.LugarRepositoryMySQL;
import estacionamiento.repository.mysql.TipoEstadiaRepositoryMySQL;
import estacionamiento.repository.mysql.LugarTipoEstadiaRepositoryMySQL;

@WebServlet("/LugarTipoEstadiaServlet")
public class LugarTipoEstadiaServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private LugarRepositoryMySQL lugarRepo;
    private TipoEstadiaRepositoryMySQL tipoEstadiaRepo;
    private LugarTipoEstadiaRepositoryMySQL lugarTipoEstadiaRepo;

    @Override
    public void init() throws ServletException {
        // Instanciamos los repositorios necesarios para buscar los objetos y armar la relación
        this.lugarRepo = new LugarRepositoryMySQL();
        this.tipoEstadiaRepo = new TipoEstadiaRepositoryMySQL();
        this.lugarTipoEstadiaRepo = new LugarTipoEstadiaRepositoryMySQL();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        try {
            if ("asignar".equals(accion)) {
                // 1. Capturar los datos del Modal
                int codigoLugar = Integer.parseInt(request.getParameter("codigoLugar"));
                int numeroTipoEstadia = Integer.parseInt(request.getParameter("numeroTipoEstadia"));
                
                // El input type="datetime-local" envía la fecha en formato ISO (Ej: 2024-05-16T15:30)
                String fechaString = request.getParameter("fechaDesde");
                LocalDateTime fechaDesde = LocalDateTime.parse(fechaString);
                
                // 2. Buscar los objetos completos en la BD para satisfacer las relaciones @ManyToOne
                Lugar lugar = lugarRepo.buscarPorClave(codigoLugar);
                TipoEstadia tipoEstadia = tipoEstadiaRepo.buscarPorNumero(numeroTipoEstadia);
                
                if (lugar == null || tipoEstadia == null) {
                    throw new IllegalArgumentException("El Lugar o el Tipo de Estadía seleccionado no existen.");
                }
                
                // 3. Crear la entidad intermedia. Su constructor armará automáticamente el LugarTipoEstadiaId
                LugarTipoEstadia asignacion = new LugarTipoEstadia(lugar, tipoEstadia, fechaDesde);
                
                // 4. Guardar en la Base de Datos
                lugarTipoEstadiaRepo.save(asignacion);
                
                // Guardamos el mensaje de éxito en la sesión
                request.getSession().setAttribute("exito", "El tipo de estadía se asignó al lugar correctamente.");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error al asignar estadía: " + e.getMessage());
        }
        
        // Redirigimos de vuelta a la pantalla de Lugares (Patrón PRG)
        response.sendRedirect(request.getContextPath() + "/LugarServlet");
    }
}