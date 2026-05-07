package estacionamiento.repository;
import java.util.List;

import estacionamiento.domain.Usuario;

public interface UsuarioRepository {
	void guardar(Usuario usuario);
    Usuario buscarPorNumero(int numero);
    List<Usuario> obtenerTodos();
    void actualizar(int numero, Usuario usuario);
    void eliminar(int numero);
}
