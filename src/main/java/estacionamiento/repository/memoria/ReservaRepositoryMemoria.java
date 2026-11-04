package estacionamiento.repository.memoria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import estacionamiento.domain.Reserva;
import estacionamiento.domain.claves.ReservaId;
import estacionamiento.repository.ReservaRepository;

public class ReservaRepositoryMemoria implements ReservaRepository {

    private List<Reserva> baseDeDatosMemoria;

    public ReservaRepositoryMemoria() {
        this.baseDeDatosMemoria = new ArrayList<>();
    }

    @Override
    public List<Reserva> obtenerTodos() {
        return this.baseDeDatosMemoria;
    }
    
	@Override
	public void eliminar(ReservaId claveCompuesta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actualizar(Reserva reservaNuevosDatos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Reserva buscarPorClave(ReservaId claveCompuesta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reserva> buscarReservasPendientesSinPagoCompleto(LocalDateTime limiteTiempo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reserva> buscarPorUsuario(Integer numeroUsuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void guardar(Reserva reserva) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Reserva> obtenerActivasPorCochera(int codigoCochera) {
		// TODO Auto-generated method stub
		return null;
	}
}