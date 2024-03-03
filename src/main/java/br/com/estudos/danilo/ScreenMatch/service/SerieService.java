package br.com.estudos.danilo.ScreenMatch.service;

import br.com.estudos.danilo.ScreenMatch.dto.EpisodeDTO;
import br.com.estudos.danilo.ScreenMatch.dto.SerieDTO;
import br.com.estudos.danilo.ScreenMatch.model.Serie;
import br.com.estudos.danilo.ScreenMatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> searchSeries(){
         return dtoSerieConverter(repository.findAll());
    }

    public List<SerieDTO> searchTop3Series(){
        return dtoSerieConverter(repository.findTop5ByOrderByRatingDesc());
    }

    public List<SerieDTO> dtoSerieConverter(List<Serie> serie){
        return  serie.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitle(),s.getTotalSeasons(),s.getRating(),s.getGenre(),s.getActors(),s.getPosterURL(),s.getPlot()))
                .collect(Collectors.toList());

    }

    public List<SerieDTO> searchNewReleases(){
        return dtoSerieConverter(repository.findMostRecentEpisode());
    }

    public SerieDTO searchSerieById(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitle(),s.getTotalSeasons(),s.getRating(),s.getGenre(),s.getActors(),s.getPosterURL(),s.getPlot());
        }
        return null;
    }

    public List<EpisodeDTO> getAllSeasons(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();
            return s.getEpisodes().stream()
                    .map(e -> new EpisodeDTO(e.getSeason(),e.getTitle(),e.getEpisodeNumber()))
                    .collect(Collectors.toList());
        }     return null;

    }
}
