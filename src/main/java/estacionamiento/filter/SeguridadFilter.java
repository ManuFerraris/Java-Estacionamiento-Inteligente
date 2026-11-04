package estacionamiento.filter;

import java.io.IOException;

import estacionamiento.domain.RolesUsuario;
import estacionamiento.domain.Usuario;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class SeguridadFilter implements Filter {

	@Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización del filtro si fuera necesaria
    }
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        // Obtenemos la URI relativa para saber a dónde quiere ir el usuario
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());

        // Validacion para el Webhook y el tunel entre Mi PC, Ngrok y MP.
        if (uri.contains("/api/webhook-mp") || uri.contains("/login")) {
            chain.doFilter(request, response);
            return; // Cortamos com la ejecución del filtro acá
        }
        
        // 1. Dejamos pasar recursos estáticos de diseño sin preguntar
        if (path.startsWith("/assets") || path.startsWith("/css") || path.startsWith("/js") || path.contains(".css")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Definimos las rutas públicas (Login, Registro, y la raíz)
        if (path.equals("/login") || path.equals("/registro") || path.equals("/") || path.equals("/logout")) {
            chain.doFilter(request, response);
            return;
        }

        // 3. A partir de aquí, la ruta es privada. Buscamos la sesión.
        // false: si no hay sesión, no creamos una nueva
        HttpSession session = req.getSession(false);
        Usuario usuarioLogueado = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        // Si no está logueado, lo llevamos al login
        if (usuarioLogueado == null) {
            res.sendRedirect(contextPath + "/login");
            return;
        }

        // 4. Si está logueado, evaluamos los permisos (Roles)
        RolesUsuario rolUsuario = usuarioLogueado.getRol();

        // Agrupamos las rutas de gestión administrativa de la municipalidad
        boolean esRutaOficina = path.endsWith("-oficina") || 
                                path.equals("/usuarios") || 
                                path.equals("/suscripciones") || 
                                path.equals("/tipos-plan");

        // Agrupamos las rutas operativas de la cochera física
        boolean esRutaEncargado = path.endsWith("-encargado");

        // Agrupamos las rutas exclusivas de la app del ciudadano
        boolean esRutaCliente = path.endsWith("-user");

        if (esRutaOficina) {
            if (rolUsuario == RolesUsuario.ADMIN || rolUsuario == RolesUsuario.TRABAJADOR) {
                chain.doFilter(request, response);
            } else {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos administrativos para acceder a esta área municipal.");
            }
            return;
        }

        if (esRutaEncargado) {
            // Permitimos al ENCARGADO operar su cochera (y al ADMIN por si necesita supervisar)
            if (rolUsuario == RolesUsuario.ENCARGADO || rolUsuario == RolesUsuario.ADMIN) {
                chain.doFilter(request, response);
            } else {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Esta vista es exclusiva para los encargados operativos de cochera.");
            }
            return;
        }

        if (esRutaCliente) {
            if (rolUsuario == RolesUsuario.CLIENTE) {
                chain.doFilter(request, response);
            } else {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Esta vista es exclusiva para la aplicación de clientes.");
            }
            return;
        }

        // Si es una ruta común (ej. /logout o recursos compartidos), dejamos pasar
        chain.doFilter(request, response);
	}
	
	@Override
    public void destroy() {
        // Limpieza de recursos al apagar el servidor
    }

}
