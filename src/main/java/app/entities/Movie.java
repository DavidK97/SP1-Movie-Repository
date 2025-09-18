package app.entities;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor //Til Hibernate
@AllArgsConstructor //Til Builder
@Builder
@ToString
@EqualsAndHashCode
@Setter

@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private boolean adult;

    @Column(name = "tmdb_id")
    private int tmdbId;

    @Column(name = "original_language")
    private String originalLanguage;

    @Column(name = "original_title")
    private String originalTitle;

    @Column(length = 1000)
    private String overview;

    private double popularity;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private String title;

    @Column(name = "vote_average")
    private double voteAverage;

    @Column(name = "vote_count")
    private int voteCount;


    //Relationer
    @Setter
    @Builder.Default
    @Cascade(CascadeType.PERSIST)
    @ManyToMany
    private Set<Actor> actors = new HashSet<>();

    @Setter
    @Builder.Default
    @ManyToMany
    private Set<Genre> genres = new HashSet<>();

    @Setter
    @ManyToOne
    @Cascade(CascadeType.PERSIST)

    private Director director;

    public void addActor (Actor actor) {
        this.actors.add(actor);
        if (actor != null) {
            actor.getMovies().add(this);
        }
    }

    public void addGenre (Genre genre) {
        this.genres.add(genre);
    }

}
