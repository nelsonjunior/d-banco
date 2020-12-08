package br.com.nrsjnet.dbanco.transacao.dominio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaDTO {
    private UUID uuid;
    private String nomeCompleto;
    private String cpf;
}

