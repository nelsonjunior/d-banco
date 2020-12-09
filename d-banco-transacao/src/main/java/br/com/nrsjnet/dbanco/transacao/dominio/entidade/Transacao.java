package br.com.nrsjnet.dbanco.transacao.dominio.entidade;

import br.com.nrsjnet.dbanco.transacao.dominio.dto.ContaDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoTransacaoEnum;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude="id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transacao {

    @Id @GeneratedValue
    @Getter @EqualsAndHashCode.Include
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conta_origem_uuid")
    @Getter @Setter
    private Conta contaOrigem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conta_destino_uuid")
    @Getter @Setter
    private Conta contaDestino;

    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private TipoTransacaoEnum tipoTransacao;

    @Getter @Setter
    private BigDecimal valorInformado;

    @Getter @Setter
    private BigDecimal valorRealizado;

    @Getter @Setter
    private BigDecimal valorTaxas;

    @Getter @Setter
    private BigDecimal valorBonus;

    @CreationTimestamp
    @Getter @Setter
    private LocalDate dataTransacao;
}
