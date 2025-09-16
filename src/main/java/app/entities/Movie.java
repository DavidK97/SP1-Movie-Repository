package app.entities;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;

@Getter
@NoArgsConstructor //Til Hibernate
@AllArgsConstructor //Til Builder
@Builder
@ToString
@EqualsAndHashCode

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private boolean adult;

    private int tmdbId;

    private String originalLanguage;
    private String originalTitle;

    @Column(length = 1000)
    private String overview;
    private double popularity;
    private LocalDate releaseDate;
    private String title;
    private double voteAverage;
    private int voteCount;

/*
    //Relationer
    @Builder.Default
    @ManyToMany
    @Cascade(CascadeType.PERSIST)
    private HashSet<Actor> actors = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @Cascade(CascadeType.PERSIST)
    private HashSet<Genre> genres = new HashSet<>();

    @ManyToOne
    @Cascade(CascadeType.PERSIST)
    private Crew director;


 */


}
