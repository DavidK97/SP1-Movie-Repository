package app.entities;

import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.CascadeType;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor // Til Hibernate
@AllArgsConstructor // Til Builder
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

    // ✅ gør film unik på TMDb-id
    @Column(name = "tmdb_id", unique = true, nullable = false)

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

    // Relationer
    @Builder.Default

    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    @ToString.Exclude @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Actor> actors = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Set<Genre> genres = new HashSet<>();

    @JoinColumn(name = "director_id")
    @Setter
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Director director;

    public void addActor(Actor actor) {
        this.actors.add(actor);
        if (actor != null) {
            actor.getMovies().add(this);
        }
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }
}
