package estacionamiento.servlet;

import java.io.IOException;
import estacionamiento.service.PagoService;
import estacionamiento.util.AppLogger;
import estacionamiento.repository.mysql.PagoRepositoryMySQL;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"/api/pago-exito", "/api/pago-pendiente", "/api/pago-fallo"})
public class RetornoMercadoPagoServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private PagoService pagoService;
	
	@Override
	public void init() throws ServletException {
		this.pagoService = new PagoService(new PagoRepositoryMySQL());
	}
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String externalReference = request.getParameter("external_reference");
            String status = request.getParameter("collection_status"); // "approved", "pending", "rejected"

            if (externalReference != null) {
                
                // SUSCRIPCIÓN
                if (externalReference.startsWith("SUSC_")) {
                    if ("approved".equals(status)) {
                        request.getSession().setAttribute("exito", "¡Pago de suscripción aprobado y acreditado!");
                    } else {
                        request.getSession().setAttribute("error", "El pago de la suscripción está pendiente o fue rechazado.");
                    }
                    
                    response.sendRedirect(request.getContextPath() + "/mis-suscripciones-user");
                    return; 
                } 
                
                // RESERVA DE COCHERA
                else if (externalReference.startsWith("RES_") || externalReference.matches("\\d+")) {
                    if ("approved".equals(status)) {
                        request.getSession().setAttribute("exito", "¡Reserva pagada con éxito!");
                    } else {
                        request.getSession().setAttribute("error", "El pago de la reserva falló.");
                    }
                    
                    response.sendRedirect(request.getContextPath() + "/mis-reservas-user");
                    return;
                }
            }

            response.sendRedirect(request.getContextPath() + "/index.jsp");
            
        } catch (Exception e) {
            AppLogger.error("Error al procesar el retorno de pago desde la pasarela de Mercado Pago", e);
            request.getSession().setAttribute("error", "Ocurrió un error al procesar el estado de tu pago.");
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
}
