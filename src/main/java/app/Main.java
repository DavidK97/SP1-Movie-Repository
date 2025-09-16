package app;

import app.config.HibernateConfig;
import app.dtos.MovieDTO;
import app.dtos.MovieSearchResultDTO;
import app.persistence.MovieDAO;
import app.services.MovieService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        MovieService movieService = new MovieService();
        MovieDAO movieDAO = new MovieDAO(emf);


        List<MovieDTO> movies = new ArrayList<>();
        movies = movieService.getAllDanishMovies();

        //movies.forEach(System.out::println);

        // Alle film gemmes i DB
        movies.forEach(movie -> movieDAO.create(movie));


        emf.close();
    }
}