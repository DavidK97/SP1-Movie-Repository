package app.persistence;

import app.dtos.MovieDTO;
import app.entities.Genre;
import app.entities.Movie;
import app.services.MovieConverter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.List;

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

    // Opgave 2
    public List<Movie> getAllMovies () {

        return null;
    }

    //Opgave 3



    //Opgave 5.1
    public boolean deleteMovie () {

        return false;
    }

    //Opgave 5.2
    public Movie updateTitleAndReleaseDate (int movieId, String newTitle, LocalDate newReleaseDate) {

        return null;
    }

}
