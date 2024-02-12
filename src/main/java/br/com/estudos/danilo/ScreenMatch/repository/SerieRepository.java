package br.com.estudos.danilo.ScreenMatch.repository;

import br.com.estudos.danilo.ScreenMatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository <Serie, Long> {
}
