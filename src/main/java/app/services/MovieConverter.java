package app.services;

import app.dtos.MovieDTO;
import app.entities.Movie;

public class MovieConverter {

    public static Movie dtoToEntity (MovieDTO movieDTO) {
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
        return movie;
    }
}
