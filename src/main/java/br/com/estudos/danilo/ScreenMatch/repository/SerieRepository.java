package br.com.estudos.danilo.ScreenMatch.repository;

import br.com.estudos.danilo.ScreenMatch.model.Category;
import br.com.estudos.danilo.ScreenMatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository <Serie, Long> {
    Optional<Serie>findByTitleContainingIgnoreCase(String serieName);
    Optional<Serie>findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(String actorName, double rating);
    List<Serie> findTop3ByOrderByRatingDesc();
    List<Serie> findByGenre(Category category);
    List<Serie> findTop3ByTotalSeasonsLessThanEqualAndRatingGreaterThanEqual(int numberOfSeasons, double rating);

}
