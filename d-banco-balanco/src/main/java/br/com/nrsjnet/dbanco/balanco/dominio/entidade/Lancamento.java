package br.com.nrsjnet.dbanco.balanco.dominio.entidade;

import br.com.nrsjnet.dbanco.balanco.dominio.enums.TipoLancamentoEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;



@Document(collection = "lancamento")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude="id")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Lancamento {

    @Id
    @EqualsAndHashCode.Include
    private String uuid;

    private Conta conta;

    private TipoLancamentoEnum tipoLancamento;

    private String descricao;

    private BigDecimal valorInformado;

    private BigDecimal valorTaxas;

    private BigDecimal valorBonus;

    private BigDecimal valorRealizado;

    private LocalDate dataTransacao;
}
