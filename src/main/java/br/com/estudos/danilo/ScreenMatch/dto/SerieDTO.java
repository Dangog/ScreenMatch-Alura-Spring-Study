package br.com.estudos.danilo.ScreenMatch.dto;

import br.com.estudos.danilo.ScreenMatch.model.Category;
import jakarta.persistence.*;

public record SerieDTO(Long id,
        String title,
        Integer totalSeasons,
        Double rating,
        Category genre,
        String actors,
        String posterURL,
        String plot) {
}
