package estacionamiento.repository;

import estacionamiento.domain.Lugar;
import estacionamiento.domain.LugarTipoEstadia;
import estacionamiento.domain.TipoEstadia;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LugarTipoEstadiaRepositoryMySQL implements LugarTipoEstadiaRepository {

    private final String url = "jdbc:mysql://localhost:3306/estacionamiento_db";
    private final String usuario = "root";
    private final String contrasenia = "tu_contrasenia";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, usuario, contrasenia);
    }

    @Override
    public LugarTipoEstadia save(LugarTipoEstadia lugarTipoEstadia) {
        
        String sql = "INSERT INTO lugar_tipo_estadia (codigo_lugar, numero_tipo_estadia, fecha_desde) " +
                     "VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE fecha_desde = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lugarTipoEstadia.getLugar().getCodigo());
            stmt.setInt(2, lugarTipoEstadia.getTipoEstadia().getNumero());
            
            Timestamp timestamp = Timestamp.valueOf(lugarTipoEstadia.getFechaDesde());
            stmt.setTimestamp(3, timestamp);
            stmt.setTimestamp(4, timestamp);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al guardar en MySQL: " + e.getMessage());
        }

        return lugarTipoEstadia;
    }

    @Override
    public List<LugarTipoEstadia> findAll() {
        List<LugarTipoEstadia> lista = new ArrayList<>();
        String sql = "SELECT * FROM lugar_tipo_estadia";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar todos en MySQL: " + e.getMessage());
        }

        return lista;
    }

    public Optional<LugarTipoEstadia> findById(String codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
       
    	String sql = "SELECT * FROM lugar_tipo_estadia WHERE codigo_lugar = ? AND numero_tipo_estadia = ? AND fecha_desde = ?";
        LugarTipoEstadia registro = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoLugar);
            stmt.setInt(2, numeroTipoEstadia);
            stmt.setTimestamp(3, Timestamp.valueOf(fechaDesde));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    registro = mapearResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar por ID en MySQL: " + e.getMessage());
        }

        return Optional.ofNullable(registro);
    }

    public void delete(String codigoLugar, int numeroTipoEstadia, LocalDateTime fechaDesde) {
        
        String sql = "DELETE FROM lugar_tipo_estadia WHERE codigo_lugar = ? AND numero_tipo_estadia = ? AND fecha_desde = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoLugar);
            stmt.setInt(2, numeroTipoEstadia);
            stmt.setTimestamp(3, Timestamp.valueOf(fechaDesde));
            
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar en MySQL: " + e.getMessage());
        }
    }


    private LugarTipoEstadia mapearResultSet(ResultSet rs) throws SQLException {
       
        String codigoLugar = rs.getString("codigo_lugar");
        int numeroTipoEstadia = rs.getInt("numero_tipo_estadia");
        LocalDateTime fechaDesde = rs.getTimestamp("fecha_desde").toLocalDateTime();

        Lugar lugar = new Lugar();
        lugar.setCodigo(codigoLugar); 
        
        TipoEstadia tipoEstadia = new TipoEstadia();
        tipoEstadia.setNumero(numeroTipoEstadia);

        return new LugarTipoEstadia(lugar, tipoEstadia, fechaDesde);
    }
}