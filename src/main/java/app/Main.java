package app;

import app.config.HibernateConfig;
import app.dtos.CastAndCrewDTO;
import app.dtos.GenreDTO;
import app.dtos.MovieDTO;
import app.entities.Genre;
import app.entities.Movie;
import app.persistence.GenreDAO;
import app.services.GenreConverter;
import app.services.MovieImportService;
import app.services.MovieService;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        MovieService movieService = new MovieService();
        GenreDAO genreDAO = new GenreDAO(emf);
        MovieImportService importService = new MovieImportService(emf);

        // 1. Hent alle Movie DTOs fra TMDb
        List<MovieDTO> allMovieDTOs = movieService.getAllDanishMovies();

        // 2. Hent alle Genre DTOs og gem i DB
        List<GenreDTO> allGenresDTO = movieService.getAllGenres();
        List<Genre> allGenresAsEntities = new ArrayList<>();
        for (GenreDTO dto : allGenresDTO) {
            Genre genre = GenreConverter.dtoToEntity(dto);
            allGenresAsEntities.add(genreDAO.upsert(genre)); // upsert i stedet for create
        }

        // 3. Behandl hver film med service
        for (MovieDTO movieDTO : allMovieDTOs) {
            CastAndCrewDTO castAndCrewDTO = movieService.getCastAndCrew(movieDTO.getTmdbId());
            Movie imported = importService.importSingleMovie(movieDTO, castAndCrewDTO, allGenresAsEntities);
            System.out.println("Importerede film: " + imported.getTitle());
        }

        emf.close();
    }
}
