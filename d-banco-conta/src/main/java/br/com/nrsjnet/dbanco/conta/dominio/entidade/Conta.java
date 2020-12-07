package br.com.nrsjnet.dbanco.conta.dominio.entidade;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@NoArgsConstructor @AllArgsConstructor()
@ToString(exclude="id")
@EqualsAndHashCode(exclude = {"nomeCompleto", "cpf"})
public class Conta {

    @Id @GeneratedValue
    @Getter
    private UUID uuid;

    @Getter @Setter
    private String nomeCompleto;

    @Getter @Setter
    private String cpf;

}
