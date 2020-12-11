package br.com.nrsjnet.dbanco.balanco.service;

import br.com.nrsjnet.dbanco.balanco.dominio.dto.RetornoContaDTO;
import br.com.nrsjnet.dbanco.balanco.dominio.dto.RetornoLancamentoDTO;
import br.com.nrsjnet.dbanco.balanco.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.balanco.repository.ContaRepository;
import br.com.nrsjnet.dbanco.balanco.repository.LancamentoRepository;
import br.com.nrsjnet.dbanco.balanco.service.exceptions.ContaNaoEncontradaException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    private final LancamentoRepository lancamentoRepository;

    private final ModelMapper modelMapper;

    public ContaService(ContaRepository contaRepository, LancamentoRepository lancamentoRepository, ModelMapper modelMapper) {
        this.contaRepository = contaRepository;
        this.lancamentoRepository = lancamentoRepository;
        this.modelMapper = modelMapper;
    }

    public RetornoContaDTO obter(String cpf) {
        Optional<Conta> conta = contaRepository.findByCpf(cpf);
        validarObterConta(conta);
        return modelMapper.map(conta.get(), RetornoContaDTO.class);
    }

    public List<RetornoContaDTO> listar() {
        List<RetornoContaDTO> contas = contaRepository.findAll().stream()
                .map(c -> modelMapper.map(c, RetornoContaDTO.class))
                .collect(Collectors.toList());
        return contas;
    }

    private void validarObterConta(Optional<Conta> conta) {
        if (conta.isEmpty()) {
            throw new ContaNaoEncontradaException();
        }
    }

    public List<RetornoLancamentoDTO> obterExtrato(String cpf) {
        Optional<Conta> conta = contaRepository.findByCpf(cpf);
        validarObterConta(conta);

        List<RetornoLancamentoDTO> lancamentos = lancamentoRepository.findAllByConta_Cpf(conta.get().getCpf())
                .stream()
                .map(lancamento -> modelMapper.map(lancamento, RetornoLancamentoDTO.class))
                .collect(Collectors.toList());
        return lancamentos;
    }
}
