package br.com.nrsjnet.dbanco.conta.dominio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaCadastradaDTO {

    private UUID uuid;
    private String nomeCompleto;
    private String cpf;
}
