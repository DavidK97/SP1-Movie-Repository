package app.persistence;

import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.List;

public class MovieDAO {
    private final EntityManagerFactory emf;

    public MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Movie create(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();
            return movie;
        }
    }

    public Movie merge(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Movie managed = em.merge(movie);
            em.getTransaction().commit();
            return managed;
        }
    }

    public Movie findById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Movie.class, id);
        }
    }

    public List<Movie> getAllMovies() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Movie m", Movie.class)
                    .getResultList();
        }
    }

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

    public Movie updateTitleAndReleaseDate(Integer movieId, String newTitle, LocalDate newReleaseDate) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Movie movie = em.find(Movie.class, movieId);
            if (movie == null) {
                throw new IllegalArgumentException("Movie with id " + movieId + " does not exist");
            }
            movie.setTitle(newTitle);
            movie.setReleaseDate(newReleaseDate);
            em.merge(movie);
            em.getTransaction().commit();
            return movie;
        }
    }

    public List<Movie> getMoviesByGenre(String genre) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT m FROM Movie m JOIN m.genres g WHERE g.name LIKE :genre",
                            Movie.class
                    ).setParameter("genre", genre)
                    .getResultList();
        }
    }
}
