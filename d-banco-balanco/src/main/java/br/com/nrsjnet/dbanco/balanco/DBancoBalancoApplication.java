package br.com.nrsjnet.dbanco.balanco;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DBancoBalancoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DBancoBalancoApplication.class, args);
	}


	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
//		modelMapper.typeMap(Transacao.class, TransacaoDTO.class).addMappings(mapper -> {
//			mapper.map(src -> src.getContaDestino().getSaldo(),
//					TransacaoDTO::setValorSaldo);
//		});
		return modelMapper;
	}
}
