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

    // Bruges til MoviePopulator for at undgå EntityExistsException på detached entiteter
    public Movie merge (Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(movie);
            em.getTransaction().commit();

            return movie;
        }
    }

    public Movie findById (int id) {
        try(EntityManager em = emf.createEntityManager()) {
            Movie foundMovie = em.find(Movie.class, id);
            return foundMovie;
        }
    }

    // Opgave 2
    public List<Movie> getAllMovies () {
        try(EntityManager em = emf.createEntityManager()) {
            List<Movie> movieList = em.createQuery("SELECT m FROM Movie m", Movie.class)
                    .getResultList();
            return movieList;
        }
    }

    //Opgave 3



    //Opgave 5.1
    public boolean deleteMovie(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            int rows = em.createQuery("DELETE FROM Movie m WHERE m.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            em.getTransaction().commit();
            return rows > 0;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    //Opgave 5.2
    public Movie updateTitleAndReleaseDate(Integer movieId, String newTitle, LocalDate newReleaseDate) {
        Movie movie;
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            movie = em.find(Movie.class, movieId);
            if (movie == null) {
                throw new IllegalArgumentException("Movie with id " + movieId + " does not exist");
            }
            movie.setTitle(newTitle);
            movie.setReleaseDate(newReleaseDate);
            em.merge(movie);
            em.getTransaction().commit();
        }
        return movie;
    }


    //Opgave 4.2
    public List<Movie> getMoviesByGenre(String genre) {
        try (EntityManager em = emf.createEntityManager()) {
            List<Movie> movieList = em.createQuery("SELECT m FROM Movie m " +
                            "JOIN m.genres g " +
                            "WHERE g.name LIKE :genre", Movie.class)
                    .setParameter("genre", genre)
                    .getResultList();
            return movieList;
        }
    }
}
