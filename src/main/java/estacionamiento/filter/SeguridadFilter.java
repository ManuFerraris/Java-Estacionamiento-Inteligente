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

        // Agrupamos las rutas de gestión de la municipalidad
        boolean esRutaOficina = path.endsWith("-oficina") || 
                                path.equals("/usuarios") || 
                                path.equals("/suscripciones") || 
                                path.equals("/tipos-plan");

        // Agrupamos las rutas exclusivas de la app del usuario final
        boolean esRutaCliente = path.endsWith("-user");

        if (esRutaOficina) {
            if (rolUsuario == RolesUsuario.ADMIN || rolUsuario == RolesUsuario.TRABAJADOR) {
                // Tiene permiso, abrimos la puerta
                chain.doFilter(request, response);
            } else {
                // Es un cliente intentando entrar al panel municipal
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos de empleado para acceder a esta área.");
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

        // Si es una ruta que no clasificamos arriba, dejamos pasar la petición por defecto
        chain.doFilter(request, response);
	}
	
	@Override
    public void destroy() {
        // Limpieza de recursos al apagar el servidor
    }

}
