package app.services;

import app.dtos.ActorDTO;
import app.dtos.CastAndCrewDTO;
import app.dtos.DirectorDTO;
import app.dtos.MovieDTO;
import app.entities.Actor;
import app.entities.Director;
import app.entities.Genre;
import app.entities.Movie;
import app.persistence.ActorDAO;
import app.persistence.DirectorDAO;
import app.persistence.GenreDAO;
import app.persistence.MovieDAO;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MovieImportService {
    private final ActorDAO actorDAO;
    private final DirectorDAO directorDAO;
    private final GenreDAO genreDAO;
    private final MovieDAO movieDAO;

    public MovieImportService(EntityManagerFactory emf) {
        this.actorDAO    = new ActorDAO(emf);
        this.directorDAO = new DirectorDAO(emf);
        this.genreDAO    = new GenreDAO(emf);
        this.movieDAO    = new MovieDAO(emf);
    }

    public Movie importSingleMovie(MovieDTO movieDTO, CastAndCrewDTO credits, List<Genre> knownGenres) {
        // 0) Opslag for genres (fra parameter eller hent alle fra DB)
        Map<Integer, Genre> genreByTmdbId = new HashMap<>();
        if (knownGenres == null || knownGenres.isEmpty()) {
            genreDAO.getAllGenres().forEach(g -> genreByTmdbId.put(g.getTmdbId(), g));
        } else {
            for (Genre g : knownGenres) {
                genreByTmdbId.put(g.getTmdbId(), g);
            }
        }

        Movie movie = MovieConverter.dtoToEntity(movieDTO);

        if (credits != null) {
            if (credits.getCrew() != null) {
                Optional<DirectorDTO> maybeDirector = credits.getCrew().stream()
                        .filter(c -> "Director".equalsIgnoreCase(c.getJob()))
                        .findFirst();

                if (maybeDirector.isPresent()) {
                    Director director = DirectorConverter.dtoToEntity(maybeDirector.get());
                    director = directorDAO.upsert(director); // undgå dubletter
                    movie.setDirector(director);
                }
            }

            if (credits.getActors() != null) {
                for (ActorDTO actorDTO : credits.getActors()) {
                    Actor actor = ActorConverter.dtoToEntity(actorDTO);
                    actor = actorDAO.upsert(actor); // undgå dubletter
                    movie.addActor(actor);
                }
            }
        }

        if (movieDTO.getGenreIds() != null) {
            for (Integer tmdbGenreId : movieDTO.getGenreIds()) {
                Genre g = genreByTmdbId.get(tmdbGenreId);
                if (g != null) {
                    movie.addGenre(g);
                }
            }
        }
        return movieDAO.merge(movie);
    }

    public Movie importSingleMovie(MovieDTO movieDTO, CastAndCrewDTO credits) {
        return importSingleMovie(movieDTO, credits, null);
    }
}
