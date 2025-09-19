package app.populators;

import app.entities.Actor;
import app.entities.Director;
import app.entities.Genre;
import app.entities.Movie;
import app.persistence.ActorDAO;
import app.persistence.DirectorDAO;
import app.persistence.GenreDAO;
import app.persistence.MovieDAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MoviePopulator {

    public static List<Movie> populateMovies(MovieDAO movieDAO, GenreDAO genreDAO, ActorDAO actorDAO, DirectorDAO directorDAO) {
        Genre g1 = Genre.builder()
                .tmdbId(111)
                .name("Action")
                .build();

        Genre g2 = Genre.builder()
                .tmdbId(222)
                .name("Drama")
                .build();

        Genre g3 = Genre.builder()
                .tmdbId(333)
                .name("Horror")
                .build();

        genreDAO.create(g1);
        genreDAO.create(g2);
        genreDAO.create(g3);

        Actor a1 = Actor.builder()
                .adult(false)
                .gender(2)
                .tmdbId(101)
                .knownForDepartment("Acting")
                .name("Chris Hemsworth")
                .originalName("Chris Hemsworth")
                .popularity(85.3)
                .build();

        Actor a2 = Actor.builder()
                .adult(false)
                .gender(1)
                .tmdbId(102)
                .knownForDepartment("Acting")
                .name("Scarlett Johansson")
                .originalName("Scarlett Johansson")
                .popularity(92.1)
                .build();

        Actor a3 = Actor.builder()
                .adult(false)
                .gender(2)
                .tmdbId(103)
                .knownForDepartment("Acting")
                .name("Tom Holland")
                .originalName("Tom Holland")
                .popularity(78.5)
                .build();

        Director d1 = Director.builder()
                .adult(false)
                .gender(2)
                .tmdbId(201)
                .knownForDepartment("Directing")
                .name("Christopher Nolan")
                .originalName("Christopher Nolan")
                .popularity(95.0)
                .department("Directing")
                .job("Director")
                .build();

        Director d2 = Director.builder()
                .adult(false)
                .gender(2)
                .tmdbId(202)
                .knownForDepartment("Directing")
                .name("Steven Spielberg")
                .originalName("Steven Spielberg")
                .popularity(98.2)
                .department("Directing")
                .job("Director")
                .build();

        Director d3 = Director.builder()
                .adult(false)
                .gender(2)
                .tmdbId(203)
                .knownForDepartment("Directing")
                .name("Martin Scorsese")
                .originalName("Martin Scorsese")
                .popularity(90.5)
                .department("Directing")
                .job("Director")
                .build();

        Movie m1 = Movie.builder()
                .adult(false)
                .tmdbId(1001)
                .originalLanguage("en")
                .originalTitle("The First Adventure")
                .overview("An epic journey of discovery.")
                .popularity(75.5)
                .releaseDate(LocalDate.of(2020, 5, 15))
                .title("The First Adventure")
                .voteAverage(7.8)
                .voteCount(1200)
                .build();

        Movie m2 = Movie.builder()
                .adult(false)
                .tmdbId(1002)
                .originalLanguage("da")
                .originalTitle("Den Store Flugt")
                .overview("En spændende flugt gennem København.")
                .popularity(55.2)
                .releaseDate(LocalDate.of(2021, 8, 23))
                .title("Den Store Flugt")
                .voteAverage(6.9)
                .voteCount(850)
                .build();

        Movie m3 = Movie.builder()
                .adult(true)
                .tmdbId(1003)
                .originalLanguage("fr")
                .originalTitle("La Nuit Rouge")
                .overview("En mørk og dramatisk fortælling fra Paris.")
                .popularity(95.1)
                .releaseDate(LocalDate.of(2022, 2, 10))
                .title("La Nuit Rouge")
                .voteAverage(8.2)
                .voteCount(2150)
                .build();


        // Gem Directors og Actors
        actorDAO.create(a1);
        actorDAO.create(a2);
        actorDAO.create(a3);

        directorDAO.create(d1);
        directorDAO.create(d2);
        directorDAO.create(d3);


        //Tilføj genre til film
        m1.addGenre(g1);
        m1.addGenre(g2);
        m1.addGenre(g3);

        m2.addGenre(g2);

        m3.addGenre(g1);
        m3.addGenre(g3);

        //Tilføj Actor til film
        m1.addActor(a1);
        m1.addActor(a2);
        m1.addActor(a3);

        m2.addActor(a2);

        m3.addActor(a3);

        //Tilføj Director til film
        m1.setDirector(d1);
        m2.setDirector(d2);
        m3.setDirector(d3);

        movieDAO.merge(m1);
        movieDAO.merge(m2);
        movieDAO.merge(m3);


        List<Movie> movies = new ArrayList<>();
        movies.add(m1);
        movies.add(m2);
        movies.add(m3);

        return movies;
    }
}
