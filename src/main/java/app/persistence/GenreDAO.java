package app.persistence;

import app.entities.Genre;
import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class GenreDAO {
    private EntityManagerFactory emf;

    public GenreDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Genre create (Genre genre) {
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();
            em.persist(genre);
            em.getTransaction().commit();

            return genre;
        }
    }


    public boolean delete (int id) {
        try(EntityManager em = emf.createEntityManager()) {
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
            int result = em.createQuery("UPDATE Genre g SET g.name = :name WHERE g.id = :id")
                    .setParameter("name", name)
                    .setParameter("id", id)
                    .executeUpdate();
            em.getTransaction().commit();
            return result > 0;
        }
    }


    public Genre findById (int id) {
        try(EntityManager em = emf.createEntityManager()) {
            Genre foundGenre = em.find(Genre.class, id);
            return foundGenre;
        }
    }

    //Opgave 4.1
    public List<Genre> getAllGenres () {
        try (EntityManager em = emf.createEntityManager()) {
            List<Genre> allGenres = em.createQuery("SELECT g FROM Genre g", Genre.class)
                    .getResultList();
            return allGenres;
        }
    }
}
