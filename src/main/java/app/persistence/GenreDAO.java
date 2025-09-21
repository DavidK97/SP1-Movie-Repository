package app.persistence;

import app.entities.Genre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class GenreDAO {
    private final EntityManagerFactory emf;

    public GenreDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Genre create(Genre genre) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(genre);
            em.getTransaction().commit();
            return genre;
        }
    }

    public Genre upsert(Genre genre) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Genre existing = findByTmdbId(em, genre.getTmdbId());
            if (existing == null) {
                em.persist(genre);
            } else {
                genre = em.merge(genre);
            }
            em.getTransaction().commit();
            return genre;
        }
    }

    public boolean delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            int result = em.createQuery("DELETE FROM Genre g WHERE g.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            em.getTransaction().commit();
            return result > 0;
        }
    }

    public boolean updateName(int id, String name) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            int result = em.createQuery(
                            "UPDATE Genre g SET g.name = :name WHERE g.id = :id")
                    .setParameter("name", name)
                    .setParameter("id", id)
                    .executeUpdate();
            em.getTransaction().commit();
            return result > 0;
        }
    }

    public Genre findById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Genre.class, id);
        }
    }

    public List<Genre> getAllGenres() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT g FROM Genre g", Genre.class)
                    .getResultList();
        }
    }

    // ---- helpers ----
    private Genre findByTmdbId(EntityManager em, int tmdbId) {
        List<Genre> list = em.createQuery(
                        "SELECT g FROM Genre g WHERE g.tmdbId = :id", Genre.class)
                .setParameter("id", tmdbId)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
}
