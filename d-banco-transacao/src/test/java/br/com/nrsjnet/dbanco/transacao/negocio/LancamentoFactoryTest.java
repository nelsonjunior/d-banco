package br.com.nrsjnet.dbanco.transacao.negocio;

import br.com.nrsjnet.dbanco.transacao.dominio.command.DepositarCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.SaqueCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.TransferenciaCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoTransacaoEnum;
import br.com.nrsjnet.dbanco.transacao.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql("/cadastarConta.sql")
public class LancamentoFactoryTest {

    @Autowired
    private ContaRepository contaRepository;

    private LancamentoFactory lancamentoFactory;

    @BeforeEach
    public void setup(){
        lancamentoFactory = new LancamentoFactory(contaRepository);
    }

    @Test
    @DisplayName("Deve retornar uma instancia de LancamentoDeposito")
    public void deveRetornarLancamentoDeposito(){

        AbstractLancamento lancamento = lancamentoFactory.factory(TipoTransacaoEnum.DEPOSITO, new DepositarCommand("01234567890", new BigDecimal("100.00")));

        assertThat(lancamento).isNotNull();

        assertThat(lancamento).isInstanceOf(LancamentoDeposito.class);

    }

    @Test
    @DisplayName("Deve retornar uma instancia de LancamentoSaque")
    public void deveRetornarLancamentoSaque(){

        AbstractLancamento lancamento = lancamentoFactory.factory(TipoTransacaoEnum.SAQUE, new SaqueCommand("01234567890", new BigDecimal("100.00")));

        assertThat(lancamento).isNotNull();

        assertThat(lancamento).isInstanceOf(LancamentoSaque.class);

    }

    @Test
    @DisplayName("Deve retornar uma instancia de LancamentoTransferencia")
    public void deveRetornarLancamentoTransferencia(){

        AbstractLancamento lancamento = lancamentoFactory.factory(TipoTransacaoEnum.TRANSFERENCIA,
                new TransferenciaCommand("01234567890", "11234567890", new BigDecimal("100.00")));

        assertThat(lancamento).isNotNull();

        assertThat(lancamento).isInstanceOf(LancamentoTransferencia.class);

    }

}
