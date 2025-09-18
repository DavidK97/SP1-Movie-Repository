package app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor //Til Hibernate
@AllArgsConstructor //Til Builder
@Builder
@ToString
@EqualsAndHashCode

@Entity
@Table(name = "actor")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private boolean adult;
    private int gender;

    @Column(name = "tmdb_id")
    private int tmdbId;

    @Column(name = "known_for_department")
    private String knownForDepartment;

    private String name;

    @Column(name = "original_name")
    private String originalName;

    private double popularity;


    //Relationer
    @Builder.Default //Sørger for at hashSet bliver initialiseret
    @ToString.Exclude //Undgår stackOverFlow-Error
    @EqualsAndHashCode.Exclude //Undgår stackOverFlow-Error
    @ManyToMany(mappedBy = "actors") //Peger på Actor i Movie-klasse
            Set<Movie> movies = new HashSet<>();


    //Hjælpemetode
    public void addMovie(Movie movie) {
        this.movies.add(movie);
    }
}
