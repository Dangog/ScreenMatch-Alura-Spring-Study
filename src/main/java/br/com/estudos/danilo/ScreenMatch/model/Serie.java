package br.com.estudos.danilo.ScreenMatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import java.util.*;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "totalSeasons")
    private Integer totalSeasons;

    @Column(name = "rating")
    private Double rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Category genre;

    @Column(name = "actors")
    private String actors;

    @Column(name = "posterURL")
    private String posterURL;

    @Column(name = "plot")
    private String plot;

    @OneToMany(mappedBy = "serie")
    private List<Episode> episodes = new ArrayList<>();

    public Serie(){};

    public Serie(DadosSerie dadosSerie) {
        this.title = dadosSerie.titulo();
        this.totalSeasons = dadosSerie.totalTemporadas();
        this.rating = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0.0);
        this.genre = Category.fromPortugues(dadosSerie.genero().split(",")[0].trim());
        this.actors = dadosSerie.atores();
        this.posterURL = dadosSerie.poster();
        this.plot = dadosSerie.sinopse();
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", title='" + title + '\'' +
                ", totalSeasons=" + totalSeasons +
                ", rating=" + rating +
                ", genre=" + genre +
                ", actors='" + actors + '\'' +
                ", posterURL='" + posterURL + '\'' +
                ", plot='" + plot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(Integer totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Category getGenre() {
        return genre;
    }

    public void setGenre(Category genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
}
