package br.com.unipix.api.NumeroService;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.ibm.icu.util.TimeZone;

import br.com.unipix.api.NumeroService.repository.CampanhaRepository;

@SpringBootApplication
@EnableScheduling
public class NumeroServiceApplication  {

	@Autowired
	CampanhaRepository campanhaRepository;

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(NumeroServiceApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);
	}
}
