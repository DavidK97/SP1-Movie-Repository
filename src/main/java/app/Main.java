package app;

import app.config.HibernateConfig;
import app.dtos.*;
import app.entities.Actor;
import app.entities.Director;
import app.entities.Genre;
import app.entities.Movie;
import app.persistence.GenreDAO;
import app.persistence.MovieDAO;
import app.services.GenreConverter;
import app.services.MovieService;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        MovieService movieService = new MovieService();
        MovieDAO movieDAO = new MovieDAO(emf);
        GenreDAO genreDAO = new GenreDAO(emf);



        /* TODO
            1. Få nedenstående ud af main
            2. Tjek for duplicates af især Actors og Directors og fix
         */


        // 1. Hent alle Movie
        List<MovieDTO> allMovieDTOs = new ArrayList<>();
        allMovieDTOs = movieService.getAllDanishMovies();

        // 2. Hent alle Genre
        List<GenreDTO> allGenresDTO = movieService.getAllGenres();

        // 3. Gem alle Genre
        List<Genre> allGenresEntity = new ArrayList<>();

        for (GenreDTO dto : allGenresDTO) {
            Genre genre = GenreConverter.dtoToEntity(dto);
            allGenresEntity.add(genre);
        }
        allGenresEntity.forEach(g -> genreDAO.create(g));


        //Relationer mellem Movie og Actor og Director sættes for hver Movie
        for (MovieDTO movieDTO : allMovieDTOs) {
            // 4.1 Hent castAndCrewDTO for hver film
            CastAndCrewDTO castAndCrewDTO = movieService.getCastAndCrew(movieDTO.getTmdbId());

            // 4.2 Hent List<Actor> til hver film
            List<ActorDTO> actorsDTO = castAndCrewDTO.getActors();

            // 4.3 Hent CrewDTO director til hver film
            Optional<CrewDTO> optionalDirector = movieService.getDirectorForMovie(castAndCrewDTO);


            //Map det hele til entities
            Movie movie = Movie.builder()
                    .tmdbId(movieDTO.getTmdbId())
                    .adult(movieDTO.isAdult())
                    .title(movieDTO.getTitle())
                    .originalLanguage(movieDTO.getOriginalLanguage())
                    .originalTitle(movieDTO.getOriginalTitle())
                    .releaseDate(movieDTO.getReleaseDate())
                    .title(movieDTO.getTitle())
                    .voteAverage(movieDTO.getVoteAverage())
                    .voteCount(movieDTO.getVoteCount())
                    .popularity(movieDTO.getPopularity())
                    .overview(movieDTO.getOverview())
                    .build();


            List<Genre> allGenresEntity2 = new ArrayList<>();
            for (GenreDTO dto : allGenresDTO) {
                Genre genre = GenreConverter.dtoToEntity(dto);
                allGenresEntity2.add(genre);
            }

            for (Integer genreTMDBId : movieDTO.getGenreIds()) {
                allGenresEntity2.stream()
                        .filter(g -> g.getTmdbId() == genreTMDBId)
                        .findFirst()
                        .ifPresent(movie::addGenre);
            }


            if (optionalDirector.isPresent()) {
                Director director = Director.builder()
                        .name(optionalDirector.get().getName())
                        .adult(optionalDirector.get().isAdult())
                        .gender(optionalDirector.get().getGender())
                        .tmdbId(optionalDirector.get().getTmdbId())
                        .knownForDepartment(optionalDirector.get().getKnownForDepartment())
                        .originalName(optionalDirector.get().getOriginalName())
                        .popularity(optionalDirector.get().getPopularity())
                        .department(optionalDirector.get().getDepartment())
                        .job(optionalDirector.get().getJob())
                        .build();

                director.addMovie(movie);
            }

            for (ActorDTO actorDTO : actorsDTO) {
                Actor actor = Actor.builder()
                        .name(actorDTO.getName())
                        .adult(actorDTO.isAdult())
                        .gender(actorDTO.getGender())
                        .tmdbId(actorDTO.getTmdbId())
                        .knownForDepartment(actorDTO.getKnownForDepartment())
                        .originalName(actorDTO.getOriginalName())
                        .popularity(actorDTO.getPopularity())
                        .build();
                movie.addActor(actor);
            }

            // 5. Gem alle film i DB. Cascade.Persist sørger for Director og Actor bliver gemt
            movieDAO.create(movie);
        }
        emf.close();
    }
}
