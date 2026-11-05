package estacionamiento.util;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class AppLogger {

	private static final Logger logger = Logger.getLogger("EstacionamientoApp");
    private static FileHandler fileHandler;
    
    // El bloque static se ejecuta una sola vez cuando la aplicación arranca
    static {
        try {
            // Buscamos la carpeta de instalación de Tomcat
            String catalinaBase = System.getProperty("catalina.base");
            if (catalinaBase == null) {
                catalinaBase = System.getProperty("user.dir"); // Fallback por si corre fuera de Tomcat
            }
            
            // Apuntamos a la carpeta /logs de Tomcat
            File logDir = new File(catalinaBase, "logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            // Creamos el archivo físico (el parámetro 'true' es para que agregue texto al final y no borre lo anterior)
            String rutaLog = logDir.getAbsolutePath() + File.separator + "estacionamiento-errores.log";
            fileHandler = new FileHandler(rutaLog, true);
            
            // Le damos formato legible para humanos (sin esto, Java lo guarda en XML)
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("[").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("] ");
                    sb.append("[").append(record.getLevel()).append("] ");
                    sb.append(record.getMessage()).append("\n");

                    // Si hay una Excepción (el stack trace), lo escribimos completo
                    if (record.getThrown() != null) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        record.getThrown().printStackTrace(pw);
                        sb.append(sw.toString()).append("\n");
                    }
                    return sb.toString();
                }
            });

            logger.addHandler(fileHandler);
            // setUseParentHandlers(true) hace que ADEMÁS de guardarse en el archivo, se siga viendo en la consola de Eclipse
            logger.setUseParentHandlers(true); 
            
        } catch (Exception e) {
            System.err.println("Fallo crítico al inicializar el sistema de logs: " + e.getMessage());
        }
    }

    // Método estático para registrar ERRORES (Excepciones)
    public static void error(String mensaje, Throwable excepcion) {
        logger.log(Level.SEVERE, mensaje, excepcion);
    }

    // Método estático para registrar INFORMACIÓN o advertencias
    public static void info(String mensaje) {
        logger.info(mensaje);
    }
}