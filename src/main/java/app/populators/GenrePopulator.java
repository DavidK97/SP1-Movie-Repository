package app.populators;

import app.entities.Genre;
import app.entities.Movie;
import app.persistence.GenreDAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GenrePopulator {

    public static List<Genre> populateGenres(GenreDAO genreDAO) {

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

        List<Genre> genres = new ArrayList<>();
        genres.add(g1);
        genres.add(g2);
        genres.add(g3);

        return genres;
    }
}
