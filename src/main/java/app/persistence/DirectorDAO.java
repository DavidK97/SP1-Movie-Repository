package app.persistence;

import app.entities.Director;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class DirectorDAO {
    private final EntityManagerFactory emf;

    public DirectorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /** Kun til nye records. Fejler hvis director med samme tmdbId allerede findes. */
    public Director create(Director director) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(director);
            em.getTransaction().commit();
            return director;
        }
    }

    /** Insert hvis ny, ellers Update (merge) baseret på unik tmdbId. */
    public Director upsert(Director director) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Director existing = findByTmdbId(em, director.getTmdbId());
            if (existing == null) {
                em.persist(director);
            } else {
                director = em.merge(director);
            }
            em.getTransaction().commit();
            return director;
        }
    }

    /** Hjælper: alle directors, der er brugt i mindst én film. */
    public List<Director> findAllDirectorsUsedInMovies() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT d FROM Director d JOIN d.movies m", Director.class)
                    .getResultList();
        }
    }

    private Director findByTmdbId(EntityManager em, int tmdbId) {
        List<Director> list = em.createQuery(
                        "SELECT d FROM Director d WHERE d.tmdbId = :id", Director.class)
                .setParameter("id", tmdbId)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
}
