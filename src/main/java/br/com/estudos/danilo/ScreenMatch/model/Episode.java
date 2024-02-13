package br.com.estudos.danilo.ScreenMatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodes")
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Integer season;
    private String title;
    private Integer episodeNumber;
    private Double avaliation;
    private LocalDate releasedDate;

    @ManyToOne
    private Serie serie;

    public Episode (){}

    public Episode(Integer seasonNumber, EpisodeData episodeData) {
        this.season = seasonNumber;
        this.title = episodeData.title();
        this.episodeNumber = episodeData.episodeNumber();

        try {
            this.avaliation = Double.valueOf(episodeData.avaliation());
        } catch (NumberFormatException ex) {
            this.avaliation = 0.0;
        }

        try {
            this.releasedDate = LocalDate.parse(episodeData.releasedDate());
        } catch (DateTimeParseException ex) {
            this.releasedDate = null;
        }
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public Double getAvaliation() {
        return avaliation;
    }

    public void setAvaliation(Double avaliation) {
        this.avaliation = avaliation;
    }

    public LocalDate getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(LocalDate releasedDate) {
        this.releasedDate = releasedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    @Override
    public String toString() {
        return  "season=" + season +
                ", title='" + title + '\'' +
                ", episodeNumber=" + episodeNumber +
                ", avaliation=" + avaliation +
                ", releasedDate=" + releasedDate;
    }
}




