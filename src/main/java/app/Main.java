package app;

import app.config.HibernateConfig;
import app.dtos.*;
import app.entities.Director;
import app.entities.Genre;
import app.entities.Movie;
import app.persistence.GenreDAO;
import app.persistence.MovieDAO;
import app.services.*;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        MovieDAO movieDAO = new MovieDAO(emf);
        GenreDAO genreDAO = new GenreDAO(emf);

        MovieService movieService = new MovieService(movieDAO);



        /* TODO
            2. Fix duplicates af især Actors og Directors og fix
         */


        // 1. Hent alle Movie
        List<MovieDTO> allMovieDTOs = movieService.getAllDanishMovies();

        // 2. Hent alle Genre
        List<GenreDTO> allGenresDTO = movieService.getAllGenres();

        // 3. Gem alle Genre
        List<Genre> allGenresAsEntities = new ArrayList<>();
        for (GenreDTO dto : allGenresDTO) {
            Genre genre = GenreConverter.dtoToEntity(dto);
            allGenresAsEntities.add(genre);
        }
        allGenresAsEntities.forEach(genre -> genreDAO.create(genre));


        //4. Relationer mellem Movie og Actor og Director sættes for hver Movie
        for (MovieDTO movieDTO : allMovieDTOs) {
            // 4.1 Hent castAndCrewDTO for hver film
            CastAndCrewDTO castAndCrewDTO = movieService.getCastAndCrew(movieDTO.getTmdbId());

            // 4.2 Hent List<Actor> til hver film
            List<ActorDTO> actorsDTO = castAndCrewDTO.getActors();

            // 4.3 Hent CrewDTO director til hver film
            Optional<DirectorDTO> optionalDirector = movieService.getDirectorForMovie(castAndCrewDTO);

            // 5. Map hver MovieDTO til en Entity
            Movie movie = MovieConverter.dtoToEntity(movieDTO);

            // 6. Alle ActorDTOs mappes til Entities og tilføjes til deres film
            actorsDTO.forEach(actorDTO -> movie.addActor(ActorConverter.dtoToEntity(actorDTO)));

            // 7. Der tilføjes en Director til en film
            if (optionalDirector.isPresent()) {
                DirectorDTO directorDTO = optionalDirector.get();
                Director director = DirectorConverter.dtoToEntity(directorDTO);

                director.addMovie(movie);
            }

            // 8. Hvert genreTMDBid som en film har laver vi en relation til en genre med
            allGenresAsEntities.stream()
                    .filter(genre -> movieDTO.getGenreIds().contains(genre.getTmdbId()))
                    .forEach(genre -> movie.addGenre(genre));


            // Hver film gemmes i DB. Cascade.Persist sørger for Director og Actor bliver gemt
            movieDAO.create(movie);
        }

        emf.close();
    }
}
