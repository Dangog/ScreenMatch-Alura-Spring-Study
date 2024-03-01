package br.com.estudos.danilo.ScreenMatch;

import br.com.estudos.danilo.ScreenMatch.Principal.Principal;
import br.com.estudos.danilo.ScreenMatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

}
