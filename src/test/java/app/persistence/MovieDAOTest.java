package app.persistence;

import app.config.HibernateConfig;
import app.entities.Genre;
import app.entities.Movie;
import app.exceptions.ApiException;
import app.populators.GenrePopulator;
import app.populators.MoviePopulator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieDAOTest {
    private EntityManagerFactory emf;
    private GenreDAO genreDAO;
    private MovieDAO movieDAO;
    private DirectorDAO directorDAO;
    private ActorDAO actorDAO;

    private Movie m1;
    private Movie m2;
    private Movie m3;

    private List<Movie> movies;

    @BeforeAll
    void initOnce() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        genreDAO = new GenreDAO(emf);
        movieDAO = new MovieDAO(emf);
        actorDAO = new ActorDAO(emf);
        directorDAO = new DirectorDAO(emf);
    }

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE genre, movie, actor, director, movie_actor, movie_genre RESTART IDENTITY CASCADE")
                    .executeUpdate();
            em.getTransaction().commit();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to truncate tables", e);
        }

        movies = MoviePopulator.populateMovies(movieDAO, genreDAO, actorDAO, directorDAO);
        if (movies.size() == 3) {
            m1 = movies.get(0);
            m2 = movies.get(1);
            m3 = movies.get(2);
        } else {
            throw new ApiException(500, "Populator doesnt work");
        }
    }

    @AfterAll
    void tearDown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    void getInstance () {
        assertNotNull(emf);
    }

    @Test
    void create() {
            // Arrange
            Movie m4 = Movie.builder()
                    .tmdbId(1222)
                    .originalTitle("test movie")
                    .title("Test movie")
                    .originalLanguage("en")
                    .overview("test")
                    .popularity(5)
                    .releaseDate(LocalDate.of(2222, 2, 22))
                    .voteAverage(10)
                    .voteCount(100)
                    .adult(false)
                    .build();

            // Act
            movieDAO.create(m4);
            List<Movie> allMovies = movieDAO.getAllMovies();
            Movie foundMovie = movieDAO.findById(m4.getId());

            // Assert
            assertNotNull(foundMovie);
            assertEquals(m4.getTmdbId(), foundMovie.getTmdbId());
            assertEquals(allMovies.size(), 4);
    }

    @Test
    void getAllMovies() {
        // Arrange
        int actual = 3;
        int tmdbId = 1001; //Kendes fra MoviePopulator

        // Act
        List<Movie> movies = movieDAO.getAllMovies();

        // Assert
        assertEquals(movies.size(), actual);
        assertEquals(movies.get(0).getTmdbId(), tmdbId);
    }

    @Test
    void deleteMovie() {
    }

    @Test
    void updateTitleAndReleaseDate() {
    }

    @Test
    void getMoviesByGenre() {
        // Arrange
        // Vi ved fra MoviePopulator at 2 film har genren Drama
        int actual1 = 2;
        int actual2 = 0;

        // Act
        List<Movie> movies = movieDAO.getMoviesByGenre("Drama");
        List<Movie> movies2 = movieDAO.getMoviesByGenre("Comedy");

        // Assert
        assertEquals(movies.size(), actual1);
        assertEquals(movies2.size(), actual2);
    }

    @Test
    void getAverageForAllMovies() {
        double avg = movieDAO.getAverageForAllMovies();
        assertTrue(avg >= 0.0);
    }

    @Test
    void getTopTenMovies_desc() {
        List<Movie> top = movieDAO.getTopTenMovies(true);
        assertFalse(top.isEmpty());
        assertTrue(top.size() <= 10);
        for (int i = 1; i < top.size(); i++) {
            assertTrue(top.get(i - 1).getVoteAverage() >= top.get(i).getVoteAverage());
        }
    }

    @Test
    void getTopTenMovies_asc() {
        List<Movie> bottom = movieDAO.getTopTenMovies(false);
        assertFalse(bottom.isEmpty());
        assertTrue(bottom.size() <= 10);
        for (int i = 1; i < bottom.size(); i++) {
            assertTrue(bottom.get(i - 1).getVoteAverage() <= bottom.get(i).getVoteAverage());
        }
    }

    @Test
    void getMostPopularMovies() {
        List<Movie> popular = movieDAO.getMostPopularMovies();
        assertFalse(popular.isEmpty());
        assertTrue(popular.size() <= 10);
        for (int i = 1; i < popular.size(); i++) {
            assertTrue(popular.get(i - 1).getPopularity() >= popular.get(i).getPopularity());
        }
    }
}