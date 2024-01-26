package br.com.estudos.danilo.ScreenMatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeasonData(@JsonAlias ("Season") Integer respectiveSeason,
                         @JsonAlias("Episodes")List<EpisodeData> episodes) {
}
