package app.persistence;

import app.dtos.MovieDTO;
import app.entities.Movie;
import app.services.MovieConverter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class MovieDAO {
    private EntityManagerFactory emf;

    public MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Movie create (Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();

            return movie;
        }
    }
}
