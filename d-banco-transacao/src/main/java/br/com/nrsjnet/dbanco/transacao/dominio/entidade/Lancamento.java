package br.com.nrsjnet.dbanco.transacao.dominio.entidade;

import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoLancamentoEnum;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude="id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Lancamento {

    @Id @GeneratedValue
    @Getter @EqualsAndHashCode.Include
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conta_uuid")
    @Getter @Setter
    private Conta conta;

    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private TipoLancamentoEnum tipoLancamento;

    @Getter @Setter
    private String descricao;

    @Getter @Setter
    private BigDecimal valorInformado;

    @Getter @Setter
    private BigDecimal valorTaxas;

    @Getter @Setter
    private BigDecimal valorBonus;

    @Getter @Setter
    private BigDecimal valorRealizado;

    @CreationTimestamp
    @Getter @Setter
    private LocalDate dataTransacao;
}
