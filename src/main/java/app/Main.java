package app;

import app.config.HibernateConfig;
import app.dtos.*;
import app.entities.Actor;
import app.entities.Crew;
import app.entities.Movie;
import app.persistence.MovieDAO;
import app.services.MovieService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        MovieService movieService = new MovieService();
        MovieDAO movieDAO = new MovieDAO(emf);



        // 1. Hent alle Movie
        List<MovieDTO> allMovieDTOs = new ArrayList<>();
        allMovieDTOs = movieService.getAllDanishMovies();

        // 2. Hent alle Genre
        movieService.getAllGenres();

        // 3. Gem alle Genre
        //TODO Mangler DAO-metode


        for (MovieDTO movieDTO : allMovieDTOs) {
            // 4.1 Hent castAndCrewDTO for hver film
            CastAndCrewDTO castAndCrewDTO = movieService.getCastAndCrew(movieDTO.getTmdbId());

            // 4.2 Hent List<Actor> til hver film
            List<ActorDTO> actorsDTO = castAndCrewDTO.getActors();

            // 4.3 Hent CrewDTO director til hver film
            Optional<CrewDTO> optionalDirector = movieService.getDirectorForMovie(castAndCrewDTO);


            //Map det hele til entities
            Movie movie = Movie.builder()
                    .adult(movieDTO.isAdult())
                    .title(movieDTO.getTitle())
                    .originalLanguage(movieDTO.getOriginalLanguage())
                    .originalTitle(movieDTO.getOriginalTitle())
                    .releaseDate(movieDTO.getReleaseDate())
                    .title(movieDTO.getTitle())
                    .voteAverage(movieDTO.getVoteAverage())
                    .voteCount(movieDTO.getVoteCount())
                    .popularity(movieDTO.getPopularity())
                    .build();


            if (optionalDirector.isPresent()) {
                Crew director = Crew.builder()
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





        /*
        List<MovieDTO> movies = new ArrayList<>();
        // movies = movieService.getAllDanishMovies();

        //movies.forEach(System.out::println);

        // Alle film gemmes i DB
        //movies.forEach(movie -> movieDAO.create(movie));

        // Alle genre hentes
        //movieService.getAllGenres();

        //Hent alle credits for 1 movie
        CastAndCrewDTO c = movieService.getCastAndCrew(12);

        //Test af director
        Optional<CrewDTO> director = movieService.getDirectorForMovie(c);
        System.out.println("----------------- Director: -------------------" + director);

        //System.out.println(c);

         */


        emf.close();
    }
}