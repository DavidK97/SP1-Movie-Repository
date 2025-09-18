package app.persistence;

import app.config.HibernateConfig;
import app.entities.Genre;
import app.entities.Movie;
import app.exceptions.ApiException;
import app.populators.GenrePopulator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenreDAOTest {
    private EntityManagerFactory emf;
    private GenreDAO genreDAO;
    private Genre g1;
    private Genre g2;
    private Genre g3;

    private List<Genre> genres;

    @BeforeAll
    void initOnce() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        genreDAO = new GenreDAO(emf);
    }

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE genre, movie RESTART IDENTITY CASCADE")
                    .executeUpdate();
            em.getTransaction().commit();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to truncate tables", e);
        }

        genres = GenrePopulator.populateGenres(genreDAO);
        if (genres.size() == 3) {
            g1 = genres.get(0);
            g2 = genres.get(1);
            g3 = genres.get(2);
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
        Genre g4 = Genre.builder()
                .name("Comedy")
                .tmdbId(444)
                .build();

        // Act
        genreDAO.create(g4);
        List<Genre> allGenres = genreDAO.getAllGenres();
        Genre foundGenre = genreDAO.findById(4);

        // Assert
        assertEquals(g4, foundGenre);
        assertEquals(4, allGenres.size());

    }


    @Test
    void findById() {
        // Arrange
        Genre g4 = Genre.builder()
                .name("Comedy")
                .tmdbId(444)
                .build();

        // Act
        genreDAO.create(g4);
        Genre foundGenre = genreDAO.findById(4);

        // Assert
        assertThat(foundGenre, samePropertyValuesAs(g4));

    }

    @Test
    void getAllGenres() {
        // Arrange
        int actual = 3;

        // Act
        List<Genre> allGenres = genreDAO.getAllGenres();

        // Assert
        assertEquals(allGenres.size(), actual);
        assertThat(allGenres.get(0), samePropertyValuesAs(g1));
    }

    @Test
    void updateName() {
        // Arrange


        // Act
        boolean result = genreDAO.updateName(1, "Comedy-Horror");
        Genre genre = genreDAO.findById(1);

        // Assert
        assertTrue(result);
        assertEquals("Comedy-Horror", genre.getName());
    }

    @Test
    void delete () {
        // Arrange


        // Act
        boolean result = genreDAO.delete(1);
        Genre genre = genreDAO.findById(1);
        List<Genre> allGenres = genreDAO.getAllGenres();

        // Assert
        assertTrue(result);
        assertNull(genre);
        assertEquals(allGenres.size(), 2);
    }
}