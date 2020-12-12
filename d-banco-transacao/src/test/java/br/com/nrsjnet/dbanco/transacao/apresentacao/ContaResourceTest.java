package br.com.nrsjnet.dbanco.transacao.apresentacao;

import br.com.nrsjnet.dbanco.transacao.dominio.command.DepositarCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.SaqueCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.TransferenciaCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoTransacaoEnum;
import br.com.nrsjnet.dbanco.transacao.repository.ContaRepository;
import br.com.nrsjnet.dbanco.transacao.repository.LancamentoRepository;
import br.com.nrsjnet.dbanco.transacao.util.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql(value = "/cadastarConta.sql")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class ContaResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    private static final String CPF_ORIGEM = "01234567890";

    private static final String CPF_DESTINO = "11234567890";

    private static final String CPF_INVALIDO = "41234567890";

    @AfterEach
    public void cleanUp(){
        lancamentoRepository.deleteAll();
        contaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve realizar request para realizar deposito em conta")
    public void deveRealizarDepositoEhRetornarStatus201() throws Exception {

        mockMvc.perform(post(Paths.CONTAS+"/depositar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(new DepositarCommand(CPF_ORIGEM, new BigDecimal("50.00")))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoTransacao", is(TipoTransacaoEnum.DEPOSITO.toString())))
                .andExpect(jsonPath("$.valorInformado", is(new BigDecimal("50.0")), BigDecimal.class))
                .andExpect(jsonPath("$.valorRealizado", is(new BigDecimal("50.25")), BigDecimal.class))
                .andExpect(jsonPath("$.valorBonus", is(new BigDecimal("0.25")), BigDecimal.class))
                .andExpect(jsonPath("$.valorTaxas", is(BigDecimal.ZERO), BigDecimal.class))
                .andExpect(jsonPath("$.valorSaldo", is(new BigDecimal("1050.25")), BigDecimal.class))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        Optional<Conta> contaSalva = contaRepository.findByCpf(CPF_ORIGEM);

        assertThat(contaSalva.isPresent()).isTrue();
        assertThat(contaSalva.get().getSaldo()).as("Salto Atualizado").isEqualTo(new BigDecimal("1050.25"));
    }

    @Test
    @DisplayName("Deve realizar request para realizar deposito em conta com erro")
    public void deveRealizarDepositoContaComErroEhRetornarStatus400() throws Exception {

        mockMvc.perform(post(Paths.CONTAS+"/depositar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(new DepositarCommand(CPF_INVALIDO, new BigDecimal("50.00")))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    @DisplayName("Deve realizar request para realizar saque em conta")
    public void deveRealizarSaqueEhRetornarStatus201() throws Exception {

        mockMvc.perform(post(Paths.CONTAS+"/sacar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(new SaqueCommand(CPF_ORIGEM, new BigDecimal("200.00")))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoTransacao", is(TipoTransacaoEnum.SAQUE.toString())))
                .andExpect(jsonPath("$.valorInformado", is(new BigDecimal("200.0")), BigDecimal.class))
                .andExpect(jsonPath("$.valorRealizado", is(new BigDecimal("202.0")), BigDecimal.class))
                .andExpect(jsonPath("$.valorBonus", is(BigDecimal.ZERO), BigDecimal.class))
                .andExpect(jsonPath("$.valorTaxas", is(new BigDecimal("2.0")), BigDecimal.class))
                .andExpect(jsonPath("$.valorSaldo", is(new BigDecimal("798.0")), BigDecimal.class))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        Optional<Conta> contaSalva = contaRepository.findByCpf(CPF_ORIGEM);

        assertThat(contaSalva.isPresent()).isTrue();
        assertThat(contaSalva.get().getSaldo()).as("Salto Atualizado").isEqualTo(new BigDecimal("798.00"));
    }

    @Test
    @DisplayName("Deve realizar request para realizar sacar em conta com erro")
    public void deveRealizarSacarContaComErroEhRetornarStatus400() throws Exception {

        mockMvc.perform(post(Paths.CONTAS+"/sacar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(new SaqueCommand(CPF_INVALIDO, new BigDecimal("50.00")))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    @DisplayName("Deve realizar request para realizar transferência entre contas")
    public void deveRealizarTransferenciaEhRetornarStatus201() throws Exception {

        mockMvc.perform(post(Paths.CONTAS+"/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(new TransferenciaCommand(CPF_ORIGEM, CPF_DESTINO, new BigDecimal("100.00")))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoTransacao", is(TipoTransacaoEnum.TRANSFERENCIA.toString())))
                .andExpect(jsonPath("$.valorInformado", is(new BigDecimal("100.0")), BigDecimal.class))
                .andExpect(jsonPath("$.valorRealizado", is(new BigDecimal("100.0")), BigDecimal.class))
                .andExpect(jsonPath("$.valorBonus", is(BigDecimal.ZERO), BigDecimal.class))
                .andExpect(jsonPath("$.valorTaxas", is(BigDecimal.ZERO), BigDecimal.class))
                .andExpect(jsonPath("$.valorSaldo", is(new BigDecimal("900.0")), BigDecimal.class))
                .andExpect(jsonPath("$.cpfOrigem", is(CPF_ORIGEM)))
                .andExpect(jsonPath("$.cpfDestino", is(CPF_DESTINO)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        Optional<Conta> saldoContaOrigem = contaRepository.findByCpf(CPF_ORIGEM);
        assertThat(saldoContaOrigem.isPresent()).isTrue();
        assertThat(saldoContaOrigem.get().getSaldo()).as("Salto Atualizado Conta Origem").isEqualTo(new BigDecimal("900.00"));

        Optional<Conta> saldoContaDestino = contaRepository.findByCpf(CPF_DESTINO);
        assertThat(saldoContaDestino.isPresent()).isTrue();
        assertThat(saldoContaDestino.get().getSaldo()).as("Salto Atualizado Conta Destino").isEqualTo(new BigDecimal("150.00"));
    }

    @Test
    @DisplayName("Deve realizar request para realizar transferência entre contas com erro")
    public void deveRealizarTransferirContaComErroEhRetornarStatus400() throws Exception {

        mockMvc.perform(post(Paths.CONTAS+"/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(new TransferenciaCommand(CPF_INVALIDO, CPF_DESTINO, new BigDecimal("50.00")))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }
}
