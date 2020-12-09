package br.com.nrsjnet.dbanco.balanco.dominio.entidade;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@Document(collection = "conta")
@ToString(exclude="id")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor()
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Conta {

    @Id @EqualsAndHashCode.Include
    private String uuid;

    private String nomeCompleto;

    private String cpf;

    private BigDecimal saldo = BigDecimal.ZERO;

}