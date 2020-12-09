package br.com.nrsjnet.dbanco.transacao.service;

import br.com.nrsjnet.dbanco.transacao.dominio.command.DepositarCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.SaqueCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.TransacaoCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.TransferenciaCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.dto.TransacaoDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.dto.TransferenciaDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.dto.ValoresTransacaoDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Transacao;
import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoTransacaoEnum;
import br.com.nrsjnet.dbanco.transacao.repository.ContaRepository;
import br.com.nrsjnet.dbanco.transacao.repository.TransacaoRepository;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.ContaNaoEncontradaException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransacaoService {

    private final ContaRepository contaRepository;

    private final TransacaoRepository transacaoRepository;

    private final ModelMapper modelMapper;

    public TransacaoService(ContaRepository contaRepository, TransacaoRepository transacaoRepository, ModelMapper modelMapper) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
        this.modelMapper = modelMapper;
    }

    public TransacaoDTO depositar(DepositarCommand command){

        Transacao transacao = gravarTransacao(TipoTransacaoEnum.DEPOSITO,
                new TransacaoCommand(command.getCpf(), command.getCpf(), command.getValor()));

        atualizarSaldoConta(transacao.getTipoTransacao(), transacao.getContaDestino(), transacao.getValorRealizado());

        return modelMapper.map(transacao, TransacaoDTO.class);
    }

    public TransacaoDTO sacar(SaqueCommand command){

        Transacao transacao = gravarTransacao(TipoTransacaoEnum.SAQUE,
                new TransacaoCommand(command.getCpf(), command.getCpf(), command.getValor()));

        atualizarSaldoConta(transacao.getTipoTransacao(), transacao.getContaDestino(), transacao.getValorRealizado());

        return modelMapper.map(transacao, TransacaoDTO.class);
    }

    public TransferenciaDTO transferir(TransferenciaCommand command){

        Transacao transacao = gravarTransacao(TipoTransacaoEnum.TRANSFERENCIA,
                new TransacaoCommand(command.getCpfOrigem(), command.getCpfDestino(), command.getValor()));

        atualizarSaldoContaTransferencia(transacao);

        return modelMapper.map(transacao, TransferenciaDTO.class);
    }

    private Transacao gravarTransacao(TipoTransacaoEnum tipo, TransacaoCommand command){

        Conta contaOrigem = recuperarConta(command.getContaOrigem());
        Conta contaDestino = recuperarConta(command.getContaDestino());

        ValoresTransacaoDTO valoresTransacaoDTO = tipo.calcularValores(command);
        Transacao transacao = new Transacao();

        transacao.setContaOrigem(contaOrigem);
        transacao.setContaDestino(contaDestino);
        transacao.setTipoTransacao(tipo);
        modelMapper.map(valoresTransacaoDTO, transacao);

        tipo.validar(contaOrigem, contaDestino, valoresTransacaoDTO);

        transacaoRepository.save(transacao);

        return transacao;
    }

    private void atualizarSaldoConta(TipoTransacaoEnum tipo, Conta conta, BigDecimal valorRealizado) {

        conta.setSaldo(tipo.atualizarSaldo(conta.getSaldo(), valorRealizado));

        contaRepository.save(conta);
    }

    private void atualizarSaldoContaTransferencia(Transacao transacao) {

        atualizarSaldoConta(TipoTransacaoEnum.SAQUE, transacao.getContaOrigem(), transacao.getValorRealizado());

        atualizarSaldoConta(TipoTransacaoEnum.DEPOSITO, transacao.getContaDestino(), transacao.getValorRealizado());
    }

    private Conta recuperarConta(String cpf){
        Optional<Conta> conta = contaRepository.findByCpf(cpf);
        if(conta.isPresent()){
            return conta.get();
        }
        throw new ContaNaoEncontradaException();
    }

}
