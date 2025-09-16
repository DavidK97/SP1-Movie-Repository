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

    public Movie create (MovieDTO movieDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            Movie movie = MovieConverter.dtoToEntity(movieDTO);

            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();

            return movie;
        }
    }
}
