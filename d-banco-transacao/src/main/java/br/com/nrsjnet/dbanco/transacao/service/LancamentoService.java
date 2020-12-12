package br.com.nrsjnet.dbanco.transacao.service;

import br.com.nrsjnet.dbanco.transacao.dominio.command.DepositarCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.SaqueCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.TransferenciaCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.dto.TransferenciaDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.dto.TransacaoDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoTransacaoEnum;
import br.com.nrsjnet.dbanco.transacao.producer.ContaProducer;
import br.com.nrsjnet.dbanco.transacao.producer.LancamentoProducer;
import br.com.nrsjnet.dbanco.transacao.repository.ContaRepository;
import br.com.nrsjnet.dbanco.transacao.repository.LancamentoRepository;
import br.com.nrsjnet.dbanco.transacao.negocio.AbstractLancamento;
import br.com.nrsjnet.dbanco.transacao.negocio.LancamentoFactory;
import br.com.nrsjnet.dbanco.transacao.negocio.LancamentoTransferencia;
import br.com.nrsjnet.dbanco.transacao.negocio.dto.RetornoLancamentoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class LancamentoService {

    private final ContaRepository contaRepository;

    private final LancamentoRepository lancamentoRepository;

    private final LancamentoFactory lancamentoFactory;

    private final LancamentoProducer lancamentoProducer;

    private final ContaProducer contaProducer;

    private final ModelMapper modelMapper;

    public LancamentoService(ContaRepository contaRepository, LancamentoRepository lancamentoRepository, LancamentoFactory lancamentoFactory, LancamentoProducer lancamentoProducer, ContaProducer contaProducer, ModelMapper modelMapper) {
        this.contaRepository = contaRepository;
        this.lancamentoRepository = lancamentoRepository;
        this.lancamentoFactory = lancamentoFactory;
        this.lancamentoProducer = lancamentoProducer;
        this.contaProducer = contaProducer;
        this.modelMapper = modelMapper;
    }

    public TransacaoDTO depositar(DepositarCommand command){

        AbstractLancamento lancamentoProxy = lancamentoFactory.factory(TipoTransacaoEnum.DEPOSITO, command);

        RetornoLancamentoDTO retornoLancamentoDTO = registrarLancamentos(lancamentoProxy);

        return lancamentoProxy.obterRetornoTransacao();
    }

    public TransacaoDTO sacar(SaqueCommand command){

        AbstractLancamento lancamentoProxy = lancamentoFactory.factory(TipoTransacaoEnum.SAQUE, command);

        RetornoLancamentoDTO retornoLancamentoDTO = registrarLancamentos(lancamentoProxy);

        return lancamentoProxy.obterRetornoTransacao();
    }

    public TransferenciaDTO transferir(TransferenciaCommand command){

        LancamentoTransferencia lancamentoProxy = (LancamentoTransferencia) lancamentoFactory
                .factory(TipoTransacaoEnum.TRANSFERENCIA, command);

        RetornoLancamentoDTO retornoLancamentoDTO = registrarLancamentos(lancamentoProxy);

        return lancamentoProxy.obterRetornoTransacao();
    }

    private RetornoLancamentoDTO registrarLancamentos(AbstractLancamento lancamentoProxy) {

        RetornoLancamentoDTO retornoLancamentoDTO = lancamentoProxy.prepararLancamento();

        lancamentoRepository.saveAll(retornoLancamentoDTO.getLancamentosRealizados());
        contaRepository.saveAll(retornoLancamentoDTO.getContasParaAtualizar());

        lancamentoProducer.enviar(retornoLancamentoDTO.getLancamentosRealizados());
        contaProducer.enviar(retornoLancamentoDTO.getContasParaAtualizar());

        return retornoLancamentoDTO;
    }

}
