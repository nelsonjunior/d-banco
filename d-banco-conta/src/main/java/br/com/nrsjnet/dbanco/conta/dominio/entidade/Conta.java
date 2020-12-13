package br.com.nrsjnet.dbanco.conta.dominio.entidade;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
    @Column(nullable = false)
    @NotBlank(message = "Nome é obrigatório")
    private String nomeCompleto;

    @Getter @Setter
    @NotBlank(message = "CPF é obrigatório")
    @Column(unique = true, nullable = false)
    private String cpf;

}
