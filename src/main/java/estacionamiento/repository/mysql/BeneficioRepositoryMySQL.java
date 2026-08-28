package estacionamiento.repository.mysql;

import java.util.List;

import estacionamiento.domain.Beneficio;
import estacionamiento.domain.claves.BeneficioId;
import estacionamiento.repository.BeneficioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class BeneficioRepositoryMySQL implements BeneficioRepository {

    private EntityManagerFactory emf;
    
    public BeneficioRepositoryMySQL() {
        this.emf = Persistence.createEntityManagerFactory("EstacionamientoPU");
    }
    
    @Override
    public void guardar(Beneficio beneficio) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            em.merge(beneficio);
            tx.commit();
            System.out.println("MySQL: Beneficio registrado correctamente en la base de datos.");
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Beneficio buscarPorClave(int codTP, int numero) {
        EntityManager em = emf.createEntityManager();
        try {
            BeneficioId claveCompuesta = new BeneficioId(codTP, numero);
            return em.find(Beneficio.class, claveCompuesta);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Beneficio> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT b FROM Beneficio b", Beneficio.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizar(int codTP, int numero, Beneficio beneficioNuevosDatos) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            BeneficioId claveCompuesta = new BeneficioId(codTP, numero);
            Beneficio benExistente = em.find(Beneficio.class, claveCompuesta);
            
            if (benExistente != null) {
                benExistente.setDescripcion(beneficioNuevosDatos.getDescripcion());
                benExistente.setFechaBaja(beneficioNuevosDatos.getFechaBaja());
                em.merge(benExistente);
                tx.commit();
                System.out.println("MySQL: Beneficio actualizado con éxito.");
            } else {
                throw new IllegalArgumentException("MySQL: No se encontró el beneficio para actualizar.");
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminar(int codTP, int numero) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            BeneficioId claveCompuesta = new BeneficioId(codTP, numero);
            Beneficio benAEliminar = em.find(Beneficio.class, claveCompuesta);
            
            if (benAEliminar != null) {
                em.remove(benAEliminar);
                tx.commit();
                System.out.println("MySQL: Beneficio eliminado correctamente.");
            } else {
                System.out.println("MySQL: El beneficio no fue encontrado para ser eliminado.");
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public int obtenerProximoNumeroPorPlan(int codigoPlan) {
        EntityManager em = emf.createEntityManager();
        try {
            Integer maxNumero = em.createQuery(
                "SELECT MAX(b.id.numero) FROM Beneficio b WHERE b.id.codigoPlan = :planId", Integer.class)
                .setParameter("planId", codigoPlan)
                .getSingleResult();
                
            return (maxNumero == null) ? 1 : maxNumero + 1;
        } finally {
            em.close();
        }
    }
}