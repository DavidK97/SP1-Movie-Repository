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

    //Opgave 4.1

    public List<Genre> getAllGenres () {

        return null;
    }

    //Opgave 4.2
    public List<Movie> getMovieByGenre () {

        return null;
    }

}
