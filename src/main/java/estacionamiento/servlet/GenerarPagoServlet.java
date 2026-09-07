package estacionamiento.servlet;

import com.mercadopago.MercadoPagoConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/api/generar-pago")
public class GenerarPagoServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
    public void init() throws ServletException {
        // Esto se ejecuta UNA SOLA VEZ cuando el servidor arranca.
        // Aca va el ACCESS_TOKEN del VENDEDOR de prueba.
        MercadoPagoConfig.setAccessToken("APP_USR-5509777146568327-090716-5f557ce2a9e3e30eb8ba00023b514e76-1997921995");
        System.out.println("Mercado Pago SDK Inicializado correctamente en el Backend.");
    }
}
