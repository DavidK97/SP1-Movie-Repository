package app.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor //Til Hibernate
@AllArgsConstructor //Til Builder
@Builder
@ToString
@EqualsAndHashCode

@Entity
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private boolean adult;
    private int gender;
    private int tmdbId;
    private String knownForDepartment;
    private String name;
    private String originalName;
    private double popularity;
    private String department;
    private String job;


    //Relationer
    @Builder.Default //Sørger for at hashSet bliver initialiseret
    @ToString.Exclude //Undgår stackOverFlow-Error
    @EqualsAndHashCode.Exclude //Undgår stackOverFlow-Error
    @OneToMany(mappedBy = "director") //Peger på director i Movie-klasse
    @Setter
    private Set<Movie> movies = new HashSet<>();

    //Hjælpemetode
    public void addMovie (Movie movie) {
        this.movies.add(movie);
        if(movie != null) {
            movie.setDirector(this);
        }
    }
}
