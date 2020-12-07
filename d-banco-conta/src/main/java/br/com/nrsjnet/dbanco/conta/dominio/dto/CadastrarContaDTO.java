package br.com.nrsjnet.dbanco.conta.dominio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarContaDTO {
    private String nomeCompleto;
    private String cpf;
}
