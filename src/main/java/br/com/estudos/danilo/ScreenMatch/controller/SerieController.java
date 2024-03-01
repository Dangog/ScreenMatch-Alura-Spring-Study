package br.com.estudos.danilo.ScreenMatch.controller;

import br.com.estudos.danilo.ScreenMatch.dto.SerieDTO;
import br.com.estudos.danilo.ScreenMatch.model.Serie;
import br.com.estudos.danilo.ScreenMatch.repository.SerieRepository;
import org.aspectj.apache.bcel.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {

    @Autowired
    private SerieRepository repository;

    @GetMapping("/series")
    public List<SerieDTO> getSeries(){
        return repository.findAll()
                .stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitle(),s.getTotalSeasons(),s.getRating(),s.getGenre(),s.getActors(),s.getPosterURL(),s.getPlot()))
                .collect(Collectors.toList());
    }
}
