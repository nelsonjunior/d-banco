package br.com.nrsjnet.dbanco.conta.service;

import br.com.nrsjnet.dbanco.conta.dominio.dto.CadastrarContaDTO;
import br.com.nrsjnet.dbanco.conta.dominio.dto.ContaDTO;
import br.com.nrsjnet.dbanco.conta.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.conta.producer.ContaProducer;
import br.com.nrsjnet.dbanco.conta.repository.ContaRepository;
import br.com.nrsjnet.dbanco.conta.service.exceptions.ContaJaCadastradaException;
import br.com.nrsjnet.dbanco.conta.service.exceptions.ContaNaoEncontradaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql("/cadastarConta.sql")
public class ContaServiceTest {

    @Autowired
    private ContaRepository contaRepository;

    @Mock
    private ContaProducer contaProducer;

    @Autowired
    private ModelMapper modelMapper;

    private ContaService service;

    @BeforeEach
    public void setup() {
        service = new ContaService(contaProducer, contaRepository, modelMapper);
    }

    @Test
    @DisplayName("Validar objetos contexto")
    public void contextLoads() throws Exception {
        assertThat(contaRepository).isNotNull();
        assertThat(contaProducer).isNotNull();
        assertThat(modelMapper).isNotNull();
    }

    @Test
    @DisplayName("Deve salvar e retornar uma conta")
    void deveSalvarEhRetornarUmaConta() {

        doNothing().when(contaProducer).enviar(any(Conta.class));

        var cadatratContaDTO = new CadastrarContaDTO("Pessoa 01", "1234567890");
        ContaDTO contaSalva = this.service.salvar(cadatratContaDTO);
        assertThat(contaSalva).isNotNull();
    }

    @Test
    @DisplayName("Deve retornar erro ao cadastrar conta com CPF já cadastrado")
    void deveRetornarErroAoCadastrarContaComCpfCadastrado() {
        var cadatratContaDTO = new CadastrarContaDTO("Pessoa Ja Cadastrada", "1111111111");

        assertThatExceptionOfType(ContaJaCadastradaException.class)
                .isThrownBy(() -> this.service.salvar(cadatratContaDTO));

    }

    @Test
    @DisplayName("Deve retornar retornar conta cadastrada")
    void deveRetornarContaJaCadastrada() {
        ContaDTO contaExistente = this.service.obter("1111111111");
        assertThat(contaExistente).isNotNull();
        assertThat(contaExistente.getCpf()).as("CPF").isEqualTo("1111111111");
        assertThat(contaExistente.getNomeCompleto()).as("Nome Completo").isEqualTo("Pessoa Ja Cadastrada");
    }

    @Test
    @DisplayName("Deve retornar erro ao recuperar conta não cadastrada")
    void deveRetornarErroAoRecuperarContaNaoCadastrada() {
        assertThatExceptionOfType(ContaNaoEncontradaException.class)
                .isThrownBy(() -> this.service.obter("1111111112"));

    }

    @Test
    @DisplayName("Deve retornar lista de contas cadastradas")
    void deveRetornarRelacaoContasCadastradas() {
        List<ContaDTO> contasCadastradas = this.service.listar();
        assertThat(contasCadastradas).isNotNull();
        assertThat(contasCadastradas).isNotEmpty();
        assertThat(contasCadastradas.size()).isEqualTo(1);
    }

}
