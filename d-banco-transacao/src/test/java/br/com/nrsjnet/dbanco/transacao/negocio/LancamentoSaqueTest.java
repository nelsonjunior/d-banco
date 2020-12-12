package br.com.nrsjnet.dbanco.transacao.negocio;

import br.com.nrsjnet.dbanco.transacao.dominio.dto.TransacaoDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoTransacaoEnum;
import br.com.nrsjnet.dbanco.transacao.negocio.dto.DadosLancamentoSaqueDTO;
import br.com.nrsjnet.dbanco.transacao.negocio.dto.RetornoLancamentoDTO;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.ContaNaoEncontradaException;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.SaldoInsuficienteEmContaException;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.ValorNaoPermitidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class LancamentoSaqueTest {

    @Test
    @DisplayName("Deve retornar erro de conta não cadastrada")
    void deveRetornarErroContaNaoCadastrada() {
        var dados = new DadosLancamentoSaqueDTO();
        dados.setValor(new BigDecimal("100.00"));
        dados.setConta(Optional.empty());
        var lancamento = new LancamentoSaque(dados);

        assertThatExceptionOfType(ContaNaoEncontradaException.class)
                .isThrownBy(lancamento::prepararLancamento);

    }

    @Test
    @DisplayName("Deve retornar erro de valor não permitido")
    void deveRetornarErroValorNaoPermitido() {
        var dados = new DadosLancamentoSaqueDTO();
        dados.setValor(new BigDecimal("-100.00"));
        dados.setConta(Optional.empty());
        var lancamento = new LancamentoSaque(dados);

        assertThatExceptionOfType(ValorNaoPermitidoException.class)
                .isThrownBy(lancamento::prepararLancamento);

    }

    @Test
    @DisplayName("Deve retornar erro de valor saldo insuficiente")
    void deveRetornarErroSaldoInsuficiente() {
        var dados = new DadosLancamentoSaqueDTO();
        dados.setValor(new BigDecimal("100.00"));
        dados.setConta(Optional.of(new Conta(UUID.randomUUID(), "Pessoa", "01234567890", new BigDecimal("50.00"))));
        var lancamento = new LancamentoSaque(dados);

        assertThatExceptionOfType(SaldoInsuficienteEmContaException.class)
                .isThrownBy(lancamento::prepararLancamento);

    }

    @Test
    @DisplayName("Deve retornar lista de lancamentos e contas atualizadas")
    void deveRetornarLancamentoSaque() {
        var dados = new DadosLancamentoSaqueDTO();
        dados.setValor(new BigDecimal("10.00"));
        dados.setConta(Optional.of(new Conta(UUID.randomUUID(), "Pessoa", "01234567890", new BigDecimal("1000.00"))));
        var lancamento = new LancamentoSaque(dados);

        RetornoLancamentoDTO retornoLancamentoDTO = lancamento.prepararLancamento();

        assertThat(retornoLancamentoDTO).isNotNull();

        assertThat(retornoLancamentoDTO.getLancamentosRealizados().size()).isEqualTo(1);

        assertThat(retornoLancamentoDTO.getContasParaAtualizar().size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Deve retornar lista de lancamentos e contas atualizadas")
    void deveRetornarTransacaoTipoSaque() {
        var dados = new DadosLancamentoSaqueDTO();
        dados.setValor(new BigDecimal("100.00"));
        dados.setConta(Optional.of(new Conta(UUID.randomUUID(), "Pessoa", "01234567890", new BigDecimal("1000.00"))));
        var lancamento = new LancamentoSaque(dados);

        lancamento.prepararLancamento();

        TransacaoDTO transacaoDTO = lancamento.obterRetornoTransacao();

        assertThat(transacaoDTO).isNotNull();

        assertThat(transacaoDTO.getTipoTransacao()).isEqualTo(TipoTransacaoEnum.SAQUE);

    }

    @Test
    @DisplayName("Deve ser calculado taxa para saque")
    void deveSerCalculadoTaxaParaSaque() {
        var dados = new DadosLancamentoSaqueDTO();
        dados.setValor(new BigDecimal("100.00"));
        dados.setConta(Optional.of(new Conta(UUID.randomUUID(), "Pessoa Origem", "01234567890", new BigDecimal("1000.00"))));

        var lancamento = new LancamentoSaque(dados);

        lancamento.prepararLancamento();

        TransacaoDTO transacaoDTO = lancamento.obterRetornoTransacao();

        assertThat(transacaoDTO).isNotNull();
        assertThat(transacaoDTO.getValorTaxas()).isEqualTo(new BigDecimal("1.00"));

    }

    @Test
    @DisplayName("Não deve ser calculado bônus para saque")
    void naoDeveSerCalculadoBonusParaSaque() {
        var dados = new DadosLancamentoSaqueDTO();
        dados.setValor(new BigDecimal("100.00"));
        dados.setConta(Optional.of(new Conta(UUID.randomUUID(), "Pessoa Origem", "01234567890", new BigDecimal("1000.00"))));

        var lancamento = new LancamentoSaque(dados);

        lancamento.prepararLancamento();

        TransacaoDTO transacaoDTO = lancamento.obterRetornoTransacao();

        assertThat(transacaoDTO).isNotNull();
        assertThat(transacaoDTO.getValorBonus()).isEqualTo(BigDecimal.ZERO);

    }

    @Test
    @DisplayName("Deve retornar saldo atulaizado após saque")
    void deveRetornarSaldoAtualizadoAposSaque() {
        var dados = new DadosLancamentoSaqueDTO();
        dados.setValor(new BigDecimal("100.00"));
        dados.setConta(Optional.of(new Conta(UUID.randomUUID(), "Pessoa Origem", "01234567890", new BigDecimal("1000.00"))));

        var lancamento = new LancamentoSaque(dados);

        lancamento.prepararLancamento();

        TransacaoDTO transacaoDTO = lancamento.obterRetornoTransacao();

        assertThat(transacaoDTO).isNotNull();
        assertThat(transacaoDTO.getValorSaldo()).isEqualTo(new BigDecimal("899.00"));

    }

}
