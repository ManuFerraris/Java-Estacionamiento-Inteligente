package estacionamiento.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import estacionamiento.domain.Usuario;
import estacionamiento.domain.RolesUsuario;
import estacionamiento.service.UsuarioService;
import estacionamiento.repository.mysql.UsuarioRepositoryMySQL;

@WebListener
public class AppInitializationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        AppLogger.info("Inicializando contexto y ejecutando seed de usuarios...");

        try {
            UsuarioService usuarioService = new UsuarioService(new UsuarioRepositoryMySQL());

            // 1. ADMIN
            crearUsuarioSiNoExiste(usuarioService, "admin", "Administrador", "Municipal", "3410000000", "Av. Pellegrini 123", "admin@sistema.com", "admin123", RolesUsuario.ADMIN);
            
            // 2. TRABAJADOR
            crearUsuarioSiNoExiste(usuarioService, "trabajador", "Juan", "Pérez", "3411111111", "San Martín 456", "trabajador@sistema.com", "trabajador123", RolesUsuario.TRABAJADOR);
            
            // 3. CLIENTE
            crearUsuarioSiNoExiste(usuarioService, "cliente", "María", "Gómez", "3412222222", "Córdoba 789", "maria@cliente.com", "cliente123", RolesUsuario.CLIENTE);
            
            // 4. ENCARGADO
            crearUsuarioSiNoExiste(usuarioService, "encargado", "Carlos", "López", "3413333333", "Mendoza 1011", "carlos@cochera.com", "encargado123", RolesUsuario.ENCARGADO);
            
        } catch (Exception e) {
            AppLogger.error("Error crítico al ejecutar el seed de usuarios iniciales", e);
        }
    }

    private void crearUsuarioSiNoExiste(UsuarioService service, String username, String nombre, String apellido, String telefono, String direccion, String mail, String password, RolesUsuario rol) {
    	try {
            if (!service.existeUsuario(username)) {
                Usuario u = new Usuario();
                u.setNombreUsuario(username);
                u.setNombre(nombre);
                u.setApellido(apellido);
                u.setNumeroTelefono(telefono); // Agregado
                u.setDireccion(direccion);     // Agregado
                u.setMail(mail);
                u.setContrasenia(password); 
                u.setRol(rol);
                
                if (rol == RolesUsuario.CLIENTE) {
                    service.registrarCliente(u);
                } else {
                    service.registrarUsuario(u);
                }
                AppLogger.info("Usuario seed creado correctamente: [" + username + "] con rol [" + rol + "]");
            } else {
                AppLogger.info("El usuario seed [" + username + "] ya existe en BD. Se omite su creación.");
            }
        } catch (Exception e) {
            AppLogger.error("Fallo al verificar o persistir el usuario seed: " + username, e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        AppLogger.info("Apagando la aplicación y destruyendo el contexto...");
    }
}