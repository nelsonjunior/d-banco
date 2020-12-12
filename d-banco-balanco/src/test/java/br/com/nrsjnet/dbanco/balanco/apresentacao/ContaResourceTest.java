package br.com.nrsjnet.dbanco.balanco.apresentacao;

import br.com.nrsjnet.dbanco.balanco.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.balanco.dominio.entidade.Lancamento;
import br.com.nrsjnet.dbanco.balanco.dominio.enums.TipoLancamentoEnum;
import br.com.nrsjnet.dbanco.balanco.repository.ContaRepository;
import br.com.nrsjnet.dbanco.balanco.repository.LancamentoRepository;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = {ContaResourceTest.Initializer.class})
public class ContaResourceTest {

    @Rule
    public static MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    private static final String CPF = "01234567890";

    private static final String CPF_NAO_CADASTRADO = "91234567890";

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

            mongoDBContainer.start();

            TestPropertyValues.of(
                    "spring.data.mongodb.host=" + mongoDBContainer.getHost(),
                    "spring.data.mongodb.port=" + mongoDBContainer.getFirstMappedPort()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @BeforeAll
    void setup() {
        Conta conta = new Conta(UUID.randomUUID().toString(), "Pessoa", CPF, new BigDecimal("100.50"));
        contaRepository.save(conta);

        Lancamento lancamento = new Lancamento(UUID.randomUUID().toString(),
                conta,
                TipoLancamentoEnum.DEPOSITO,
                "Deposito de 100 com bônus de 0.5",
                new BigDecimal("100.0"),
                BigDecimal.ZERO,
                new BigDecimal("0.5"),
                new BigDecimal("100.5"),
                LocalDate.now()
        );
        lancamentoRepository.save(lancamento);

    }

    @Test
    @DisplayName("Deve realizar request obter saldo conta nao cadastrada")
    public void deveRecuperarSaldoContaNaoCadastradaEsperandoStatus400() throws Exception {

        mockMvc.perform(get(Paths.CONTAS + "/" + CPF_NAO_CADASTRADO + "/saldo")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Deve realizar request obter saldo conta")
    public void deveRecuperarSaldoContaEsperandoStatus200() throws Exception {

        mockMvc.perform(get(Paths.CONTAS + "/" + CPF)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto", is("Pessoa")))
                .andExpect(jsonPath("$.cpf", is(CPF)))
                .andExpect(jsonPath("$.saldo", is(new BigDecimal("100.5")), BigDecimal.class))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Deve realizar request para obter lancamentos de uma conta")
    public void deveRecuperarExtratoContaEsperandoStatus200() throws Exception {

        mockMvc.perform(get(Paths.CONTAS + "/" + CPF + "/extrato")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tipoLancamento", is(TipoLancamentoEnum.DEPOSITO.toString())))
                .andExpect(jsonPath("$[0].valorInformado", is(new BigDecimal("100.0")), BigDecimal.class))
                .andExpect(jsonPath("$[0].valorBonus", is(new BigDecimal("0.5")), BigDecimal.class))
                .andExpect(jsonPath("$[0].valorRealizado", is(new BigDecimal("100.5")), BigDecimal.class))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
