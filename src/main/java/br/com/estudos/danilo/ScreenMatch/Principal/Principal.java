package br.com.estudos.danilo.ScreenMatch.Principal;

import br.com.estudos.danilo.ScreenMatch.model.DadosSerie;
import br.com.estudos.danilo.ScreenMatch.model.SeasonData;
import br.com.estudos.danilo.ScreenMatch.service.ConsumoAPI;
import br.com.estudos.danilo.ScreenMatch.service.JacksonDataConverter;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    Scanner t = new Scanner(System.in);

    private final String URL = "https://www.omdbapi.com/?t=";
    private final String APIKey = "&apikey=1d559d70";
    private ConsumoAPI principalConsume = new ConsumoAPI();
    private JacksonDataConverter conv = new JacksonDataConverter();

    public void showMenu(){
        System.out.println("Digite a respectiva série para que faça a busca");
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
		seasonsList.forEach(System.out::println);


    }
}
