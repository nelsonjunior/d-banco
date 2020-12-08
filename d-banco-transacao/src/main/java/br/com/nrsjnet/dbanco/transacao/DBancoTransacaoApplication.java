package br.com.nrsjnet.dbanco.transacao;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DBancoTransacaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DBancoTransacaoApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
