package app.persistence;

import app.entities.Actor;
import app.entities.Director;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class DirectorDAO {
    private final EntityManagerFactory emf;

    public DirectorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Director create(Director director) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(director);
            em.getTransaction().commit();
            return director;
        }
    }

    public Director findDirectorByTmdbId(int tmdbId) {
        try (EntityManager em = emf.createEntityManager()) {
            Director foundDirector = em.createQuery("SELECT d FROM Director d WHERE d.tmdbId = :tmdbId", Director.class)
                    .setParameter("tmdbId", tmdbId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            return foundDirector;
        }
    }
}
