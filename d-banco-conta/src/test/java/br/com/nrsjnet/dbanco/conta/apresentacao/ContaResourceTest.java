package br.com.nrsjnet.dbanco.conta.apresentacao;

import br.com.nrsjnet.dbanco.conta.dominio.dto.CadastrarContaDTO;
import br.com.nrsjnet.dbanco.conta.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.conta.repository.ContaRepository;
import br.com.nrsjnet.dbanco.conta.util.JsonUtil;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @AfterEach
    public void cleanUp(){
        contaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve realizar request obter lista de contas")
    public void recuperarContasEsperandoStatus200() throws Exception {

        mockMvc.perform(get(Paths.CONTAS)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Deve realizar request para cadastrar conta")
    public void deveCadastarContaEhRetornarStatus201() throws Exception {

        var nomeCompleto = "Pessoa 01";
        var cpf = "1234567890";

        CadastrarContaDTO cadastrarContaDTO = new CadastrarContaDTO(nomeCompleto, cpf);

        mockMvc.perform(post(Paths.CONTAS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(cadastrarContaDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeCompleto", is(nomeCompleto)))
                .andExpect(jsonPath("$.cpf", is(cpf)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        Optional<Conta> contaSalva = contaRepository.findByCpf(cpf);

        assertThat(contaSalva.isPresent()).isTrue();
        assertThat(contaSalva.get().getNomeCompleto()).as("Nome Completo").isEqualTo(nomeCompleto);
        assertThat(contaSalva.get().getCpf()).as("CPF").isEqualTo(cpf);
    }

    @Test
    @DisplayName("Deve retornar erro ao cadastrar conta ja existente")
    public void deveRetornarErroAoCadastarContaJaExistenteEhRetornarStatus400() throws Exception {

        var nomeCompleto = "Pessoa Ja Cadastrada";
        var cpf = "1111111111";

        CadastrarContaDTO cadastrarContaDTO = new CadastrarContaDTO(nomeCompleto, cpf);

        mockMvc.perform(post(Paths.CONTAS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(cadastrarContaDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros[0]", is("Conta já cadastrada!")))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    @DisplayName("Deve realizar request obter a conta cadastrada")
    public void recuperarContaStatus200() throws Exception {

        mockMvc.perform(get(Paths.CONTAS + "/1111111111")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto", is("Pessoa Ja Cadastrada")))
                .andExpect(jsonPath("$.cpf", is("1111111111")))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
