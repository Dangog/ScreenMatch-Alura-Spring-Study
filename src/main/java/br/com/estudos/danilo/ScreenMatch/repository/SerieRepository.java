package br.com.estudos.danilo.ScreenMatch.repository;

import br.com.estudos.danilo.ScreenMatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SerieRepository extends JpaRepository <Serie, Long> {
    Optional<Serie>findByTitleContainingIgnoreCase(String serieName);
}
