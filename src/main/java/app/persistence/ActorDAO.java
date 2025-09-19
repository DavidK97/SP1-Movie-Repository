package app.persistence;

import app.entities.Actor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

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
