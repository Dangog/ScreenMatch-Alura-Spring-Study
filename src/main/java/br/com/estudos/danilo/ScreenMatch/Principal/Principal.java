package br.com.estudos.danilo.ScreenMatch.Principal;

import br.com.estudos.danilo.ScreenMatch.model.DadosSerie;
import br.com.estudos.danilo.ScreenMatch.model.Serie;
import br.com.estudos.danilo.ScreenMatch.model.Episode;
import br.com.estudos.danilo.ScreenMatch.model.EpisodeData;
import br.com.estudos.danilo.ScreenMatch.model.SeasonData;
import br.com.estudos.danilo.ScreenMatch.repository.SerieRepository;
import br.com.estudos.danilo.ScreenMatch.service.ConsumoAPI;
import br.com.estudos.danilo.ScreenMatch.service.JacksonDataConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private SerieRepository serieRepository;

    Scanner t = new Scanner(System.in);
    Scanner s = new Scanner(System.in);
    private final String URL = "https://www.omdbapi.com/?t=";
    private final String APIKey = "&apikey=1d559d70";
    private ConsumoAPI principalConsume = new ConsumoAPI();
    private JacksonDataConverter conv = new JacksonDataConverter();

    private List<DadosSerie> seriesData = new ArrayList<>();

    public Principal(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    private List <Serie> series = new ArrayList<>();

    public void showMenu() {
       var resp = -1;

        while (resp != 0) {

            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por título
                                    
                    0 - Sair                                 
                    """;
            System.out.println(menu);

            resp = t.nextInt();

            switch (resp) {
                case 1 -> searchSerieByWeb();
                case 2 -> searchEpisodeBySerie();
                case 3 -> listSearchSeries();
                case 4 -> searchSerieByTitle();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void searchSerieByWeb() {
        DadosSerie data = getSerieData();
        //seriesData.add(data);
        Serie serie = new Serie(data);
        serieRepository.save(serie);
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
        listSearchSeries();

        System.out.println("Digite o nome da série: ");
        var serieName = s.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitle().toLowerCase().contains(serieName.toLowerCase()))
                .findFirst();

        if (serie.isPresent()){

            var foundSerie = serie.get();
            List<SeasonData> seasons = new ArrayList<>();

            for (int i = 1; i <= foundSerie.getTotalSeasons(); i++) {
                var json = principalConsume.obterDados(URL + foundSerie.getTitle().replace(" ", "+") + "&season=" + i + APIKey);
                SeasonData seasonData = conv.getData(json, SeasonData.class);
                seasons.add(seasonData);
            }

            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(s -> s.episodes().stream()
                            .map(e -> new Episode(s.respectiveSeason(), e)))
                    .collect(Collectors.toList());

            foundSerie.setEpisodes(episodes);

            serieRepository.save(foundSerie);

        } else {
            System.out.println("Série não encontrada");
        }


    }

    private void listSearchSeries(){
        series = serieRepository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }

    private void searchSerieByTitle() {
        System.out.println("Digite um título para a série: ");
        var serieName = s.nextLine();
        Optional<Serie> searchedSerie = serieRepository.findByTitleContainingIgnoreCase(serieName);

        if (searchedSerie.isPresent()){
            System.out.println("Dados da série: " + searchedSerie.get());
        } else {
            System.out.println("Série não encontrada");
        }
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
