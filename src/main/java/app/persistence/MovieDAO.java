package app.persistence;


import app.entities.Actor;
import app.entities.Director;

import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class MovieDAO {
    private final EntityManagerFactory emf;

    public MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }



    public Movie create(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Tjek om en Director allerede findes i DB
            Director director = movie.getDirector(); // Director, som vi skal vide om de findes i DB

            if (director != null) {
                Optional<Director> existingDirector = em.createQuery("SELECT d FROM Director d WHERE d.tmdbId = :tmdbId", Director.class)
                        .setParameter("tmdbId", director.getTmdbId())
                        .getResultStream()
                        .findFirst(); //Director som muligvis findes i DB

                if (existingDirector.isPresent()) {
                    movie.setDirector(existingDirector.get());
                }
            }

            //Tjek om en Actor allerede findes i DB
            Set<Actor> actors = new HashSet<>();

            //For hver actor i en Movies liste af Actors ser vi om de allerede eksisterer i DB
            for (Actor actor : movie.getActors()) {
                Optional<Actor> existingActor = em.createQuery("SELECT a FROM Actor a WHERE a.tmdbId = :tmdbId", Actor.class)
                        .setParameter("tmdbId", actor.getTmdbId())
                        .getResultStream()
                        .findFirst();

                if (existingActor.isPresent()) {
                    actors.add(existingActor.get());
                } else {
                    actors.add(actor);
                }
                movie.setActors(actors);
            }

            em.persist(movie);
            em.getTransaction().commit();
            return movie;
        }
    }



    // Bruges til MoviePopulator for at undgå EntityExistsException på detached entiteter
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

   

    // Opgave 2
    public List<Movie> getAllMovies() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Movie> movieList = em.createQuery("SELECT m FROM Movie m", Movie.class)
                    .getResultList();
        }
    }





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

    public List<Movie> searchMovieByTitle(String movieTitle) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Movie m WHERE LOWER (m.title) LIKE LOWER(:movieTitle)", Movie.class)
                    .setParameter("movieTitle", "%" + movieTitle + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
