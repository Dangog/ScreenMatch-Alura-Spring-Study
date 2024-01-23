package br.com.estudos.danilo.ScreenMatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer Rated,
                         @JsonAlias("imdbRating") String avaliacao) {
}
