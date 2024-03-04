package br.com.estudos.danilo.ScreenMatch.controller;

import br.com.estudos.danilo.ScreenMatch.dto.EpisodeDTO;
import br.com.estudos.danilo.ScreenMatch.dto.SerieDTO;
import br.com.estudos.danilo.ScreenMatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping
    public List<SerieDTO> getSeries(){
        return service.searchSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> findTop3ByOrderByRatingDesc(){
        return service.searchTop3Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> searchNewReleases(){
        return service.searchNewReleases();
    }

    @GetMapping("/{id}")
    public SerieDTO getById(@PathVariable Long id){
        return service.searchSerieById(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodeDTO> getAllSeasons(@PathVariable Long id){
        return service.getAllSeasons(id);
    }

    @GetMapping("/{id}/temporadas/{tempID}")
    public List<EpisodeDTO> getEpisodesPerSeason(@PathVariable Long id, @PathVariable Long tempID){
        return service.getSeasonsByTemp(id, tempID);
    }

    @GetMapping("/categoria/{categoria}")
    public List<SerieDTO> getSerieByCategory(@PathVariable String categoria){
        return service.getSerieByCategory(categoria);
    }

    @GetMapping("/{id}/temporadas/top")
    public List<EpisodeDTO> getEpisodesPerSeason(@PathVariable Long id){
        return service.getTop5EpisodesPerSeason(id);
    }
}
