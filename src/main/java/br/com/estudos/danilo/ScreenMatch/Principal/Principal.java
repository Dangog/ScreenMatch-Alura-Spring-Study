package br.com.estudos.danilo.ScreenMatch.Principal;

import br.com.estudos.danilo.ScreenMatch.model.*;
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

    private Optional<Serie> searchedSerie;

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
                    5 - Buscar série por ator envolvido
                    6 - Top 3 Series
                    7 - Buscar séries por categoria / gênero
                    8 - Top 3 Séries por Temporada e Avaliação
                    9 - Buscar episódios por trecho
                    10 - Top 3 episódios por série
                    11 - Buscar episódios por data
                                   
                    0 - Sair                                 
                    """;
            System.out.println(menu);

            resp = t.nextInt();

            switch (resp) {
                case 1 -> searchSerieByWeb();
                case 2 -> searchEpisodeBySerie();
                case 3 -> listSearchSeries();
                case 4 -> searchSerieByTitle();
                case 5 -> searchSerieByActor();
                case 6 -> orderTopFiveSeries();
                case 7 -> searchSerieByGenre();
                case 8 -> orderTopThreeSeriesPerSeasonsAndRating();
                case 9 -> searchEpisodesByPlot();
                case 10 -> top3EpisodesPerSerie();
                case 11 -> findEpisodesByDate();
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
        searchedSerie = serieRepository.findByTitleContainingIgnoreCase(serieName);

        if (searchedSerie.isPresent()){
            System.out.println("Dados da série: " + searchedSerie.get());
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void searchSerieByActor() {
        System.out.println("Digite o nome do ator envolvido para a busca: ");
        var actorName = s.nextLine();

        System.out.println("Digite a nota mínima a ser buscada: ");
        var minimalRating = t.nextDouble();

        Optional<Serie> searchedSerie = serieRepository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(actorName, minimalRating);

        if (searchedSerie.isPresent()){
            System.out.println("Dados da série: " + searchedSerie.get());
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void orderTopFiveSeries() {
        List<Serie> listedSeries = serieRepository.findTop3ByOrderByRatingDesc();

        listedSeries.forEach(s -> System.out.println(s.getTitle() + " avaliação: " + s.getRating()));
    }

    private void searchSerieByGenre() {
        System.out.println("Digite a categoria/gênero da série a ser buscada: ");
        var genreForSearch = s.nextLine();

        Category genre = Category.fromString(genreForSearch);
        List<Serie> searchSeries = serieRepository.findByGenre(genre);

        System.out.println("Séries da categoria: " + genreForSearch);
        searchSeries.forEach(System.out::println);

    }

    private void orderTopThreeSeriesPerSeasonsAndRating() {

        System.out.println("Digite a quantidade máxima de temporadas que a série deve ter");
        var totalSeasons = t.nextInt();

        System.out.println("Digite a nota mínima a ser buscada: ");
        var minimalRating = t.nextDouble();

        List<Serie> listedSeries = serieRepository.findTop3WithJPQL(totalSeasons, minimalRating);

        listedSeries.forEach(s -> System.out.println(s.getTitle() + " quantidade de temporadas: " + s.getTotalSeasons() + " avaliação: " + s.getRating()));
    }


    private void searchEpisodesByPlot() {
        System.out.println("Digite um trecho do episódio para a busca: ");
        var plotSearch = s.nextLine();

        List<Episode> episodes = serieRepository.findEpisodeByPlot(plotSearch);

        episodes.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitle(), e.getSeason(),
                        e.getEpisodeNumber(), e.getTitle()));
    }


    private void top3EpisodesPerSerie() {
        searchSerieByTitle();

        if(searchedSerie.isPresent()){
            Serie serie = searchedSerie.get();
            List<Episode> top3Episodes = serieRepository.findTop3EpisodesPerSerie(serie);
            top3Episodes.forEach(e -> System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                    e.getSerie().getTitle(), e.getSeason(),
                    e.getEpisodeNumber(), e.getTitle()));
        }
    }

    private void findEpisodesByDate() {
        searchSerieByTitle();

        System.out.println("Digite o ano para a busca: ");
        var minimumYearReleased = s.nextInt();

        if(searchedSerie.isPresent()){
            Serie serie = searchedSerie.get();
            List<Episode> episodesPerData = serieRepository.findEpisodesByDate(serie, minimumYearReleased);
            episodesPerData.forEach(e -> System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                    e.getSerie().getTitle(), e.getSeason(),
                    e.getEpisodeNumber(), e.getTitle()));
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
