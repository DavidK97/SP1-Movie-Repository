package app.services;

import app.dtos.*;
import app.entities.Movie;
import app.persistence.MovieDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MovieService {
    private final String apiKey = System.getenv("API_KEY");
    private MovieDAO movieDAO;


    public List<MovieDTO> getAllDanishMovies() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); //Gør at Jackson kan konvertere til LocalDate

        List<MovieDTO> movies = new ArrayList<>();
        List<MovieSearchResultDTO> searchResults = null;
        MovieSearchResultDTO movieSearchResultDTO = null;
        int page = 1;

        try {
            // Create an HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Create a request with builder-pattern
            do {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://api.themoviedb.org/3/discover/movie" +
                                "?api_key=" + apiKey +
                                "&include_adult=false" +
                                "&include_video=false" +
                                "&language=en-US&page=" + page +
                                "&primary_release_date.gte=16-09-2020" +
                                "&release_date.gte=16-09-2020" +
                                "&sort_by=primary_release_date.asc" +
                                "&with_origin_country=DK"))
                        .GET()
                        .build();

                // Send the request and get the response
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Check the status code and print the response
                if (response.statusCode() == 200) {
                    String json = response.body();
                    movieSearchResultDTO = objectMapper.readValue(json, MovieSearchResultDTO.class);

                    movies.addAll(movieSearchResultDTO.getResults());
                    // System.out.println(response.body());

                    page += 1;
                } else {
                    System.out.println("GET request failed. Status code: " + response.statusCode());
                }
            } while (page <= movieSearchResultDTO.getTotalPages());
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    public List<GenreDTO> getAllGenres() {
        ObjectMapper objectMapper = new ObjectMapper();

        List<GenreDTO> allGenres = null;
        try {
            // Create an HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Create a request with builder-pattern
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.themoviedb.org/3/genre/movie/list" +
                            "?api_key=" + apiKey +
                            "&language=dk"))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            // Check the status code and print the response
            if (response.statusCode() == 200) {
                String json = response.body();
                GenreSearchResultDTO genreSearchResultDTO = objectMapper.readValue(json, GenreSearchResultDTO.class);
                allGenres = genreSearchResultDTO.getGenres();

                System.out.println(response.body());
            } else {
                System.out.println("GET request failed. Status code: " + response.statusCode());
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return allGenres;
    }


    public CastAndCrewDTO getCastAndCrew(int movieId) {
        ObjectMapper objectMapper = new ObjectMapper();

        CastAndCrewDTO castAndCrewDTO = null;

        try {
            // Create an HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Create a request with builder-pattern
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.themoviedb.org/3/movie/" + movieId + "/credits?" +
                            "api_key=" + apiKey +
                            "&language=en-US"))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the status code and print the response
            if (response.statusCode() == 200) {
                String json = response.body();

                castAndCrewDTO = objectMapper.readValue(json, CastAndCrewDTO.class);
                //System.out.println(response.body());
            } else {
                System.out.println("GET request failed. Status code: " + response.statusCode());
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return castAndCrewDTO;
    }


    public Movie getMovieById(int id) {
        try {
            Movie movie = movieDAO.findById(id);
            if (movie == null) {
                System.out.println("Movie med id: " + id + "findes ikke i databasen");
            }
            return movie;
        } catch (RuntimeException e) {
            System.out.println("Noget gik galt ved hentning af film: " + e.getMessage());
            return null;
        }
    }



    public Optional<DirectorDTO> getDirectorForMovie (CastAndCrewDTO castAndCrewDTO) {
        Optional<DirectorDTO> crewDTO = castAndCrewDTO.getCrew().stream()
                .filter(movie -> movie.getJob().equalsIgnoreCase("Director"))
                .findAny();

        return crewDTO;
    }

    public double getAverageForAllMovies() {
        return getAllDanishMovies().stream()
                .filter(m -> m.getVoteCount() > 0)
                .mapToDouble(MovieDTO::getVoteAverage)
                .average()
                .orElse(0.0);
    }

    // boolean true = top 10, false = bottom 10
    public List<MovieDTO> getTopTenMovies(boolean topTen) {
        Comparator<MovieDTO> comparator = Comparator.comparingDouble(MovieDTO::getVoteAverage);

        if (topTen) {
            comparator = comparator.reversed();
        }

        return getAllDanishMovies().stream()
                .filter(m -> m.getVoteAverage() > 0 && m.getVoteCount() > 0)
                .sorted(comparator)
                .limit(10)
                .toList();
    }

    public List<MovieDTO> getMostPopularMovies(){
        Comparator<MovieDTO> comparator = Comparator.comparingDouble(MovieDTO::getPopularity);

        return getAllDanishMovies().stream()
                .filter(m -> m.getPopularity() > 0)
                //.mapToDouble(MovieDTO::getPopularity)
                .sorted(comparator.reversed())
                .limit(10)
                .toList();
    }

}

