package app.persistence;

import app.entities.Actor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ActorDAO {
    private final EntityManagerFactory emf;

    public ActorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Actor create(Actor actor) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(actor);
            em.getTransaction().commit();
            return actor;
        }
    }

    public Actor upsert(Actor actor) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Actor existing = findByTmdbId(em, actor.getTmdbId());
            if (existing == null) {
                em.persist(actor);
            } else {
                actor = em.merge(actor); // Hibernate laver update automatisk
            }
            em.getTransaction().commit();
            return actor;
        }
    }

    /** Hjælper: alle actors, der er brugt i mindst én film. */
    public List<Actor> findAllUsedInMovies() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT a FROM Actor a JOIN a.movies m", Actor.class)
                    .getResultList();
        }
    }

    private Actor findByTmdbId(EntityManager em, int tmdbId) {
        List<Actor> list = em.createQuery(
                        "SELECT a FROM Actor a WHERE a.tmdbId = :id", Actor.class)
                .setParameter("id", tmdbId)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public Actor findActorByImdbId(int tmdbId) {
        try (EntityManager em = emf.createEntityManager()) {

            Actor foundActor = em.createQuery("SELECT a FROM Actor a WHERE a.tmdbId = :tmdbId", Actor.class)
                    .setParameter("tmdbId", tmdbId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            return foundActor;
        }
    }
}
