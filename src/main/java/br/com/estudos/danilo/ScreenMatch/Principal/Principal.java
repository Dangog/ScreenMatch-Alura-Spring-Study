package br.com.estudos.danilo.ScreenMatch.Principal;

import br.com.estudos.danilo.ScreenMatch.model.DadosSerie;
import br.com.estudos.danilo.ScreenMatch.model.Serie;
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
    Scanner s = new Scanner(System.in);
    private final String URL = "https://www.omdbapi.com/?t=";
    private final String APIKey = "&apikey=1d559d70";
    private ConsumoAPI principalConsume = new ConsumoAPI();
    private JacksonDataConverter conv = new JacksonDataConverter();

    private List<DadosSerie> seriesData = new ArrayList<>();

    public void showMenu() {
       var resp = -1;

        while (resp != 0) {

            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                                    
                    0 - Sair                                 
                    """;
            System.out.println(menu);

            resp = t.nextInt();

            switch (resp) {
                case 1 -> searchSerieByWeb();
                case 2 -> searchEpisodeBySerie();
                case 3 -> listSearchSeries();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void searchSerieByWeb() {
        DadosSerie data = getSerieData();
        seriesData.add(data);
        System.out.println(data);
    }

    private DadosSerie getSerieData() {
        System.out.println("Digite o nome da série para busca");
        var serieName = s.nextLine();
        var json = principalConsume.obterDados(URL + serieName.replace(" ", "+") + APIKey);
        DadosSerie data = conv.getData(json, DadosSerie.class);
        return data;
    }

    private void searchEpisodeBySerie(){
        DadosSerie serieData = getSerieData();
        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i <= serieData.totalTemporadas(); i++) {
            var json = principalConsume.obterDados(URL + serieData.titulo().replace(" ", "+") + "&season=" + i + APIKey);
            SeasonData seasonData = conv.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        seasons.forEach(System.out::println);
    }

    private void listSearchSeries(){
        List <Serie> series = new ArrayList<>();
        series = seriesData.stream()
                        .map(d -> new Serie(d))
                                .collect(Collectors.toList());
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}


//        var json = principalConsume.obterDados(URL + resp.replace(" ", "+") + APIKey);
//
//        DadosSerie convertedData = conv.getData(json, br.com.estudos.danilo.ScreenMatch.model.DadosSerie.class);
//        System.out.println(convertedData);
//
//        List<SeasonData> seasonsList = new ArrayList<>();
//
//		for (int i = 1; i<=convertedData.totalTemporadas(); i++){
//			json = principalConsume.obterDados(URL + resp.replace(" ", "+")+ "&season=" + i + APIKey);
//			SeasonData convertedSeasonData = conv.getData(json, SeasonData.class);
//			seasonsList.add(convertedSeasonData);
//		}
//
//        List<EpisodeData> allEpisodesData =  seasonsList.stream()
//                        .flatMap(s -> s.episodes().stream())
//                .collect(Collectors.toList());
//
//
//        List<Episode> episodes =  seasonsList.stream()
//                .flatMap(s -> s.episodes().stream()
//                    .map(d -> new Episode(s.respectiveSeason(), d))
//                ).collect(Collectors.toList());
//
//        episodes.forEach(System.out::println);
//
//        Map<Integer, Double> perSeasonRating = episodes.stream()
//                .filter(sR -> sR.getAvaliation() > 0.0)
//                .collect(Collectors.groupingBy(Episode::getSeason,
//                        Collectors.averagingDouble(Episode::getAvaliation)));
//
//        System.out.println(perSeasonRating);
//
//        DoubleSummaryStatistics dse = episodes.stream()
//                .filter(e -> e.getAvaliation() > 0.0)
//                .collect(Collectors.summarizingDouble(Episode::getAvaliation));
//        System.out.println("Média: " + dse.getAverage());
//        System.out.println("Episódio mais bem avaliado: " + dse.getMax());
//        System.out.println("Episódio mais mal avaliado: " + dse.getMin());
//        System.out.println("Contagem de episódios: " + dse.getCount());
