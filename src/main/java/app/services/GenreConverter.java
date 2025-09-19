package app.services;

import app.dtos.GenreDTO;
import app.entities.Genre;

public class GenreConverter {

    public static Genre dtoToEntity (GenreDTO genreDTO) {
        Genre genre = Genre.builder()
                .name(genreDTO.getName())
                .tmdbId(genreDTO.getTmdbId())
                .build();
        return genre;
    }

}
