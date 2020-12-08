package br.com.nrsjnet.dbanco.transacao.dominio.entidade;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@NoArgsConstructor @AllArgsConstructor()
@ToString(exclude="id")
@EqualsAndHashCode(exclude = {"nomeCompleto", "cpf", "saldo"})
public class Conta {

    @Id
    @Getter @Setter
    private UUID uuid;

    @Getter @Setter
    private String nomeCompleto;

    @Getter @Setter
    private String cpf;

    @Getter @Setter
    private BigDecimal saldo;

}