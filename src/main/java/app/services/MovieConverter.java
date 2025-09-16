package app.services;

import app.dtos.MovieDTO;
import app.entities.Movie;

public class MovieConverter {

    public static Movie dtoToEntity (MovieDTO movieDTO) {
        Movie movie = Movie.builder()
                .title(movieDTO.getTitle())
                .originalLanguage(movieDTO.getOriginalLanguage())
                .originalTitle(movieDTO.getOriginalTitle())
                .overview(movieDTO.getOverview())
                .popularity(movieDTO.getPopularity())
                .releaseDate(movieDTO.getReleaseDate())
                .tmdbId(movieDTO.getTmdbId())
                .voteCount(movieDTO.getVoteCount())
                .build();
        return movie;
    }
}
