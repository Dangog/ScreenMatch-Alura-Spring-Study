package br.com.estudos.danilo.ScreenMatch;

import br.com.estudos.danilo.ScreenMatch.model.DadosSerie;
import br.com.estudos.danilo.ScreenMatch.model.EpisodeData;
import br.com.estudos.danilo.ScreenMatch.service.ConsumoAPI;
import br.com.estudos.danilo.ScreenMatch.service.JacksonDataConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?i=tt3896198&apikey=1d559d70");

		JacksonDataConverter conv = new JacksonDataConverter();
		DadosSerie convertedData = conv.getData(json, br.com.estudos.danilo.ScreenMatch.model.DadosSerie.class);
		System.out.println(convertedData);

		json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=1d559d70");
		EpisodeData convertedEpisodeData = conv.getData(json, EpisodeData.class);
		System.out.println(convertedEpisodeData);


	}
}
