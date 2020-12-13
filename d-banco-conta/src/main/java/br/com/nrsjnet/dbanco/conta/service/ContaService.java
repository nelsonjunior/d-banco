package br.com.nrsjnet.dbanco.conta.service;

import br.com.nrsjnet.dbanco.conta.dominio.dto.CadastrarContaDTO;
import br.com.nrsjnet.dbanco.conta.dominio.dto.ContaDTO;
import br.com.nrsjnet.dbanco.conta.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.conta.producer.ContaProducer;
import br.com.nrsjnet.dbanco.conta.repository.ContaRepository;
import br.com.nrsjnet.dbanco.conta.service.exceptions.ContaJaCadastradaException;
import br.com.nrsjnet.dbanco.conta.service.exceptions.ContaNaoEncontradaException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    private final ContaProducer contaProducer;

    private final ModelMapper modelMapper;

    public ContaService(ContaProducer contaProducer, ContaRepository contaRepository, ModelMapper modelMapper) {
        this.contaProducer = contaProducer;
        this.contaRepository = contaRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ContaDTO salvar(CadastrarContaDTO dto) {
        validarCadastro(dto);
        Conta conta = contaRepository.save(modelMapper.map(dto, Conta.class));
        contaProducer.enviar(conta);
        return modelMapper.map(conta, ContaDTO.class);
    }

    public ContaDTO obter(String cpf) {
        Optional<Conta> conta = contaRepository.findByCpf(cpf);
        validarObterConta(conta);
        return modelMapper.map(conta.get(), ContaDTO.class);
    }

    public List<ContaDTO> listar() {
        List<ContaDTO> contas = contaRepository.findAll().stream()
                .map(c -> new ContaDTO(c.getNomeCompleto(), c.getCpf()))
                .collect(Collectors.toList());
        return contas;
    }

    private void validarCadastro(CadastrarContaDTO dto) {
        Optional<Conta> contaSalva = contaRepository.findByCpf(dto.getCpf());
        if (contaSalva.isPresent()) {
            throw new ContaJaCadastradaException();
        }
    }

    private void validarObterConta(Optional<Conta> conta) {
        if (conta.isEmpty()) {
            throw new ContaNaoEncontradaException();
        }
    }
}
