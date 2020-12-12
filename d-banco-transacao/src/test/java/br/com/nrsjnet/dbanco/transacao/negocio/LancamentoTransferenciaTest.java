package br.com.nrsjnet.dbanco.transacao.negocio;

import br.com.nrsjnet.dbanco.transacao.dominio.dto.TransacaoDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoTransacaoEnum;
import br.com.nrsjnet.dbanco.transacao.negocio.dto.DadosLancamentoTransferenciaDTO;
import br.com.nrsjnet.dbanco.transacao.negocio.dto.RetornoLancamentoDTO;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.ContaNaoEncontradaException;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.SaldoInsuficienteEmContaException;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.TransferenciaNaoPermitidaMesmaContaException;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.ValorNaoPermitidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


public class LancamentoTransferenciaTest {

    @Test
    @DisplayName("Deve retornar erro de conta não cadastrada de origem")
    void deveRetornarErroContaNaoCadastradaOrigem() {
        var dados = new DadosLancamentoTransferenciaDTO();
        dados.setValor(new BigDecimal("100.00"));
        dados.setContaOrigem(Optional.empty());
        dados.setContaDestino(Optional.of(new Conta(UUID.randomUUID(), "Pessoa Destino", "01234567890", new BigDecimal("50.00"))));
        var lancamento = new LancamentoTransferencia(dados);

        assertThatExceptionOfType(ContaNaoEncontradaException.class)
                .isThrownBy(lancamento::prepararLancamento);

    }

    @Test
    @DisplayName("Deve retornar erro de conta não cadastrada de destino")
    void deveRetornarErroContaNaoCadastradaDestino() {
        var dados = new DadosLancamentoTransferenciaDTO();
        dados.setValor(new BigDecimal("100.00"));
        dados.setContaOrigem(Optional.of(new Conta(UUID.randomUUID(), "Pessoa Origem", "01234567890", new BigDecimal("50.00"))));
        dados.setContaDestino(Optional.empty());
        var lancamento = new LancamentoTransferencia(dados);

        assertThatExceptionOfType(ContaNaoEncontradaException.class)
                .isThrownBy(lancamento::prepararLancamento);

    }

    @Test
    @DisplayName("Deve retornar erro de valor não permitido")
    void deveRetornarErroValorNaoPermitido() {
        var dados = new DadosLancamentoTransferenciaDTO();
        dados.setValor(new BigDecimal("-100.00"));
        dados.setContaOrigem(Optional.of(new Conta(UUID.randomUUID(), "Pessoa Origem", "01234567890", new BigDecimal("50.00"))));
        dados.setContaDestino(Optional.of(new Conta(UUID.randomUUID(), "Pessoa Destino", "91234567890", new BigDecimal("50.00"))));
        var lancamento = new LancamentoTransferencia(dados);

        assertThatExceptionOfType(ValorNaoPermitidoException.class)
                .isThrownBy(lancamento::prepararLancamento);

    }

    @Test
    @DisplayName("Deve retornar erro de transferencia consta iguais")
    void deveRetornarErroTransferenciaContaIgual() {

        var uuidConta = UUID.randomUUID();

        var dados = new DadosLancamentoTransferenciaDTO();
        dados.setValor(new BigDecimal("10.00"));
        dados.setContaOrigem(Optional.of(new Conta(uuidConta, "Pessoa", "01234567890", new BigDecimal("50.00"))));
        dados.setContaDestino(Optional.of(new Conta(uuidConta, "Pessoa", "01234567890", new BigDecimal("50.00"))));
        var lancamento = new LancamentoTransferencia(dados);

        assertThatExceptionOfType(TransferenciaNaoPermitidaMesmaContaException.class)
                .isThrownBy(lancamento::prepararLancamento);

    }

    @Test
    @DisplayName("Deve retornar erro de valor saldo insuficiente")
    void deveRetornarErroSaldoInsuficiente() {
        var dados = new DadosLancamentoTransferenciaDTO();
        dados.setValor(new BigDecimal("100.00"));
        dados.setContaOrigem(Optional.of(new Conta(UUID.randomUUID(), "Pessoa Origem", "01234567890", new BigDecimal("50.00"))));
        dados.setContaDestino(Optional.of(new Conta(UUID.randomUUID(), "Pessoa Destino", "91234567890", new BigDecimal("50.00"))));
        var lancamento = new LancamentoTransferencia(dados);

        assertThatExceptionOfType(SaldoInsuficienteEmContaException.class)
                .isThrownBy(lancamento::prepararLancamento);

    }

    @Test
    @DisplayName("Deve retornar lista de lancamentos e contas atualizadas")
    void deveRetornarLancamentoTransferencia() {
        var dados = new DadosLancamentoTransferenciaDTO();
        dados.setValor(new BigDecimal("10.00"));
        dados.setContaOrigem(Optional.of(new Conta(UUID.randomUUID(), "Pessoa Origem", "01234567890", new BigDecimal("1000.00"))));
        dados.setContaDestino(Optional.of(new Conta(UUID.randomUUID(), "Pessoa Destino", "91234567890", new BigDecimal("50.00"))));

        var lancamento = new LancamentoTransferencia(dados);

        RetornoLancamentoDTO retornoLancamentoDTO = lancamento.prepararLancamento();

        assertThat(retornoLancamentoDTO).isNotNull();

        assertThat(retornoLancamentoDTO.getLancamentosRealizados().size()).isEqualTo(2);

        assertThat(retornoLancamentoDTO.getContasParaAtualizar().size()).isEqualTo(2);

    }

    @Test
    @DisplayName("Deve retornar lista de lancamentos e contas atualizadas")
    void deveRetornarTransacaoTipoTransferencia() {
        var dados = new DadosLancamentoTransferenciaDTO();
        dados.setValor(new BigDecimal("100.00"));
        dados.setContaOrigem(Optional.of(new Conta(UUID.randomUUID(), "Pessoa Origem", "01234567890", new BigDecimal("1000.00"))));
        dados.setContaDestino(Optional.of(new Conta(UUID.randomUUID(), "Pessoa Destino", "91234567890", new BigDecimal("50.00"))));

        var lancamento = new LancamentoTransferencia(dados);

        lancamento.prepararLancamento();

        TransacaoDTO transacaoDTO = lancamento.obterRetornoTransacao();

        assertThat(transacaoDTO).isNotNull();

        assertThat(transacaoDTO.getTipoTransacao()).isEqualTo(TipoTransacaoEnum.TRANSFERENCIA);

    }

    @Test
    @DisplayName("Deve ser alterado saldo entre as contas")
    void deveAlteracaoSaldoEntreContas() {
        var contaOrigem = new Conta(UUID.randomUUID(), "Pessoa Origem", "01234567890", new BigDecimal("1000.00"));
        var contaDestino = new Conta(UUID.randomUUID(), "Pessoa Destino", "91234567890", new BigDecimal("50.00"));

        var dados = new DadosLancamentoTransferenciaDTO();
        dados.setValor(new BigDecimal("100.00"));
        dados.setContaOrigem(Optional.of(contaOrigem));
        dados.setContaDestino(Optional.of(contaDestino));

        var lancamento = new LancamentoTransferencia(dados);

        RetornoLancamentoDTO retornoLancamentoDTO = lancamento.prepararLancamento();

        var contasAlteradas = retornoLancamentoDTO.getContasParaAtualizar();

        assertThat(contasAlteradas.get(contasAlteradas.indexOf(contaOrigem)).getSaldo()).isEqualTo(new BigDecimal("900.00"));
        assertThat(contasAlteradas.get(contasAlteradas.indexOf(contaDestino)).getSaldo()).isEqualTo(new BigDecimal("150.00"));

    }

    @Test
    @DisplayName("Não deve ser calculado bônus e taxas para transferência")
    void naoCalcularBonusZeroTaxasZero() {
        var dados = new DadosLancamentoTransferenciaDTO();
        dados.setValor(new BigDecimal("100.00"));
        dados.setContaOrigem(Optional.of(new Conta(UUID.randomUUID(), "Pessoa Origem", "01234567890", new BigDecimal("1000.00"))));
        dados.setContaDestino(Optional.of(new Conta(UUID.randomUUID(), "Pessoa Destino", "91234567890", new BigDecimal("50.00"))));

        var lancamento = new LancamentoTransferencia(dados);

        lancamento.prepararLancamento();

        TransacaoDTO transacaoDTO = lancamento.obterRetornoTransacao();

        assertThat(transacaoDTO).isNotNull();

        assertThat(transacaoDTO.getValorBonus()).isEqualTo(BigDecimal.ZERO);
        assertThat(transacaoDTO.getValorTaxas()).isEqualTo(BigDecimal.ZERO);

    }

}
