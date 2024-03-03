package br.com.estudos.danilo.ScreenMatch.repository;

import br.com.estudos.danilo.ScreenMatch.model.Category;
import br.com.estudos.danilo.ScreenMatch.model.Episode;
import br.com.estudos.danilo.ScreenMatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository <Serie, Long> {
    Optional<Serie>findByTitleContainingIgnoreCase(String serieName);
    Optional<Serie>findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(String actorName, double rating);
    List<Serie> findTop3ByOrderByRatingDesc();
    List<Serie> findByGenre(Category category);
    List<Serie> findTop3ByTotalSeasonsLessThanEqualAndRatingGreaterThanEqual(int numberOfSeasons, double rating);

    @Query(value = "select s from Serie s WHERE s.totalSeasons <= :maximumSeasons AND s.rating >= :minimumRating")
    List<Serie> findTop3WithJPQL(int maximumSeasons, double minimumRating);

    @Query(value ="SELECT e FROM Serie s JOIN s.episodes e WHERE e.title ILIKE %:plotSearch")
    List<Episode> findEpisodeByPlot(String plotSearch);

    @Query(value ="SELECT e FROM Serie s JOIN s.episodes e WHERE s = :serie ORDER BY e.avaliation LIMIT 3")
    List<Episode> findTop3EpisodesPerSerie(Serie serie);

    @Query(value ="SELECT e FROM Serie s JOIN s.episodes e WHERE s = :serie AND YEAR(e.releasedDate) <= :minimumYearReleased")
    List<Episode> findEpisodesByDate(Serie serie, int minimumYearReleased);

    List<Serie> findTop5ByOrderByRatingDesc();

    @Query("SELECT s FROM Serie s " +
            "JOIN s.episodes e " +
            "GROUP BY s " +
            "ORDER BY MAX(e.releasedDate) DESC LIMIT 5")
    List<Serie> findMostRecentEpisode();

}
