package br.com.nrsjnet.dbanco.balanco.service;

import br.com.nrsjnet.dbanco.balanco.dominio.dto.RetornoContaDTO;
import br.com.nrsjnet.dbanco.balanco.dominio.dto.RetornoLancamentoDTO;
import br.com.nrsjnet.dbanco.balanco.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.balanco.repository.ContaReactiveRepository;
import br.com.nrsjnet.dbanco.balanco.service.exceptions.ContaNaoEncontradaException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContaReactiveService {

    private final ContaReactiveRepository contaReactiveRepository;

    private final ModelMapper modelMapper;

    public ContaReactiveService(ContaReactiveRepository contaReactiveRepository, ModelMapper modelMapper) {
        this.contaReactiveRepository = contaReactiveRepository;
        this.modelMapper = modelMapper;
    }

    public Mono<RetornoContaDTO> obter(Mono<String> cpf) {
        return contaReactiveRepository.findByCpf(cpf)
                .switchIfEmpty(Mono.error(new ContaNaoEncontradaException()))
                .flatMap(c -> Mono.just(modelMapper.map(c, RetornoContaDTO.class)));
    }

}
