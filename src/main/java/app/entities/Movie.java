package app.entities;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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


    //Relationer
    @Setter
    @Builder.Default
    @Cascade(CascadeType.PERSIST)
    @ManyToMany
    private Set<Actor> actors = new HashSet<>();

    @Setter
    @Builder.Default
    @ManyToMany
    @Cascade(CascadeType.PERSIST)
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
