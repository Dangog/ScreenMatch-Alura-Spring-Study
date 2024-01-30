package br.com.estudos.danilo.ScreenMatch.Principal;

import br.com.estudos.danilo.ScreenMatch.model.DadosSerie;
import br.com.estudos.danilo.ScreenMatch.model.Episode;
import br.com.estudos.danilo.ScreenMatch.model.EpisodeData;
import br.com.estudos.danilo.ScreenMatch.model.SeasonData;
import br.com.estudos.danilo.ScreenMatch.service.ConsumoAPI;
import br.com.estudos.danilo.ScreenMatch.service.JacksonDataConverter;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    Scanner t = new Scanner(System.in);

    private final String URL = "https://www.omdbapi.com/?t=";
    private final String APIKey = "&apikey=1d559d70";
    private ConsumoAPI principalConsume = new ConsumoAPI();
    private JacksonDataConverter conv = new JacksonDataConverter();

    public void showMenu(){
        System.out.println("\nDigite a respectiva série para que faça a busca: ");
        var resp = t.nextLine();

        var json = principalConsume.obterDados(URL + resp.replace(" ", "+") + APIKey);

        DadosSerie convertedData = conv.getData(json, br.com.estudos.danilo.ScreenMatch.model.DadosSerie.class);
        System.out.println(convertedData);

        List<SeasonData> seasonsList = new ArrayList<>();

		for (int i = 1; i<=convertedData.totalTemporadas(); i++){
			json = principalConsume.obterDados(URL + resp.replace(" ", "+")+ "&season=" + i + APIKey);
			SeasonData convertedSeasonData = conv.getData(json, SeasonData.class);
			seasonsList.add(convertedSeasonData);
		}

        List<EpisodeData> allEpisodesData =  seasonsList.stream()
                        .flatMap(s -> s.episodes().stream())
                .collect(Collectors.toList());

//        allEpisodesData.stream()
//                .filter(e -> !e.avaliation().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primeiro filtro " + e))
//                .sorted(Comparator.comparing(EpisodeData::avaliation).reversed())
//                .peek(e -> System.out.println("Ordenação " + e))
//                .limit(5)
//                .peek(e -> System.out.println("Processo de limitação " + e))
//                .map(e -> e.title().toUpperCase())
//                .peek(e -> System.out.println("Deixando em maiúsculo com  mapeamento " + e))
//                .forEach(System.out::println);

        List<Episode> episodes =  seasonsList.stream()
                .flatMap(s -> s.episodes().stream()
                    .map(d -> new Episode(d.episodeNumber(), d))
                ).collect(Collectors.toList());

        episodes.forEach(System.out::println);




        System.out.println("Digite o usuário que deseja ser buscado: ");
        var episodeSearch = t.nextLine();

        Optional<Episode> searchedEpisode = episodes.stream()
                .filter(e -> e.getTitle().contains(episodeSearch))
                .peek(e -> System.out.println("O episódio com base no titulo é: " + e))
                .findFirst();

        if (searchedEpisode.isPresent()){
            System.out.println("Episódio encontrado");
            System.out.println("Temporada: " + searchedEpisode.get().getSeason());
        } else {
            System.out.println("O episódio " + searchedEpisode + " não foi encontrado");
        }



//        System.out.println("A partir de que ano você deseja ver o episódio?: ");
//        var year = t.nextInt();
//        t.nextLine();
//
//        LocalDate searchDate = LocalDate.of(year, 1 ,1);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodes.stream()
//                .filter(e -> e.getReleasedDate() !=  null && e.getReleasedDate().isAfter(searchDate))
//                .forEach(e -> System.out.println("Temporada: " + e.getSeason()
//                + " Episódio: " + e.getEpisodeNumber()
//                + " Data de Lançamento: " + formatter.format(e.getReleasedDate())));


    }
}
