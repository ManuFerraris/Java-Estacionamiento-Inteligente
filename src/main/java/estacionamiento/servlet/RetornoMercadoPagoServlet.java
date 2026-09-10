package estacionamiento.servlet;

import java.io.IOException;
import estacionamiento.domain.EstadoPago;
import estacionamiento.service.PagoService;
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
		
		String paymentId = request.getParameter("payment_id");
        String status = request.getParameter("status");
        String externalReference = request.getParameter("external_reference"); //Seria nuestro idPagoLocal
        
        String uri = request.getRequestURI(); // Obtengo la ruta que disparo el servlet
        
        try {
            if (externalReference != null && !externalReference.isEmpty()) {
                Integer idPagoLocal = Integer.parseInt(externalReference);
                
                // Actualizamos la base de datos según el status
                if ("approved".equals(status) || uri.contains("pago-exito")) {
                	
                    pagoService.actualizarEstado(idPagoLocal, EstadoPago.APROBADO, paymentId);
                    request.getSession().setAttribute("exito", "¡Pago acreditado! Tu reserva está confirmada. Comprobante MP: " + paymentId);
                
                } else if ("pending".equals(status) || uri.contains("pago-pendiente")) {
                	
                    pagoService.actualizarEstado(idPagoLocal, EstadoPago.PENDIENTE, paymentId);
                    request.getSession().setAttribute("alerta", "Tu pago está pendiente de acreditación (ej. Rapipago/Pago Fácil).");
                    
                } else {
                    
                    pagoService.actualizarEstado(idPagoLocal, EstadoPago.RECHAZADO, paymentId);
                    request.getSession().setAttribute("error", "El pago fue rechazado. Por favor, intentá con otra tarjeta.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Ocurrió un error al procesar el retorno del pago.");
        }

        // 3. Finalmente, redirigimos al usuario a su panel de reservas para que vea el resultado
        response.sendRedirect(request.getContextPath() + "/mis-reservas-user");
    }
}
