package app.services;

import app.dtos.MovieDTO;
import app.dtos.MovieSearchResultDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class MovieService {
    private final String apiKey = System.getenv("API_KEY");

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
}

