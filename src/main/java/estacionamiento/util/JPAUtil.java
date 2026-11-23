package estacionamiento.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class JPAUtil {

	// Creamos la factoría una sola vez (Singleton)
    private static final EntityManagerFactory emf = buildEntityManagerFactory();

    private static EntityManagerFactory buildEntityManagerFactory() {
        try {
            Map<String, String> properties = new HashMap<>();

            // Buscamos las variables de entorno (las de Railway)
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");

            // 2. Si detectamos que DB_URL existe, estamos en PRODUCCIÓN.
            // Sobrescribimos dinámicamente lo que dice el persistence.xml
            if (dbUrl != null && !dbUrl.trim().isEmpty()) {
                properties.put("jakarta.persistence.jdbc.url", dbUrl);
                properties.put("jakarta.persistence.jdbc.user", dbUser);
                properties.put("jakarta.persistence.jdbc.password", dbPassword);
                properties.put("hibernate.connection.pool_size", "2");
                properties.put("hibernate.show_sql", "false");
                
                AppLogger.info("JPA inicializado en modo PRODUCCIÓN (Variables de entorno inyectadas).");
            } else {
                AppLogger.info("JPA inicializado en modo LOCAL (Usando persistence.xml por defecto).");
            }
            
            return Persistence.createEntityManagerFactory("EstacionamientoPU", properties);
            
        } catch (Throwable ex) {
            AppLogger.error("Error crítico al inicializar el EntityManagerFactory", (Exception) ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
    
    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
	
}
