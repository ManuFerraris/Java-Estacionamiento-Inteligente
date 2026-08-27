package estacionamiento.repository;
import java.util.List;

import estacionamiento.domain.Usuario;

public interface UsuarioRepository {
	void guardar(Usuario usuario);
    Usuario buscarPorNumero(Integer numero);
    List<Usuario> obtenerTodos();
    void actualizar(Integer numero, Usuario usuario);
    void eliminar(Integer numero);
}
