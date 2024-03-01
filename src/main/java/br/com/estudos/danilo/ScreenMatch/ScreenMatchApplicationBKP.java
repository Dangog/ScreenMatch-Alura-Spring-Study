package br.com.estudos.danilo.ScreenMatch;

import br.com.estudos.danilo.ScreenMatch.Principal.Principal;
import br.com.estudos.danilo.ScreenMatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMatchApplicationBKP implements CommandLineRunner {

	@Autowired
	private SerieRepository serieRepository;

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplicationBKP.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal(serieRepository);
		principal.showMenu();

	}


}
